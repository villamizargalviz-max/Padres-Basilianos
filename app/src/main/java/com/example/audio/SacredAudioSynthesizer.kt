package com.example.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.os.Build
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.speech.tts.Voice
import android.util.Log
import com.example.data.ai.DailySacredSong
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

/**
 * Motor de Audio Sagrado & Canto Litúrgico con Piano Acústico de Paz y Voz Humana Natural.
 *
 * - Sintetiza una interpretación continua de Piano Contemplativo de Paz mediante modelado físico
 *   armónico con caída acústica natural (Hammer attack, armónicos decrecientes y resonancia).
 * - Incorpora atenuación suave (Audio Ducking) para que la voz orante humana resalte con nitidez y calidez.
 * - Configura la síntesis de voz (TTS) con selección de voz humana de alta calidad, entonación cálida
 *   y cadencia orante con pausas naturales de respiración (Lectio Divina).
 */
class SacredAudioSynthesizer(private val context: Context) {

    private val tag = "SacredPianoPlayer"
    private val scope = CoroutineScope(Dispatchers.Default)

    private var audioTrack: AudioTrack? = null
    private var synthJob: Job? = null
    private var progressJob: Job? = null
    private var tts: TextToSpeech? = null
    private var isTtsReady = false

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _isVoiceSpeaking = MutableStateFlow(false)
    val isVoiceSpeaking: StateFlow<Boolean> = _isVoiceSpeaking.asStateFlow()

    private val _currentPositionSec = MutableStateFlow(0)
    val currentPositionSec: StateFlow<Int> = _currentPositionSec.asStateFlow()

    private val _totalDurationSec = MutableStateFlow(160) // ~2:40 minutos
    val totalDurationSec: StateFlow<Int> = _totalDurationSec.asStateFlow()

    private val _currentSong = MutableStateFlow<DailySacredSong?>(null)
    val currentSong: StateFlow<DailySacredSong?> = _currentSong.asStateFlow()

    init {
        try {
            tts = TextToSpeech(context) { status ->
                if (status == TextToSpeech.SUCCESS) {
                    val localeEs = Locale("es", "ES")
                    var langResult = tts?.setLanguage(localeEs)
                    if (langResult == TextToSpeech.LANG_MISSING_DATA || langResult == TextToSpeech.LANG_NOT_SUPPORTED) {
                        tts?.language = Locale("es")
                    }

                    // Seleccionar la voz más humana y natural disponible en español
                    try {
                        val availableVoices = tts?.voices
                        if (!availableVoices.isNullOrEmpty()) {
                            val spanishVoices = availableVoices.filter {
                                it.locale.language.equals("es", ignoreCase = true)
                            }
                            if (spanishVoices.isNotEmpty()) {
                                // Priorizar voces de alta calidad o redes neuronales instaladas
                                val bestVoice = spanishVoices
                                    .sortedWith(
                                        compareByDescending<Voice> { it.quality }
                                            .thenByDescending { !it.isNetworkConnectionRequired }
                                            .thenBy {
                                                val n = it.name.lowercase()
                                                if (n.contains("natural") || n.contains("neural") || n.contains("es-es") || n.contains("es-us")) 0 else 1
                                            }
                                    )
                                    .firstOrNull()

                                if (bestVoice != null) {
                                    tts?.voice = bestVoice
                                    Log.d(tag, "Voz humana seleccionada: ${bestVoice.name}")
                                }
                            }
                        }
                    } catch (e: Exception) {
                        Log.w(tag, "No se pudo filtrar lista de voces: ${e.message}")
                    }

                    // Parámetros acústicos para entonación humana cálida y solemne
                    tts?.setPitch(0.98f) // Tono natural humano, cálido y reposado
                    tts?.setSpeechRate(0.84f) // Cadencia orante, reflexiva y contemplativa

                    // Escucha del progreso del habla para realizar ducking armónico del piano
                    tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                        override fun onStart(utteranceId: String?) {
                            _isVoiceSpeaking.value = true
                        }

                        override fun onDone(utteranceId: String?) {
                            _isVoiceSpeaking.value = false
                        }

                        @Deprecated("Deprecated in Java")
                        override fun onError(utteranceId: String?) {
                            _isVoiceSpeaking.value = false
                        }
                    })

                    isTtsReady = true
                }
            }
        } catch (e: Exception) {
            Log.w(tag, "Error inicializando TTS: ${e.message}")
        }
    }

    /**
     * Inicia la reproducción del cántico sagrado diario generado por la IA con Piano de Paz.
     */
    fun playSong(song: DailySacredSong, voiceRecitation: Boolean = true) {
        stop()
        _currentSong.value = song
        _currentPositionSec.value = 0
        _totalDurationSec.value = 160
        _isPlaying.value = true

        // 1. Iniciar sintetizador de piano acústico contemplativo
        startPianoPeaceSynthesizer()

        // 2. Control de tiempo y progreso
        progressJob = scope.launch {
            while (isActive && _isPlaying.value && _currentPositionSec.value < _totalDurationSec.value) {
                delay(1000)
                _currentPositionSec.value += 1
            }
            if (_currentPositionSec.value >= _totalDurationSec.value) {
                stop()
            }
        }

        // 3. Voz sagrada humana orante si está habilitada
        if (voiceRecitation && isTtsReady && tts != null) {
            scope.launch(Dispatchers.Main) {
                delay(1600) // Entrada suave del piano de paz antes de comenzar la oración
                val prayerText = humanizePrayerText(song)
                tts?.speak(prayerText, TextToSpeech.QUEUE_FLUSH, null, "sacred_piano_recitation")
            }
        }
    }

    /**
     * Convierte la letra y estructura del cántico en un texto fluido, cálido y humano
     * con pausas de respiración y respeto por la métrica orante.
     */
    private fun humanizePrayerText(song: DailySacredSong): String {
        return buildString {
            append(song.title)
            append(". ")
            append(song.subtitle)
            append("... ")

            // Limpieza y humanización de etiquetas y abreviaturas
            val cleanLyrics = song.lyrics
                .replace("C.S.B.", "Padres Basilianos")
                .replace("CSB", "Padres Basilianos")
                .replace("P.", "Padre ")
                .replace("Mons.", "Monseñor ")
                .replace("[Antífona]", "... Antífona... ")
                .replace("[Estrofa I]", "... Primera estrofa... ")
                .replace("[Estrofa II]", "... Segunda estrofa... ")
                .replace("[Estrofa III]", "... Tercera estrofa... ")
                .replace("[Doxología]", "... Doxología final... ")
                .replace("\n", "... ")
                .replace(";", ", ")
                .replace("—", ", ")
                .replace("«", "")
                .replace("»", "")
                .replace("*", "")

            append(cleanLyrics)
            append("... Amén.")
        }
    }

    /**
     * Pausa la reproducción manteniendo la posición.
     */
    fun pause() {
        _isPlaying.value = false
        _isVoiceSpeaking.value = false
        stopPianoAudio()
        try {
            tts?.stop()
        } catch (_: Exception) {}
        progressJob?.cancel()
    }

    /**
     * Reanuda la reproducción desde la posición actual.
     */
    fun resume() {
        val song = _currentSong.value ?: return
        _isPlaying.value = true
        startPianoPeaceSynthesizer()
        progressJob = scope.launch {
            while (isActive && _isPlaying.value && _currentPositionSec.value < _totalDurationSec.value) {
                delay(1000)
                _currentPositionSec.value += 1
            }
            if (_currentPositionSec.value >= _totalDurationSec.value) {
                stop()
            }
        }
    }

    /**
     * Detiene por completo la reproducción y reinicia los contadores.
     */
    fun stop() {
        _isPlaying.value = false
        _isVoiceSpeaking.value = false
        _currentPositionSec.value = 0
        progressJob?.cancel()
        stopPianoAudio()
        try {
            tts?.stop()
        } catch (_: Exception) {}
    }

    /**
     * Ajusta la posición de reproducción (Seek).
     */
    fun seekTo(seconds: Int) {
        _currentPositionSec.value = seconds.coerceIn(0, _totalDurationSec.value)
    }

    /**
     * Generador de Piano Acústico de Paz:
     * Modela notas de piano tocadas en arpegios contemplativos con ataque percusivo suave,
     * armónicos que caen exponencialmente y resonancia armónica de sala.
     */
    private fun startPianoPeaceSynthesizer() {
        stopPianoAudio()
        synthJob = scope.launch(Dispatchers.IO) {
            val sampleRate = 22050
            val bufferSize = AudioTrack.getMinBufferSize(
                sampleRate,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT
            ).coerceAtLeast(4096)

            try {
                audioTrack = AudioTrack.Builder()
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build()
                    )
                    .setAudioFormat(
                        AudioFormat.Builder()
                            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                            .setSampleRate(sampleRate)
                            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                            .build()
                    )
                    .setBufferSizeInBytes(bufferSize)
                    .setTransferMode(AudioTrack.MODE_STREAM)
                    .build()

                audioTrack?.play()

                // Progresión de acordes de piano de paz en Modo Dórico / Mayor Contemplativo (Frecuencias exactas en Hz)
                // Acordes: Re menor 9 -> Si bemol mayor 7 -> Do mayor 9 -> Fa mayor 7 -> La menor 7 -> Sol menor 7 -> Do sus4
                data class PianoNote(val freq: Double, val durationSec: Double, val volume: Double)

                val chordProgression: List<List<PianoNote>> = listOf(
                    // 1. Re menor 9 (Paz y serenidad inicial)
                    listOf(
                        PianoNote(73.42, 3.2, 0.45),   // D2 (Bajo profundo)
                        PianoNote(146.83, 2.8, 0.40),  // D3
                        PianoNote(220.00, 2.5, 0.35),  // A3
                        PianoNote(349.23, 2.2, 0.30),  // F4
                        PianoNote(523.25, 2.0, 0.28),  // C5
                        PianoNote(659.25, 2.2, 0.25)   // E5 (Novena dulce)
                    ),
                    // 2. Si bemol Mayor 7 (Elevación luminosa)
                    listOf(
                        PianoNote(58.27, 3.2, 0.45),   // Bb1
                        PianoNote(116.54, 2.8, 0.40),  // Bb2
                        PianoNote(174.61, 2.5, 0.35),  // F3
                        PianoNote(233.08, 2.2, 0.30),  // Bb3
                        PianoNote(293.66, 2.0, 0.28),  // D4
                        PianoNote(440.00, 2.2, 0.25)   // A4
                    ),
                    // 3. Do Mayor 9 (Claridad y gracia divina)
                    listOf(
                        PianoNote(65.41, 3.2, 0.45),   // C2
                        PianoNote(130.81, 2.8, 0.40),  // C3
                        PianoNote(196.00, 2.5, 0.35),  // G3
                        PianoNote(261.63, 2.2, 0.30),  // C4
                        PianoNote(329.63, 2.0, 0.28),  // E4
                        PianoNote(587.33, 2.2, 0.25)   // D5
                    ),
                    // 4. Fa Mayor 7 (Acogida fraterna)
                    listOf(
                        PianoNote(87.31, 3.2, 0.45),   // F2
                        PianoNote(174.61, 2.8, 0.40),  // F3
                        PianoNote(261.63, 2.5, 0.35),  // C4
                        PianoNote(349.23, 2.2, 0.30),  // F4
                        PianoNote(440.00, 2.0, 0.28),  // A4
                        PianoNote(523.25, 2.2, 0.25)   // C5
                    ),
                    // 5. La Menor 7 (Silencio y reflexión interior)
                    listOf(
                        PianoNote(55.00, 3.2, 0.45),   // A1
                        PianoNote(110.00, 2.8, 0.40),  // A2
                        PianoNote(164.81, 2.5, 0.35),  // E3
                        PianoNote(220.00, 2.2, 0.30),  // A3
                        PianoNote(261.63, 2.0, 0.28),  // C4
                        PianoNote(329.63, 2.2, 0.25)   // E4
                    ),
                    // 6. Sol Menor 7 -> Do sus4 (Resolución armónica de descanso)
                    listOf(
                        PianoNote(98.00, 3.2, 0.45),   // G2
                        PianoNote(146.83, 2.8, 0.38),  // D3
                        PianoNote(233.08, 2.4, 0.32),  // Bb3
                        PianoNote(349.23, 2.0, 0.28),  // F4
                        PianoNote(523.25, 2.5, 0.25)   // C5
                    )
                )

                val buffer = ShortArray(bufferSize / 2)
                var currentVolumeDucking = 0.50f
                var chordIndex = 0
                var noteInChord = 0
                var sampleCounter = 0
                val samplesPerNoteArpeggio = (sampleRate * 0.45).toInt() // arpegio cada 450 ms
                val activeNotes = mutableListOf<ActivePianoVoice>()

                while (isActive && _isPlaying.value) {
                    // Control de Ducking suave de volumen si la voz está hablando
                    val targetVolume = if (_isVoiceSpeaking.value) 0.20f else 0.52f
                    currentVolumeDucking += (targetVolume - currentVolumeDucking) * 0.08f

                    for (i in buffer.indices) {
                        // Lanzamiento de notas del arpegio de piano
                        if (sampleCounter % samplesPerNoteArpeggio == 0) {
                            val currentChord = chordProgression[chordIndex % chordProgression.size]
                            if (noteInChord < currentChord.size) {
                                val note = currentChord[noteInChord]
                                activeNotes.add(
                                    ActivePianoVoice(
                                        freq = note.freq,
                                        volume = note.volume,
                                        totalSamples = (note.durationSec * sampleRate).toInt()
                                    )
                                )
                                noteInChord++
                            } else {
                                noteInChord = 0
                                chordIndex++
                            }
                        }
                        sampleCounter++

                        // Calcular muestra compuesta de todas las voces activas de piano
                        var sampleAcc = 0.0
                        val iterator = activeNotes.iterator()
                        while (iterator.hasNext()) {
                            val voice = iterator.next()
                            sampleAcc += voice.nextSample(sampleRate)
                            if (voice.isFinished()) {
                                iterator.remove()
                            }
                        }

                        // Aplicar volumen maestro, ducking y saturación suave
                        val finalSample = (sampleAcc * currentVolumeDucking)
                            .coerceIn(-1.0, 1.0)

                        buffer[i] = (finalSample * Short.MAX_VALUE).toInt().toShort()
                    }

                    audioTrack?.write(buffer, 0, buffer.size)
                }
            } catch (e: Exception) {
                Log.w(tag, "Síntesis de piano de paz detenida: ${e.message}")
            } finally {
                try {
                    audioTrack?.stop()
                    audioTrack?.release()
                } catch (_: Exception) {}
                audioTrack = null
            }
        }
    }

    /**
     * Voz acústica de piano con envolvente ADSR (Ataque percusivo y decaimiento armónico natural)
     */
    private class ActivePianoVoice(
        val freq: Double,
        val volume: Double,
        val totalSamples: Int
    ) {
        var currentSampleIndex: Int = 0
        private var phaseFundamental = 0.0
        private var phase2nd = 0.0
        private var phase3rd = 0.0
        private var phase4th = 0.0

        fun nextSample(sampleRate: Int): Double {
            if (currentSampleIndex >= totalSamples) return 0.0

            val timeSec = currentSampleIndex.toDouble() / sampleRate
            currentSampleIndex++

            // 1. Ataque suave percusivo de macillo de piano (8 milisegundos)
            val attackEnvelope = min(1.0, timeSec / 0.008)

            // 2. Decaimiento armónico acústico de piano (las notas altas caen más rápido)
            val decayTau = max(0.8, 2.5 - (freq / 350.0))
            val decayFundamental = exp(-timeSec / decayTau)
            val decay2nd = exp(-timeSec / (decayTau * 0.70))
            val decay3rd = exp(-timeSec / (decayTau * 0.45))
            val decay4th = exp(-timeSec / (decayTau * 0.28))

            // 3. Síntesis armónica con leve inarmonicidad acústica de cuerda metálica
            val f1 = freq
            val f2 = freq * 2.002
            val f3 = freq * 3.006
            val f4 = freq * 4.012

            val s1 = sin(phaseFundamental) * 0.52 * decayFundamental
            val s2 = sin(phase2nd) * 0.26 * decay2nd
            val s3 = sin(phase3rd) * 0.14 * decay3rd
            val s4 = sin(phase4th) * 0.08 * decay4th

            phaseFundamental += 2.0 * PI * f1 / sampleRate
            phase2nd += 2.0 * PI * f2 / sampleRate
            phase3rd += 2.0 * PI * f3 / sampleRate
            phase4th += 2.0 * PI * f4 / sampleRate

            if (phaseFundamental > 2.0 * PI) phaseFundamental -= 2.0 * PI
            if (phase2nd > 2.0 * PI) phase2nd -= 2.0 * PI
            if (phase3rd > 2.0 * PI) phase3rd -= 2.0 * PI
            if (phase4th > 2.0 * PI) phase4th -= 2.0 * PI

            return (s1 + s2 + s3 + s4) * attackEnvelope * volume
        }

        fun isFinished(): Boolean = currentSampleIndex >= totalSamples
    }

    private fun stopPianoAudio() {
        synthJob?.cancel()
        synthJob = null
        try {
            audioTrack?.stop()
            audioTrack?.release()
        } catch (_: Exception) {}
        audioTrack = null
    }

    fun release() {
        stop()
        try {
            tts?.shutdown()
        } catch (_: Exception) {}
        tts = null
    }
}

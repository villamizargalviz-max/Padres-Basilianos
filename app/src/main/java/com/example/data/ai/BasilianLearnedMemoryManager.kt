package com.example.data.ai

import android.content.Context
import android.content.SharedPreferences
import org.json.JSONArray
import org.json.JSONObject

data class BasilianLearnedMemory(
    val id: String = java.util.UUID.randomUUID().toString(),
    val fact: String,
    val topic: String = "Tradición Basiliana",
    val timestamp: Long = System.currentTimeMillis(),
    val source: String = "Interacción con la Comunidad"
)

object BasilianLearnedMemoryManager {
    private const val PREFS_NAME = "basilian_learned_memory_prefs"
    private const val KEY_MEMORIES = "learned_memories_json"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    private val defaultInitialMemories = listOf(
        BasilianLearnedMemory(
            id = "mem_init_1",
            fact = "Ciro Alfonso Cárdenas y Fabien Mwehu fueron novicios en 2025; el 8 de agosto de 2026 emitieron sus primeros votos y actualmente son Escolásticos en formación teológica en el Teologado de Bogotá.",
            topic = "Vocaciones y Escolásticos",
            source = "Directorio Oficial 2026"
        ),
        BasilianLearnedMemory(
            id = "mem_init_2",
            fact = "Los novicios canónicos del ciclo 2026-2027 en Casa Annonay (Bogotá) son Diego Alejandro Martínez López, José Yonni Villa de Jesús y Jorge Mario Rodríguez Peña, bajo la dirección del Maestro de Novicios P. Maurice Luke Restivo.",
            topic = "Noviciado 2026-2027",
            source = "Casa Annonay Bogotá"
        ),
        BasilianLearnedMemory(
            id = "mem_init_3",
            fact = "El lema basiliano «Bonitatem et disciplinam et scientiam doce me» (Enséñame bondad, disciplina y ciencia) está tomado del Salmo 118 (119):66 y constituye el eje de la pedagogía y espiritualidad basiliana.",
            topic = "Espiritualidad y Lema",
            source = "Constituciones C.S.B."
        ),
        BasilianLearnedMemory(
            id = "mem_init_4",
            fact = "VERDAD OFICIAL ACTUALIZADA SOBRE CALI: Los Padres Basilianos (C.S.B.) YA NO están en Cali. Aunque su misión en Colombia comenzó históricamente en 1987 en Cali (Parroquia Ntra. Sra. de la Asunción en Andrés Sanín y Parroquia/Colegio en Los Naranjos, Aguablanca, con los padres Francisco Amico y Bob Holmes), esa etapa en Cali ya concluyó y tanto la parroquia como el colegio se separaron / fueron entregados a la Arquidiócesis de Cali. Hoy la presencia activa de los Padres Basilianos en Colombia está ÚNICAMENTE en Bogotá (Casa Annonay, Noviciado, Teologado y Parroquia Ntra. Sra. de Egipto) y en Medellín (Parroquia San Basilio).",
            topic = "Realidad Actual en Colombia y Cali",
            source = "basilian.org y Curia C.S.B."
        )
    )

    @Synchronized
    fun getMemories(context: Context): List<BasilianLearnedMemory> {
        val prefs = getPrefs(context)
        val jsonStr = prefs.getString(KEY_MEMORIES, null)
        if (jsonStr.isNullOrBlank()) {
            saveMemories(context, defaultInitialMemories)
            return defaultInitialMemories
        }
        return try {
            val jsonArray = JSONArray(jsonStr)
            val userMemories = mutableListOf<BasilianLearnedMemory>()
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                val id = obj.optString("id", java.util.UUID.randomUUID().toString())
                if (id.startsWith("mem_init_")) continue
                val fact = obj.optString("fact", "")
                if (fact.isNotBlank()) {
                    userMemories.add(
                        BasilianLearnedMemory(
                            id = id,
                            fact = fact,
                            topic = obj.optString("topic", "Tradición Basiliana"),
                            timestamp = obj.optLong("timestamp", System.currentTimeMillis()),
                            source = obj.optString("source", "Interacción")
                        )
                    )
                }
            }
            val merged = userMemories + defaultInitialMemories
            saveMemories(context, merged)
            merged
        } catch (_: Exception) {
            defaultInitialMemories
        }
    }

    @Synchronized
    fun saveMemories(context: Context, memories: List<BasilianLearnedMemory>) {
        val jsonArray = JSONArray()
        for (m in memories) {
            val obj = JSONObject().apply {
                put("id", m.id)
                put("fact", m.fact)
                put("topic", m.topic)
                put("timestamp", m.timestamp)
                put("source", m.source)
            }
            jsonArray.put(obj)
        }
        getPrefs(context).edit().putString(KEY_MEMORIES, jsonArray.toString()).apply()
    }

    @Synchronized
    fun addMemory(context: Context, fact: String, topic: String = "Aporte de la Comunidad", source: String = "Interacción Comunitaria"): BasilianLearnedMemory {
        val cleanFact = fact.trim()
        val current = getMemories(context).toMutableList()
        // Evitar duplicados exactos
        val existing = current.find { it.fact.equals(cleanFact, ignoreCase = true) }
        if (existing != null) return existing

        val newMemory = BasilianLearnedMemory(
            fact = cleanFact,
            topic = topic,
            timestamp = System.currentTimeMillis(),
            source = source
        )
        current.add(0, newMemory)
        saveMemories(context, current)
        return newMemory
    }

    @Synchronized
    fun deleteMemory(context: Context, id: String) {
        val current = getMemories(context).filter { it.id != id }
        saveMemories(context, current)
    }

    @Synchronized
    fun clearMemories(context: Context) {
        saveMemories(context, defaultInitialMemories)
    }

    /**
     * Construye el contexto de memoria acumulada que se inyecta en el prompt del sistema de Gemini.
     */
    fun getLearnedPromptContext(context: Context): String {
        val memories = getMemories(context)
        if (memories.isEmpty()) return ""
        val sb = StringBuilder()
        sb.append("MEMORIA DINÁMICA APRENDIDA Y AUTO-MEJORA CONTINUA (CONOCIMIENTOS ADICIONALES ASIMILADOS):\n")
        sb.append("Has incorporado los siguientes hechos, testimonios y datos en tu base de discernimiento para mejorar constantemente tu precisión sobre los Padres Basilianos:\n")
        memories.take(20).forEachIndexed { index, m ->
            sb.append("${index + 1}. [${m.topic}] ${m.fact} (Fuente: ${m.source})\n")
        }
        sb.append("Usa esta memoria acumulada de manera natural para responder con máxima fidelidad, sin repetir datos desactualizados.\n")
        return sb.toString()
    }

    /**
     * Detecta si el texto del usuario contiene información explícita para enseñar a la IA y la guarda automáticamente.
     */
    fun autoDetectAndLearn(context: Context, userText: String): BasilianLearnedMemory? {
        val lower = userText.lowercase().trim()
        val triggers = listOf(
            "aprende que",
            "recuerda que",
            "te enseño que",
            "un dato sobre",
            "te cuento que",
            "ten en cuenta que",
            "debes saber que",
            "actualiza que"
        )
        for (trigger in triggers) {
            val idx = lower.indexOf(trigger)
            if (idx != -1) {
                val candidateFact = userText.substring(idx + trigger.length).trim().trimStart(':', ',', '-', ' ')
                if (candidateFact.length >= 10) {
                    return addMemory(context, candidateFact, topic = "Aprendizaje Dinámico en Charla", source = "Conversación")
                }
            }
        }
        return null
    }
}

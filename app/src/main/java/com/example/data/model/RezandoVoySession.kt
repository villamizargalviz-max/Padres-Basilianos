package com.example.data.model

data class RezandoVoyStep(
    val title: String, // e.g. "1. Nos preparamos en silencio", "2. La Palabra de Dios", "3. Pistas de Reflexión", "4. Oración Basiliana", "5. Coloquio y Envío"
    val subtitle: String,
    val iconName: String,
    val body: String,
    val audioDurationSeconds: Int = 120
)

data class RezandoVoySession(
    val id: String,
    val dayOfWeek: String, // "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo", "Especial"
    val dateDisplay: String, // e.g., "Martes • San Basilio y la Vida en Común"
    val scriptureReference: String,
    val mottoFocus: String, // "Bondad", "Disciplina", "Ciencia"
    val hymnOrSongTitle: String,
    val steps: List<RezandoVoyStep>
)

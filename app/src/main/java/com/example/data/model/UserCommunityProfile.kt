package com.example.data.model

import java.util.UUID

/**
 * Perfil de Miembro de la Comunidad Basiliana (tipo red comunitaria/Facebook).
 * Permite a cualquier persona registrarse con su correo, definir su rol, santo patrono protector,
 * biografía, lema y foto de perfil.
 */
data class UserCommunityProfile(
    val id: String = UUID.randomUUID().toString(),
    val email: String,
    val fullName: String,
    val role: String = "Fiel / Amigo de San Basilio", // "Sacerdote Basiliano", "Novicio / Postulante", "Docente Basiliano", "Laico Consagrado", "Fiel"
    val patronSaint: String = "San Basilio Magno", // Santo protector
    val bio: String = "Caminando con bondad, disciplina y ciencia.",
    val cityAndCountry: String = "Cali, Colombia",
    val parishOrInstitution: String = "Parroquia San Basilio",
    val profilePhotoUri: String? = null,
    val coverPhotoUri: String? = null,
    val isCuriaMember: Boolean = false,
    val joinedYear: String = "2026",
    val favoriteQuote: String = "«Bonitatem et disciplinam et scientiam doce me»",
    val prayerIntentions: List<String> = listOf("Por las vocaciones basilianas en América Latina"),
    val birthdayDay: Int? = null,
    val birthdayMonth: Int? = null,
    val avatarStyleId: String = "avatar_basil",
    // Atributos de Vida Religiosa & Escolasticado Basiliano
    val formationStage: String = "", // Ej: "Escolasticado (Filosofía & Teología)", "Noviciado", "Votos Temporales", "Votos Perpetuos"
    val religiousHouse: String = "", // Ej: "Casa de Formación San Basilio", "Seminario Mayor", "Comunidad de Cali"
    val professionDate: String = "", // Ej: "15 de Agosto de 2023 • Primeros Votos"
    val scholasticStudies: String = "", // Ej: "Filosofía, Teología & Patrística"
    val monasticCommitment: String = "", // Ej: "Liturgia de las Horas, Lectio Divina & Regla de San Basilio"
    val lastActiveAt: Long = System.currentTimeMillis()
) {
    val isOnline: Boolean
        get() {
            val elapsed = System.currentTimeMillis() - lastActiveAt
            return elapsed in 0..(5 * 60 * 1000) // Conectado en los últimos 5 minutos
        }

    val presenceStatusText: String
        get() {
            if (isOnline) return "En línea ahora"
            val diffMs = (System.currentTimeMillis() - lastActiveAt).coerceAtLeast(0)
            val minutes = (diffMs / (1000 * 60)).coerceAtLeast(1)
            return when {
                minutes < 60 -> "Activo hace $minutes min"
                minutes < 1440 -> "Activo hace ${minutes / 60} h"
                else -> "Activo hace ${minutes / 1440} d"
            }
        }
}

/**
 * Publicación del Muro Comunitario Basiliano (estilo Facebook fraterno).
 */
data class CommunityPost(
    val id: String = UUID.randomUUID().toString(),
    val authorId: String = "",
    val authorEmail: String,
    val authorName: String,
    val authorRole: String,
    val authorPatronSaint: String,
    val authorPhotoUri: String? = null,
    val text: String,
    val imageUri: String? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val likesCount: Int = 1,
    val likedByMe: Boolean = false,
    val prayerCount: Int = 0
) {
    val content: String get() = text
    val isLikedByMe: Boolean get() = likedByMe
    val timeAgo: String
        get() {
            val diffMs = System.currentTimeMillis() - timestamp
            val minutes = (diffMs / (1000 * 60)).coerceAtLeast(1)
            return when {
                minutes < 60 -> "Hace $minutes min"
                minutes < 1440 -> "Hace ${minutes / 60} h"
                else -> "Hace ${minutes / 1440} d"
            }
        }
}

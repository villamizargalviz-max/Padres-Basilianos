package com.example.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.ui.viewmodel.BasilianViewModel

/**
 * Pantalla unificada histórica. Delega en RecursosUnifiedScreen para mantener
 * una arquitectura limpia sin pantallas ni pestañas duplicadas.
 */
@Composable
fun HistoriaUnifiedScreen(
    viewModel: BasilianViewModel,
    modifier: Modifier = Modifier
) {
    RecursosUnifiedScreen(
        viewModel = viewModel,
        initialSubTab = RecursosSubTab.HISTORIA_LIBROS,
        modifier = modifier
    )
}

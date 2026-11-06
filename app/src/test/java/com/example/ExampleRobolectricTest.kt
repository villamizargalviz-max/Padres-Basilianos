package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Padres Basilianos", appName)
  }

  @Test
  fun `copilot generates reflection from Rule of Saint Basil`() = kotlinx.coroutines.runBlocking {
    val reflection = com.example.data.ai.BasilianAiService.generateCopilotReflection(
      prompt = "Reflexión sobre vida fraterna",
      corpusCategory = "Regla de San Basilio (Vida Fraterna)"
    )
    org.junit.Assert.assertNotNull(reflection)
    org.junit.Assert.assertTrue(reflection.title.isNotBlank())
    org.junit.Assert.assertTrue(reflection.content.isNotBlank())
    org.junit.Assert.assertTrue(reflection.patristicSource.isNotBlank())
  }

  @Test
  fun `basilianViewModel initializes cleanly without NullPointerException on fresh and logged-in state`() {
    val app = ApplicationProvider.getApplicationContext<android.app.Application>()
    // 1. Fresh initialization
    val vmFresh = com.example.ui.viewmodel.BasilianViewModel(app)
    org.junit.Assert.assertNotNull(vmFresh.currentTab.value)
    vmFresh.addRegisteredBasilianEmail("nuevo.padre@gmail.com", "P. Nuevo C.S.B.")
    org.junit.Assert.assertTrue(vmFresh.isEmailInOfficialBasilianDirectory("nuevo.padre@gmail.com"))

    // 2. Authenticate and persist user profile, then re-instantiate BasilianViewModel (simulates reopening app on phone)
    vmFresh.authenticateAndLinkUserByEmail(
      email = "nuevo.padre@gmail.com",
      fullName = "P. Nuevo C.S.B.",
      role = "Sacerdote Basiliano C.S.B."
    )

    val vmReopened = com.example.ui.viewmodel.BasilianViewModel(app)
    org.junit.Assert.assertTrue(vmReopened.isBasilianPrivateAccess.value)
    org.junit.Assert.assertEquals("nuevo.padre@gmail.com", vmReopened.currentUserProfile.value?.email)
  }
}

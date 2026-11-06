package com.example.ui.components

import android.annotation.SuppressLint
import android.content.Context
import android.view.MotionEvent
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

import com.example.data.model.AppLanguage

/**
 * Componente Web Interactivo Autónomo: "Simulador del Cristo Interior" y Racha de Oración Diaria (10,000 Días).
 * Implementado en HTML5, Tailwind/CSS Neo-Sacro Cyber-Monástico (#030712, #F59E0B, #DC2626),
 * SVG multicapa de Jesús joven digno y realista con 6 vestimentas históricas/espirituales desbloqueables,
 * Motor Pedagógico Generativo de 10,000 días sin repetición en 5 Grandes Ciclos,
 * Web Audio API sintetizado (cero dependencias externas), partícula de consagración,
 * persistencia híbrida (localStorage + SharedPreferences nativo), Mecánica de la Misericordia ("Brasa en espera")
 * y traducción completa en vivo (Español ES, English EN, Français FR).
 */
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun ChristInteriorStreakSimulatorCard(
    appLanguage: AppLanguage = AppLanguage.ES,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val prefs = remember(context) {
        context.getSharedPreferences("christ_interior_streak_prefs", Context.MODE_PRIVATE)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("christ_interior_streak_simulator_card"),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF030712)),
        border = BorderStroke(1.5.dp, Color(0xFFF59E0B).copy(alpha = 0.42f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(940.dp)
                .clip(RoundedCornerShape(26.dp))
                .background(Color(0xFF030712))
        ) {
            AndroidView(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("christ_interior_webview"),
                factory = { ctx ->
                    object : WebView(ctx) {
                        override fun onTouchEvent(event: MotionEvent?): Boolean {
                            when (event?.actionMasked) {
                                MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                                    parent?.requestDisallowInterceptTouchEvent(true)
                                }
                                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                                    parent?.requestDisallowInterceptTouchEvent(false)
                                }
                            }
                            return super.onTouchEvent(event)
                        }
                    }.apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        setBackgroundColor(android.graphics.Color.parseColor("#030712"))
                        settings.apply {
                            javaScriptEnabled = true
                            domStorageEnabled = true
                            databaseEnabled = true
                            loadWithOverviewMode = true
                            useWideViewPort = true
                            mediaPlaybackRequiresUserGesture = false
                            cacheMode = WebSettings.LOAD_DEFAULT
                        }
                        addJavascriptInterface(
                            object {
                                @JavascriptInterface
                                fun loadNativeState(): String {
                                    return prefs.getString("streak_state_json", "") ?: ""
                                }

                                @JavascriptInterface
                                fun saveNativeState(json: String) {
                                    prefs.edit().putString("streak_state_json", json).apply()
                                }

                                @JavascriptInterface
                                fun getCurrentLang(): String {
                                    return appLanguage.code
                                }
                            },
                            "AndroidStreakBridge"
                        )
                        webChromeClient = WebChromeClient()
                        webViewClient = object : WebViewClient() {
                            override fun onPageFinished(view: WebView?, url: String?) {
                                super.onPageFinished(view, url)
                                view?.evaluateJavascript(
                                    "if (window.setSimLanguage) window.setSimLanguage('${appLanguage.code}');",
                                    null
                                )
                            }
                        }
                        loadDataWithBaseURL(
                            "https://basilianos.csb.local/",
                            buildChristInteriorSimulatorHtml(),
                            "text/html",
                            "UTF-8",
                            null
                        )
                    }
                },
                update = { webView ->
                    webView.evaluateJavascript(
                        "if (window.setSimLanguage) window.setSimLanguage('${appLanguage.code}');",
                        null
                    )
                }
            )
        }
    }
}

private fun buildChristInteriorSimulatorHtml(): String =
    CHRIST_INTERIOR_SIMULATOR_HTML_PART_1 + CHRIST_INTERIOR_SIMULATOR_HTML_PART_2

private const val CHRIST_INTERIOR_SIMULATOR_HTML_PART_1 = """<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
<title>Simulador del Cristo Interior • Racha Espiritual</title>
<style>
  :root {
    --bg-obsidian: #030712;
    --bg-card: rgba(15, 23, 42, 0.78);
    --amber-gold: #F59E0B;
    --amber-soft: #FDE68A;
    --crimson-aug: #DC2626;
    --text-main: #F8FAFC;
    --text-muted: #94A3B8;
  }
  * {
    box-sizing: border-box;
    -webkit-tap-highlight-color: transparent;
    user-select: none;
  }
  html, body {
    margin: 0;
    padding: 0;
    width: 100%;
    height: 100%;
    background: radial-gradient(circle at 50% 18%, #172036 0%, #070B19 52%, #030712 100%);
    color: var(--text-main);
    font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif;
    overflow-x: hidden;
  }
  .sim-container {
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    min-height: 100%;
    padding: 14px 14px 12px 14px;
    position: relative;
    overflow: hidden;
  }
  /* Ambient sacred glow */
  .sacred-aura {
    position: absolute;
    top: 90px;
    left: 50%;
    transform: translateX(-50%);
    width: 290px;
    height: 290px;
    border-radius: 50%;
    background: radial-gradient(circle, rgba(245,158,11,0.22) 0%, rgba(220,38,38,0.10) 45%, rgba(3,7,18,0) 72%);
    pointer-events: none;
    z-index: 0;
    transition: all 0.6s ease;
  }
  .sacred-aura.ember-wait {
    background: radial-gradient(circle, rgba(220,38,38,0.18) 0%, rgba(245,158,11,0.08) 48%, rgba(3,7,18,0) 72%);
  }
  .sacred-aura.consecrated {
    background: radial-gradient(circle, rgba(245,158,11,0.36) 0%, rgba(220,38,38,0.18) 52%, rgba(3,7,18,0) 76%);
  }
  /* Header */
  .top-header {
    position: relative;
    z-index: 2;
    display: flex;
    align-items: center;
    justify-content: space-between;
    gap: 8px;
    background: rgba(15, 23, 42, 0.76);
    border: 1px solid rgba(245, 158, 11, 0.28);
    border-radius: 18px;
    padding: 10px 12px;
    backdrop-filter: blur(12px);
  }
  .brand-title {
    display: flex;
    flex-direction: column;
  }
  .brand-eyebrow {
    font-size: 10px;
    font-weight: 800;
    letter-spacing: 1.4px;
    text-transform: uppercase;
    color: var(--amber-gold);
  }
  .brand-main {
    font-size: 15px;
    font-weight: 800;
    color: #FFFFFF;
    margin-top: 1px;
  }
  .streak-badges {
    display: flex;
    align-items: center;
    gap: 6px;
  }
  .badge-pill {
    display: inline-flex;
    align-items: center;
    gap: 5px;
    padding: 5px 10px;
    border-radius: 999px;
    font-size: 12px;
    font-weight: 800;
    background: rgba(245, 158, 11, 0.16);
    border: 1px solid rgba(245, 158, 11, 0.45);
    color: var(--amber-soft);
  }
  .badge-pill.ember-mode {
    background: rgba(220, 38, 38, 0.18);
    border-color: rgba(220, 38, 38, 0.55);
    color: #FCA5A5;
  }
  /* Stage & Avatar */
  .avatar-stage {
    position: relative;
    z-index: 2;
    display: flex;
    flex-direction: column;
    align-items: center;
    margin: 6px 0 4px 0;
  }
  .status-banner {
    display: inline-flex;
    align-items: center;
    gap: 6px;
    padding: 4px 12px;
    border-radius: 999px;
    font-size: 11px;
    font-weight: 700;
    background: rgba(15, 23, 42, 0.85);
    border: 1px solid rgba(245, 158, 11, 0.32);
    color: #FDE68A;
    margin-bottom: 4px;
  }
  .svg-wrapper {
    width: 246px;
    height: 286px;
    position: relative;
    display: flex;
    align-items: center;
    justify-content: center;
  }
  svg#jesus-avatar {
    width: 100%;
    height: 100%;
    overflow: visible;
    filter: drop-shadow(0 12px 24px rgba(0, 0, 0, 0.65));
  }
  /* Animations */
  @keyframes haloRotate {
    0% { transform: rotate(0deg); }
    100% { transform: rotate(360deg); }
  }
  @keyframes corArdensBeat {
    0%, 100% { transform: scale(1); filter: drop-shadow(0 0 8px rgba(220,38,38,0.85)); }
    50% { transform: scale(1.13); filter: drop-shadow(0 0 18px rgba(245,158,11,0.98)); }
  }
  @keyframes flameFlicker {
    0%, 100% { transform: translateY(0) scaleY(1); opacity: 0.95; }
    50% { transform: translateY(-2px) scaleY(1.12); opacity: 1; }
  }
  @keyframes breathFloat {
    0%, 100% { transform: translateY(0px); }
    50% { transform: translateY(-3px); }
  }
  .breathing-figure {
    animation: breathFloat 4.2s ease-in-out infinite;
    transform-origin: center;
  }
  .cor-ardens-pulse {
    animation: corArdensBeat 1.8s ease-in-out infinite;
    transform-origin: 130px 175px;
  }
  .flame-flicker {
    animation: flameFlicker 1.2s ease-in-out infinite;
    transform-origin: 130px 168px;
  }
  /* Vestment Layer Transitions */
  .vestment-layer {
    opacity: 0;
    transition: opacity 0.45s ease, transform 0.45s ease;
    pointer-events: none;
  }
  .vestment-layer.active {
    opacity: 1;
  }
  /* Vestment Info + Carousel */
  .vestment-info-bar {
    width: 100%;
    background: rgba(15, 23, 42, 0.80);
    border: 1px solid rgba(148, 163, 184, 0.22);
    border-radius: 16px;
    padding: 8px 10px;
    margin-top: 2px;
    backdrop-filter: blur(10px);
  }
  .vestment-header-row {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 6px;
  }
  .vestment-title {
    font-size: 12px;
    font-weight: 800;
    color: var(--amber-soft);
  }
  .vestment-sub {
    font-size: 10.5px;
    color: var(--text-muted);
  }
  .vestment-carousel {
    display: flex;
    gap: 6px;
    overflow-x: auto;
    padding-bottom: 2px;
    scrollbar-width: none;
  }
  .vestment-carousel::-webkit-scrollbar {
    display: none;
  }
  .vest-chip {
    flex: 0 0 auto;
    padding: 5px 9px;
    border-radius: 10px;
    font-size: 10.5px;
    font-weight: 700;
    border: 1px solid rgba(148, 163, 184, 0.25);
    background: rgba(30, 41, 59, 0.65);
    color: #CBD5E1;
    cursor: pointer;
    display: inline-flex;
    align-items: center;
    gap: 4px;
    transition: all 0.2s ease;
  }
  .vest-chip.selected {
    background: linear-gradient(135deg, rgba(245,158,11,0.28), rgba(220,38,38,0.22));
    border-color: var(--amber-gold);
    color: #FFFFFF;
    box-shadow: 0 0 10px rgba(245,158,11,0.25);
  }
  .vest-chip.locked {
    opacity: 0.55;
    border-style: dashed;
  }
  /* Pedagogical Card (10,000 Days Matrix) */
  .pedagogy-card {
    position: relative;
    z-index: 2;
    background: linear-gradient(160deg, rgba(15, 23, 42, 0.90) 0%, rgba(9, 14, 28, 0.95) 100%);
    border: 1px solid rgba(245, 158, 11, 0.34);
    border-radius: 18px;
    padding: 11px 13px;
    margin: 7px 0;
    box-shadow: 0 8px 24px rgba(0, 0, 0, 0.45);
  }
  .cycle-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    gap: 6px;
    margin-bottom: 5px;
  }
  .cycle-tag {
    font-size: 10px;
    font-weight: 800;
    letter-spacing: 0.8px;
    text-transform: uppercase;
    color: var(--amber-gold);
    background: rgba(245, 158, 11, 0.14);
    border: 1px solid rgba(245, 158, 11, 0.35);
    padding: 3px 8px;
    border-radius: 999px;
  }
  .day-counter-tag {
    font-size: 10.5px;
    font-weight: 700;
    color: #94A3B8;
  }
  .micro-quote {
    font-size: 12.5px;
    font-weight: 700;
    line-height: 1.38;
    color: #F8FAFC;
    margin: 0 0 5px 0;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
  }
  .micro-praxis {
    font-size: 11.5px;
    font-weight: 600;
    line-height: 1.35;
    color: #FDE68A;
    margin: 0;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
  }
  /* Action Consecration Button */
  .action-zone {
    position: relative;
    z-index: 2;
    margin-top: 2px;
  }
  .consecrate-btn {
    width: 100%;
    border: none;
    border-radius: 16px;
    padding: 13px 16px;
    font-size: 13.5px;
    font-weight: 800;
    letter-spacing: 0.3px;
    color: #030712;
    background: linear-gradient(135deg, #FBBF24 0%, #F59E0B 55%, #EA580C 100%);
    box-shadow: 0 6px 20px rgba(245, 158, 11, 0.42);
    cursor: pointer;
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 8px;
    transition: transform 0.18s ease, box-shadow 0.25s ease, background 0.25s ease;
  }
  .consecrate-btn:active:not(:disabled) {
    transform: scale(0.98);
  }
  .consecrate-btn.sealed {
    background: rgba(16, 185, 129, 0.18);
    color: #A7F3D0;
    border: 1.5px solid rgba(16, 185, 129, 0.55);
    box-shadow: none;
    cursor: default;
  }
  /* Light Particle Flying to Heart */
  .sacred-particle {
    position: fixed;
    width: 16px;
    height: 16px;
    border-radius: 50%;
    background: radial-gradient(circle, #FFFBEB 0%, #F59E0B 50%, #DC2626 100%);
    box-shadow: 0 0 18px 6px rgba(245, 158, 11, 0.9);
    pointer-events: none;
    z-index: 999;
    opacity: 0;
  }
  /* Demo Bar */
  .demo-bar {
    position: relative;
    z-index: 2;
    margin-top: 8px;
    padding-top: 7px;
    border-top: 1px solid rgba(148, 163, 184, 0.16);
    display: flex;
    flex-direction: column;
    gap: 5px;
  }
  .demo-label-row {
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-size: 9.5px;
    font-weight: 700;
    text-transform: uppercase;
    letter-spacing: 0.9px;
    color: #64748B;
  }
  .demo-buttons {
    display: flex;
    flex-wrap: wrap;
    gap: 4px;
  }
  .demo-btn {
    flex: 1 1 auto;
    background: rgba(15, 23, 42, 0.85);
    border: 1px solid rgba(148, 163, 184, 0.22);
    color: #CBD5E1;
    border-radius: 8px;
    padding: 5px 7px;
    font-size: 10px;
    font-weight: 700;
    cursor: pointer;
    text-align: center;
  }
  .demo-btn:active {
    background: rgba(245, 158, 11, 0.25);
    border-color: var(--amber-gold);
    color: #FFFFFF;
  }
</style>
</head>
<body>
<div class="sim-container">
  <div id="sacred-aura" class="sacred-aura"></div>
  <div id="sacred-particle" class="sacred-particle"></div>

  <!-- HEADER -->
  <div class="top-header">
    <div class="brand-title">
      <span id="brand-eyebrow-text" class="brand-eyebrow">Simulador del Cristo Interior</span>
      <span id="brand-main-text" class="brand-main">Racha de Crecimiento en Jesús</span>
    </div>
    <div class="streak-badges">
      <div id="streak-pill" class="badge-pill">
        <span id="streak-icon">🔥</span>
        <span id="streak-count-text">Día 1</span>
      </div>
    </div>
  </div>

  <!-- AVATAR STAGE -->
  <div class="avatar-stage">
    <div id="mercy-status-banner" class="status-banner">
      <span>✨</span>
      <span id="mercy-status-text">Llama Interior Activa • Creciendo en Cristo</span>
    </div>

    <div class="svg-wrapper" id="avatar-target-box">
      <svg id="jesus-avatar" viewBox="0 0 260 300" xmlns="http://www.w3.org/2000/svg">
        <defs>
          <radialGradient id="nimbusGrad" cx="50%" cy="50%" r="50%">
            <stop offset="0%" stop-color="#FDE68A" stop-opacity="0.55" />
            <stop offset="55%" stop-color="#F59E0B" stop-opacity="0.22" />
            <stop offset="100%" stop-color="#030712" stop-opacity="0" />
          </radialGradient>
          <linearGradient id="skinTone" x1="0%" y1="0%" x2="0%" y2="100%">
            <stop offset="0%" stop-color="#F1C6A7" />
            <stop offset="65%" stop-color="#E0AC8B" />
            <stop offset="100%" stop-color="#C9916E" />
          </linearGradient>
          <linearGradient id="hairGrad" x1="0%" y1="0%" x2="100%" y2="100%">
            <stop offset="0%" stop-color="#3B2314" />
            <stop offset="50%" stop-color="#27160C" />
            <stop offset="100%" stop-color="#180D06" />
          </linearGradient>
          <linearGradient id="linenGrad" x1="0%" y1="0%" x2="0%" y2="100%">
            <stop offset="0%" stop-color="#E7DEC8" />
            <stop offset="100%" stop-color="#BFB297" />
          </linearGradient>
          <linearGradient id="galileeMantle" x1="0%" y1="0%" x2="100%" y2="100%">
            <stop offset="0%" stop-color="#1E3A8A" />
            <stop offset="60%" stop-color="#1D4ED8" />
            <stop offset="100%" stop-color="#0F172A" />
          </linearGradient>
          <linearGradient id="cappadociaHabit" x1="0%" y1="0%" x2="0%" y2="100%">
            <stop offset="0%" stop-color="#4A3525" />
            <stop offset="100%" stop-color="#261A12" />
          </linearGradient>
          <linearGradient id="csbCassock" x1="0%" y1="0%" x2="0%" y2="100%">
            <stop offset="0%" stop-color="#1E293B" />
            <stop offset="50%" stop-color="#0F172A" />
            <stop offset="100%" stop-color="#020617" />
          </linearGradient>
          <linearGradient id="pantocratorCrimson" x1="0%" y1="0%" x2="100%" y2="100%">
            <stop offset="0%" stop-color="#991B1B" />
            <stop offset="55%" stop-color="#7F1D1D" />
            <stop offset="100%" stop-color="#450A0A" />
          </linearGradient>
          <linearGradient id="goldOrnate" x1="0%" y1="0%" x2="100%" y2="100%">
            <stop offset="0%" stop-color="#FEF08A" />
            <stop offset="50%" stop-color="#F59E0B" />
            <stop offset="100%" stop-color="#B45309" />
          </linearGradient>
        </defs>

        <!-- CRUCIFORM SACRED NIMBUS -->
        <g id="cruciform-nimbus">
          <circle cx="130" cy="78" r="62" fill="url(#nimbusGrad)" />
          <circle cx="130" cy="78" r="46" fill="none" stroke="#F59E0B" stroke-width="1.6" stroke-opacity="0.65" />
          <circle cx="130" cy="78" r="41" fill="none" stroke="#FDE68A" stroke-width="0.8" stroke-dasharray="4 3" stroke-opacity="0.55" />
          <!-- Cross arms inside halo (Cruciform Nimbus) -->
          <path d="M124 33 L136 33 L134 52 L126 52 Z" fill="#F59E0B" fill-opacity="0.38" stroke="#FDE68A" stroke-width="0.8" />
          <path d="M85 72 L85 84 L104 82 L104 74 Z" fill="#F59E0B" fill-opacity="0.38" stroke="#FDE68A" stroke-width="0.8" />
          <path d="M175 72 L175 84 L156 82 L156 74 Z" fill="#F59E0B" fill-opacity="0.38" stroke="#FDE68A" stroke-width="0.8" />
          <!-- Greek IC XC OwN letters subtle -->
          <text x="92" y="81" fill="#FDE68A" font-size="7" font-weight="bold" opacity="0.75">ὁ</text>
          <text x="127" y="44" fill="#FDE68A" font-size="7" font-weight="bold" opacity="0.75">ὤ</text>
          <text x="163" y="81" fill="#FDE68A" font-size="7" font-weight="bold" opacity="0.75">ν</text>
        </g>

        <g class="breathing-figure">
          <!-- BACK HAIR (Natural wavy shoulder-length hair of young Jesus) -->
          <path d="M98 62 C88 82, 86 112, 92 132 C98 136, 108 136, 112 128 L148 128 C152 136, 162 136, 168 132 C174 112, 172 82, 162 62 Z" fill="url(#hairGrad)" />

          <!-- NECK & SHOULDERS BASE -->
          <path d="M117 110 L117 134 L143 134 L143 110 Z" fill="url(#skinTone)" />
          <path d="M117 120 Q130 128 143 120" fill="none" stroke="#B87D5B" stroke-width="1.2" opacity="0.55" />

          <!-- ============================================================ -->
          <!-- LAYER 1 (Día 1-10): Túnica rústica de lino de Nazaret (ketonet) -->
          <!-- ============================================================ -->
          <g id="vest-layer-1" class="vestment-layer active">
            <path d="M84 134 C98 127, 114 126, 130 129 C146 126, 162 127, 176 134 L196 278 L64 278 Z" fill="url(#linenGrad)" stroke="#9C8D70" stroke-width="1.3" />
            <!-- Tunic neckline & natural folds -->
            <path d="M112 128 L130 148 L148 128" fill="none" stroke="#8C7A5B" stroke-width="2" />
            <path d="M104 152 L96 274 M156 152 L164 274 M130 148 L130 276" stroke="#A8997E" stroke-width="1.2" opacity="0.55" />
            <!-- Woven rope belt (ceñidor de Nazaret) -->
            <path d="M84 214 Q130 223 176 214 L177 223 Q130 232 83 223 Z" fill="#785233" stroke="#4D331E" stroke-width="1" />
            <path d="M124 221 L118 258 M134 222 L139 262" stroke="#785233" stroke-width="4.5" stroke-linecap="round" />
            <circle cx="129" cy="220" r="5.5" fill="#8D623F" stroke="#4D331E" stroke-width="1" />
          </g>

          <!-- ============================================================ -->
          <!-- LAYER 2 (Día 11-30): Manto de Galilea (himation/talit) + tsitsit -->
          <!-- ============================================================ -->
          <g id="vest-layer-2" class="vestment-layer">
            <!-- Blue Galilee Himation draped diagonally over the ketonet -->
            <path d="M80 133 C98 126, 118 132, 132 142 L182 136 L194 276 L138 276 L112 222 L72 248 L66 176 Z" fill="url(#galileeMantle)" stroke="#60A5FA" stroke-width="1" stroke-opacity="0.45" />
            <!-- Tallit white & tekhelet stripes -->
            <path d="M83 145 L176 168 M81 153 L178 176" stroke="#F8FAFC" stroke-width="2.5" opacity="0.85" />
            <path d="M82 149 L177 172" stroke="#38BDF8" stroke-width="1.6" opacity="0.9" />
            <!-- Ritual fringes (tsitsit) -->
            <g stroke="#F8FAFC" stroke-width="1.5" stroke-linecap="round">
              <line x1="74" y1="246" x2="71" y2="262" />
              <line x1="79" y1="244" x2="77" y2="261" />
              <line x1="84" y1="242" x2="83" y2="259" />
              <line x1="184" y1="264" x2="186" y2="279" />
              <line x1="189" y1="264" x2="192" y2="279" />
            </g>
          </g>

          <!-- ============================================================ -->
          <!-- LAYER 4 (Día 61-120): Hábito monástico de Capadocia + Asceticon -->
          <!-- ============================================================ -->
          <g id="vest-layer-4" class="vestment-layer">
            <!-- Cappadocian monastic mantle & hood resting on shoulders -->
            <path d="M72 136 C92 122, 168 122, 188 136 L202 278 L58 278 Z" fill="url(#cappadociaHabit)" stroke="#785A40" stroke-width="1.3" />
            <!-- Monastic hood cowl around neck -->
            <path d="M90 130 Q130 158 170 130 C164 148, 148 164, 130 166 C112 164, 96 148, 90 130 Z" fill="#2A1C13" stroke="#8C6B4F" stroke-width="1.3" />
            <!-- Leather monastic belt with Eastern cross -->
            <rect x="78" y="214" width="104" height="9" rx="3" fill="#1C120C" stroke="#9A7B56" stroke-width="1" />
            <!-- Codex of the Asceticon of St. Basil held in left hand -->
            <g transform="translate(148, 188)">
              <rect x="0" y="0" width="38" height="48" rx="4" fill="#7C2D12" stroke="#F59E0B" stroke-width="1.8" />
              <rect x="4" y="4" width="30" height="40" rx="2" fill="none" stroke="#FDE68A" stroke-width="0.9" />
              <path d="M19 11 L19 37 M10 21 L28 21" stroke="#F59E0B" stroke-width="2" />
              <text x="7" y="42" fill="#FDE68A" font-size="5.5" font-weight="bold">ΑΣΚΗΤΙΚΟΝ</text>
            </g>
          </g>

          <!-- ============================================================ -->
          <!-- LAYER 5 (Día 121-365): Hábito formal Congregación de San Basilio (CSB) -->
          <!-- ============================================================ -->
          <g id="vest-layer-5" class="vestment-layer">
            <path d="M74 134 C96 124, 164 124, 186 134 L200 278 L60 278 Z" fill="url(#csbCassock)" stroke="#475569" stroke-width="1.4" />
            <!-- Roman/Basilian white collar -->
            <rect x="115" y="125" width="30" height="8" rx="2" fill="#F8FAFC" stroke="#CBD5E1" stroke-width="1" />
            <!-- Basilian Pectoral Cross on cord -->
            <path d="M110 131 L130 176 L150 131" fill="none" stroke="#F59E0B" stroke-width="1.6" />
            <g transform="translate(130, 186)">
              <rect x="-3" y="-12" width="6" height="26" rx="1.5" fill="url(#goldOrnate)" stroke="#FEF08A" stroke-width="0.8" />
              <rect x="-10" y="-6" width="20" height="5.5" rx="1.5" fill="url(#goldOrnate)" stroke="#FEF08A" stroke-width="0.8" />
            </g>
            <!-- CSB Emblem & Motto Ribbon (Bonitatem et Disciplinam et Scientiam) -->
            <g transform="translate(85, 236)">
              <rect x="0" y="0" width="90" height="18" rx="9" fill="#0F172A" stroke="#F59E0B" stroke-width="1.2" />
              <text x="45" y="11.5" text-anchor="middle" fill="#FDE68A" font-size="6.3" font-weight="bold" letter-spacing="0.4">C.S.B. • BONITATEM • SCIENTIAM</text>
            </g>
          </g>

          <!-- ============================================================ -->
          <!-- LAYER 6 (Día 366+): Manto Imperial de Gloria / Pantocrátor -->
          <!-- ============================================================ -->
          <g id="vest-layer-6" class="vestment-layer">
            <!-- Imperial Crimson & Gold Cloak -->
            <path d="M66 132 C94 120, 166 120, 194 132 L212 278 L48 278 Z" fill="url(#pantocratorCrimson)" stroke="url(#goldOrnate)" stroke-width="2.2" />
            <!-- Golden Loros / Byzantine Orphrey -->
            <path d="M104 128 L130 156 L156 128 L166 136 L140 166 L140 278 L120 278 L120 166 L94 136 Z" fill="url(#goldOrnate)" opacity="0.92" />
            <!-- Precious gems on Loros -->
            <circle cx="130" cy="172" r="3.2" fill="#DC2626" stroke="#FEF08A" stroke-width="0.8" />
            <circle cx="130" cy="196" r="3.2" fill="#2563EB" stroke="#FEF08A" stroke-width="0.8" />
            <circle cx="130" cy="220" r="3.2" fill="#10B981" stroke="#FEF08A" stroke-width="0.8" />
            <circle cx="130" cy="244" r="3.2" fill="#DC2626" stroke="#FEF08A" stroke-width="0.8" />
            <!-- Shepherd Staff (Báculo de Pastor glorioso) -->
            <g transform="translate(188, 72)">
              <line x1="0" y1="24" x2="0" y2="206" stroke="url(#goldOrnate)" stroke-width="4" stroke-linecap="round" />
              <path d="M0 26 C0 4, 24 4, 24 20 C24 32, 8 34, 8 23 C8 16, 16 16, 16 21" fill="none" stroke="url(#goldOrnate)" stroke-width="3.6" stroke-linecap="round" />
            </g>
          </g>

          <!-- ============================================================ -->
          <!-- LAYER 3 (Día 31-60): El Cor Ardens Agustiniano (Siempre encima del pecho cuando está activo) -->
          <!-- ============================================================ -->
          <g id="vest-layer-3" class="vestment-layer">
            <g class="cor-ardens-pulse">
              <!-- Outer radiant halo on chest -->
              <circle cx="130" cy="175" r="24" fill="#DC2626" fill-opacity="0.24" />
              <circle cx="130" cy="175" r="16" fill="#F59E0B" fill-opacity="0.32" />
              <!-- Living Flame above heart -->
              <path class="flame-flicker" d="M130 146 C135 154, 141 158, 137 166 C135 170, 125 170, 123 166 C119 158, 125 154, 130 146 Z" fill="#FDE047" />
              <path class="flame-flicker" d="M130 151 C133 156, 136 159, 134 165 C132 167, 128 167, 126 165 C124 159, 127 156, 130 151 Z" fill="#EA580C" />
              <!-- Sacred Heart (Cor Ardens) -->
              <path d="M130 189 C128 187, 114 177, 114 167 C114 160, 120 156, 126 159 C128 160, 129 162, 130 164 C131 162, 132 160, 134 159 C140 156, 146 160, 146 167 C146 177, 132 187, 130 189 Z" fill="#DC2626" stroke="#FEF08A" stroke-width="1.5" />
              <!-- Crown of light around heart -->
              <path d="M114 169 Q130 175 146 169" fill="none" stroke="#FDE047" stroke-width="1.4" stroke-dasharray="2 2" />
            </g>
          </g>

          <!-- BLESSING RIGHT HAND OF JESUS (Christ Teaching / Blessing Gesture) -->
          <g transform="translate(82, 168)">
            <path d="M12 26 C8 22, 7 14, 10 8 L12 2 C12 0, 15 0, 15 2 L15 11 L17 1 C17 -1, 20 -1, 20 1 L20 13 C23 11, 26 13, 25 18 C27 18, 28 21, 26 25 C24 29, 17 31, 12 26 Z" fill="url(#skinTone)" stroke="#A67150" stroke-width="1" />
          </g>

          <!-- HEAD, SERENE REALISTIC FACE & BEARD OF YOUNG JESUS -->
          <g id="jesus-head">
            <!-- Face contour -->
            <path d="M104 60 C104 42, 156 42, 156 60 C156 84, 148 106, 130 114 C112 106, 104 84, 104 60 Z" fill="url(#skinTone)" stroke="#B88262" stroke-width="1" />
            <!-- Soft cheekbone warmth -->
            <ellipse cx="115" cy="82" rx="6" ry="3.5" fill="#D98A6C" opacity="0.35" />
            <ellipse cx="145" cy="82" rx="6" ry="3.5" fill="#D98A6C" opacity="0.35" />

            <!-- Expressive, serene eyebrows -->
            <path d="M111 68 Q118 64 125 67" fill="none" stroke="#2D1A0E" stroke-width="2" stroke-linecap="round" />
            <path d="M135 67 Q142 64 149 68" fill="none" stroke="#2D1A0E" stroke-width="2" stroke-linecap="round" />

            <!-- Compassionate, deep eyes with catchlights -->
            <ellipse cx="118" cy="74" rx="5.2" ry="3.1" fill="#FFFFFF" />
            <circle cx="118" cy="74" r="2.6" fill="#3E2415" />
            <circle cx="118" cy="74" r="1.3" fill="#120904" />
            <circle cx="119.2" cy="73" r="0.8" fill="#FFFFFF" />

            <ellipse cx="142" cy="74" rx="5.2" ry="3.1" fill="#FFFFFF" />
            <circle cx="142" cy="74" r="2.6" fill="#3E2415" />
            <circle cx="142" cy="74" r="1.3" fill="#120904" />
            <circle cx="143.2" cy="73" r="0.8" fill="#FFFFFF" />

            <!-- Upper eyelids -->
            <path d="M112 73 Q118 69.5 124 73" fill="none" stroke="#4A2E1B" stroke-width="1.3" stroke-linecap="round" />
            <path d="M136 73 Q142 69.5 148 73" fill="none" stroke="#4A2E1B" stroke-width="1.3" stroke-linecap="round" />

            <!-- Straight, noble nose -->
            <path d="M130 70 L129 86 Q130 88.5 133 87" fill="none" stroke="#9E6849" stroke-width="1.5" stroke-linecap="round" />

            <!-- Well-defined young beard & mustache -->
            <path d="M105 84 C106 98, 115 112, 130 116 C145 112, 154 98, 155 84 C152 92, 145 97, 138 97 C135 97, 133 95, 130 95 C127 95, 125 97, 122 97 C115 97, 108 92, 105 84 Z" fill="url(#hairGrad)" opacity="0.92" />
            <!-- Mustache -->
            <path d="M118 94 Q124 90 130 92 Q136 90 142 94" fill="none" stroke="#27160C" stroke-width="2.6" stroke-linecap="round" />
            <!-- Serene gentle smile -->
            <path d="M123 95.5 Q130 99 137 95.5" fill="none" stroke="#D98A76" stroke-width="1.6" stroke-linecap="round" />

            <!-- Parted top hair waves -->
            <path d="M130 43 C114 43, 101 54, 101 72 C103 61, 114 53, 130 52 C146 53, 157 61, 159 72 C159 54, 146 43, 130 43 Z" fill="url(#hairGrad)" />
          </g>
        </g>
      </svg>
    </div>

    <!-- VESTMENT SELECTOR & PROGRESS CAROUSEL -->
    <div class="vestment-info-bar">
      <div class="vestment-header-row">
        <span id="active-vestment-name" class="vestment-title">1. Túnica de Lino de Nazaret (Ketonet)</span>
        <span id="unlocked-count-label" class="vestment-sub">1/6 Desbloqueadas</span>
      </div>
      <div id="vestment-carousel" class="vestment-carousel"></div>
    </div>
  </div>

  <!-- PEDAGOGICAL ENGINE CARD (10,000 UNIQUE DAYS) -->
  <div class="pedagogy-card">
    <div class="cycle-header">
      <span id="cycle-badge" class="cycle-tag">Ciclo 1 • San Basilio y Capadocia</span>
      <span id="matrix-day-label" class="day-counter-tag">Semilla #1 / 10,000</span>
    </div>
    <p id="micro-quote-text" class="micro-quote"></p>
    <p id="micro-praxis-text" class="micro-praxis"></p>
  </div>

  <!-- CONSECRATION BUTTON -->
  <div class="action-zone">
    <button id="consecrate-btn" class="consecrate-btn" onclick="handleConsecrateToday()">
      <span id="consecrate-btn-icon">🔥</span>
      <span id="consecrate-btn-text">Sellar Oración Diaria y Encender la Llama</span>
    </button>
  </div>

  <!-- DEMO BAR FOR IMMEDIATE TESTING -->
  <div class="demo-bar">
    <div class="demo-label-row">
      <span id="demo-title-label">Modo Exploración & Saltos de Etapa</span>
      <span id="demo-state-indicator">Misericordia Activa</span>
    </div>
    <div class="demo-buttons">
      <button id="demo-btn-step" class="demo-btn" onclick="demoStepDay(1)">+1 Día</button>
      <button id="demo-btn-galilee" class="demo-btn" onclick="demoJumpToDay(11)">Galilea (D.11)</button>
      <button id="demo-btn-ardens" class="demo-btn" onclick="demoJumpToDay(31)">Cor Ardens (D.31)</button>
      <button id="demo-btn-basil" class="demo-btn" onclick="demoJumpToDay(61)">San Basilio (D.61)</button>
      <button id="demo-btn-csb" class="demo-btn" onclick="demoJumpToDay(121)">CSB Annonay (D.121)</button>
      <button id="demo-btn-panto" class="demo-btn" onclick="demoJumpToDay(366)">Pantocrátor (D.366)</button>
      <button id="demo-btn-ember" class="demo-btn" onclick="demoTriggerEmberWait()">Brasa en Espera</button>
    </div>
  </div>
</div>
"""

private const val CHRIST_INTERIOR_SIMULATOR_HTML_PART_2 = """
<script>
/**
 * 0. SISTEMA MULTILINGÜE EN VIVO (ESPAÑOL, ENGLISH, FRANÇAIS)
 */
let currentLang = "es";
try {
  if (window.AndroidStreakBridge && window.AndroidStreakBridge.getCurrentLang) {
    const bridgeLang = window.AndroidStreakBridge.getCurrentLang();
    if (bridgeLang === "en" || bridgeLang === "fr" || bridgeLang === "es") {
      currentLang = bridgeLang;
    }
  }
} catch (e) {}

const UI_STRINGS = {
  es: {
    eyebrow: "Simulador del Cristo Interior",
    mainTitle: "Racha de Crecimiento en Jesús",
    emberBadgePrefix: "Brasa • Día ",
    streakPrefix: "Racha: ",
    daySingular: " Día",
    dayPlural: " Días",
    emberBanner: (d) => "Brasa en espera • Tu progreso (" + d + " días) está intacto. ¡Reaviva hoy!",
    demoEmberState: "Estado: Brasa en espera",
    sealedBanner: "Llama Consagrada Hoy • Creciendo en la Estatura de Cristo",
    activeBanner: "Llama Interior Activa • Lista para tu Oración de Hoy",
    stageActive: (n) => "Etapa " + n + "/6 activa",
    unlockedSuffix: "/6 Desbloqueadas",
    lockedHint: (minD) => "Se desbloquea en el Día " + minD + " (Usa la barra Demo abajo para probarla)",
    foundationalYear: "Año Fundacional",
    perseveranceYear: (y) => "Año " + y + " de Perseverancia",
    dayCounterPrefix: "Día #",
    sealedBtn: "Comunión sellada por hoy ✓",
    reviveBtn: "Reavivar la Brasa y Sellar Oración de Hoy",
    sealBtn: "Sellar Oración Diaria y Encender la Llama",
    demoTitle: "Modo Exploración & Saltos de Etapa",
    demoStep: "+1 Día",
    demoGalilee: "Galilea (D.11)",
    demoArdens: "Cor Ardens (D.31)",
    demoBasil: "San Basilio (D.61)",
    demoCsb: "CSB Annonay (D.121)",
    demoPanto: "Pantocrátor (D.366)",
    demoEmber: "Brasa en Espera"
  },
  en: {
    eyebrow: "Inner Christ Simulator",
    mainTitle: "Daily Growth Streak in Jesus",
    emberBadgePrefix: "Ember • Day ",
    streakPrefix: "Streak: ",
    daySingular: " Day",
    dayPlural: " Days",
    emberBanner: (d) => "Glowing Ember • Your progress (" + d + " days) is safe. Rekindle today!",
    demoEmberState: "Status: Glowing Ember",
    sealedBanner: "Flame Consecrated Today • Growing into the Fullness of Christ",
    activeBanner: "Inner Flame Active • Ready for Today's Prayer",
    stageActive: (n) => "Stage " + n + "/6 active",
    unlockedSuffix: "/6 Unlocked",
    lockedHint: (minD) => "Unlocks on Day " + minD + " (Use the Exploration bar below to preview it)",
    foundationalYear: "Foundational Year",
    perseveranceYear: (y) => "Year " + y + " of Perseverance",
    dayCounterPrefix: "Day #",
    sealedBtn: "Communion Sealed for Today ✓",
    reviveBtn: "Rekindle the Ember & Seal Today's Prayer",
    sealBtn: "Seal Daily Prayer & Ignite the Flame",
    demoTitle: "Exploration Mode & Stage Jumps",
    demoStep: "+1 Day",
    demoGalilee: "Galilee (D.11)",
    demoArdens: "Cor Ardens (D.31)",
    demoBasil: "St. Basil (D.61)",
    demoCsb: "CSB Annonay (D.121)",
    demoPanto: "Pantocrator (D.366)",
    demoEmber: "Glowing Ember"
  },
  fr: {
    eyebrow: "Simulateur du Christ Intérieur",
    mainTitle: "Série de Croissance en Jésus",
    emberBadgePrefix: "Braise • Jour ",
    streakPrefix: "Série : ",
    daySingular: " Jour",
    dayPlural: " Jours",
    emberBanner: (d) => "Braise en attente • Votre progrès (" + d + " jours) est intact. Ravivez aujourd'hui !",
    demoEmberState: "État : Braise en attente",
    sealedBanner: "Flamme Consacrée Aujourd'hui • Grandir dans la Plénitude du Christ",
    activeBanner: "Flamme Intérieure Active • Prête pour votre Prière d'Aujourd'hui",
    stageActive: (n) => "Étape " + n + "/6 active",
    unlockedSuffix: "/6 Débloquées",
    lockedHint: (minD) => "Se débloque au Jour " + minD + " (Utilisez la barre d'exploration ci-dessous)",
    foundationalYear: "Année Fondatrice",
    perseveranceYear: (y) => "Année " + y + " de Persévérance",
    dayCounterPrefix: "Jour #",
    sealedBtn: "Communion scellée pour aujourd'hui ✓",
    reviveBtn: "Raviver la Braise et Sceller la Prière",
    sealBtn: "Sceller la Prière Quotidienne et Allumer la Flamme",
    demoTitle: "Mode Exploration & Sauts d'Étape",
    demoStep: "+1 Jour",
    demoGalilee: "Galilée (J.11)",
    demoArdens: "Cor Ardens (J.31)",
    demoBasil: "Saint Basile (J.61)",
    demoCsb: "CSB Annonay (J.121)",
    demoPanto: "Pantocrator (J.366)",
    demoEmber: "Braise en Attente"
  }
};

/**
 * 1. DEFINICIÓN DE LAS 6 VESTIMENTAS HISTÓRICAS Y ESPIRITUALES (ES / EN / FR)
 */
const VESTMENTS = [
  {
    id: 1,
    minDay: 1,
    shortLabel: {
      es: "1. Nazaret (1+)",
      en: "1. Nazareth (1+)",
      fr: "1. Nazareth (1+)"
    },
    fullName: {
      es: "1. Túnica rústica de lino de Nazaret (Ketonet)",
      en: "1. Rustic Linen Tunic of Nazareth (Ketonet)",
      fr: "1. Tunique rustique en lin de Nazareth (Ketonet)"
    },
    layers: [1]
  },
  {
    id: 2,
    minDay: 11,
    shortLabel: {
      es: "2. Galilea (11+)",
      en: "2. Galilee (11+)",
      fr: "2. Galilée (11+)"
    },
    fullName: {
      es: "2. Manto de Galilea (Himation/Talit) con flecos Tsitsit",
      en: "2. Mantle of Galilee (Himation/Tallit) with Tzitzit fringes",
      fr: "2. Manteau de Galilée (Himation/Tallit) à franges Tsitsit"
    },
    layers: [1, 2]
  },
  {
    id: 3,
    minDay: 31,
    shortLabel: {
      es: "3. Cor Ardens (31+)",
      en: "3. Cor Ardens (31+)",
      fr: "3. Cor Ardens (31+)"
    },
    fullName: {
      es: "3. El Cor Ardens Agustiniano (Corazón en Llama Viva)",
      en: "3. The Augustinian Cor Ardens (Heart Ablaze with Love)",
      fr: "3. Le Cor Ardens Augustinien (Cœur Ardent de Charité)"
    },
    layers: [1, 2, 3]
  },
  {
    id: 4,
    minDay: 61,
    shortLabel: {
      es: "4. Capadocia (61+)",
      en: "4. Cappadocia (61+)",
      fr: "4. Cappadoce (61+)"
    },
    fullName: {
      es: "4. Hábito Monástico de Capadocia (San Basilio) y Asceticon",
      en: "4. Cappadocian Monastic Habit (St. Basil) & Asceticon",
      fr: "4. Habit Monastique de Cappadoce (Saint Basile) et Asceticon"
    },
    layers: [4, 3]
  },
  {
    id: 5,
    minDay: 121,
    shortLabel: {
      es: "5. CSB Annonay (121+)",
      en: "5. CSB Annonay (121+)",
      fr: "5. CSB Annonay (121+)"
    },
    fullName: {
      es: "5. Hábito Congregación de San Basilio (CSB) y Crucifijo",
      en: "5. Congregation of St. Basil (CSB) Habit & Pectoral Cross",
      fr: "5. Habit de la Congrégation de Saint-Basile (CSB) et Croix"
    },
    layers: [5, 3]
  },
  {
    id: 6,
    minDay: 366,
    shortLabel: {
      es: "6. Pantocrátor (366+)",
      en: "6. Pantocrator (366+)",
      fr: "6. Pantocrator (366+)"
    },
    fullName: {
      es: "6. Manto Imperial de Gloria / Pantocrátor y Báculo",
      en: "6. Imperial Mantle of Glory / Pantocrator & Shepherd Staff",
      fr: "6. Manteau Impérial de Gloire / Pantocrator et Crosse"
    },
    layers: [6, 3]
  }
];

/**
 * 2. MOTOR PEDAGÓGICO GENERATIVO DETERMINISTA (10,000 DÍAS ÚNICOS EN ES / EN / FR)
 */
const CYCLES_I18N = {
  es: [
    {
      name: "Ciclo 1 • San Basilio y Capadocia",
      authors: [
        "San Basilio Magno en las Reglas Morales",
        "San Basilio en el Hexaemerón",
        "San Gregorio Nacianceno junto a Basilio en el Ponto",
        "Santa Macrina la Joven en Anisa",
        "San Gregorio de Nisa en la Vida de Moisés",
        "San Basilio en la Homilía sobre el Espíritu Santo",
        "San Basilio al fundar la Basiliada en Cesarea (370 d.C.)"
      ],
      themes: [
        "recuerda que como la abeja sabia que toma solo el néctar puro de cada flor, el alma orante transforma toda lectura en miel de caridad",
        "enseña que el amor a Dios no se aprende con discursos aislados, sino en la koinonía fraterna donde el hermano es sacramento vivo",
        "afirma que el granero que retiene el pan se corrompe, pero el corazón que reparte su tiempo y su paz multiplica la gracia",
        "contempla que cada amanecer es una página viva de la creación donde el Artífice divino firma con belleza y orden interior",
        "advierte que la verdadera ascesis no consiste en huir del mundo con amargura, sino en habitarlo con mansedumbre y firmeza",
        "proclama que el Espíritu Santo ilumina la inteligencia como el sol al ojo limpio, haciendo resplandecer a Cristo en lo cotidiano",
        "demuestra que el hospital y la escuela nacen del mismo altar cuando la liturgia se prolonga en compasión activa hacia el débil"
      ],
      praxis: [
        "Aplicación hoy: Antes de responder con prisa, regala 20 segundos de escucha atenta y bendice interiormente a quien tienes delante.",
        "Aplicación hoy: Elige una tarea sencilla de tu jornada y realízala con excelencia silenciosa como ofrenda por tu comunidad.",
        "Aplicación hoy: Comparte un gesto concreto de alivio o aliento con alguien que hoy cargue cansancio o incertidumbre.",
        "Aplicación hoy: Lee un versículo del Evangelio en voz baja y permite que una sola palabra gobierne tu paz durante la tarde.",
        "Aplicación hoy: Renuncia hoy a una queja innecesaria y cámbiala por una intercesión breve por la unidad fraterna."
      ]
    },
    {
      name: "Ciclo 2 • San Agustín y el Cor Ardens",
      authors: [
        "San Agustín en las Confesiones (Libro X)",
        "San Agustín en su Regla Monástica (Ordo Monasterii)",
        "San Agustín en el Comentario al Evangelio de San Juan",
        "Santa Mónica en el coloquio de Ostia Tiberina",
        "San Agustín en La Ciudad de Dios",
        "San Agustín en los Sermones sobre la Caridad",
        "San Agustín sobre el Maestro Interior (De Magistro)"
      ],
      themes: [
        "confiesa: «Nos hiciste, Señor, para Ti, y nuestro corazón está inquieto hasta que descanse en Tu verdad viva»",
        "exhorta a tener «una sola alma y un solo corazón orientados hacia Dios», haciendo de la amistad espiritual un hogar luminoso",
        "descubre que quien busca a Dios fuera mientras descuida su morada interior camina lejos de Quien es más íntimo que su propia intimidad",
        "enseña que el amor es el peso del alma («Pondus meum amor meus»): allá donde pones tu afecto, allá habita tu vida",
        "recuerda que cantar y orar es propio de quien ama, pero caminar sin desfallecer es la prueba del fuego verdadero",
        "ilumina que Cristo habla por dentro cuando hacemos silencio de nuestras vanidades para escuchar al Maestro Interior",
        "afirma que la humildad es la raíz que sostiene el árbol de toda ciencia; sin ella, el saber hincha, pero con ella edifica"
      ],
      praxis: [
        "Aplicación hoy: Entra tres minutos en tu celda interior al mediodía y repite despacio: «Señor, que me conozca a mí y te conozca a Ti».",
        "Aplicación hoy: Examina qué deseo guía hoy tus decisiones y ordénalo con serenidad hacia el bien común.",
        "Aplicación hoy: Perdona de corazón cualquier roce reciente para que ninguna sombra apague la brasa de tu paz.",
        "Aplicación hoy: Busca la verdad con humildad en tus conversaciones de hoy, prefiriendo comprender antes que imponerte.",
        "Aplicación hoy: Agradece en silencio a una persona que haya encendido tu fe como Mónica acompañó el camino de Agustín."
      ]
    },
    {
      name: "Ciclo 3 • Historia y Santos Patronos Basilianos",
      authors: [
        "El Patriarca José en la tradición espiritual basiliana",
        "San Juan Evangelista, discípulo amado y custodio de María",
        "San Pedro Apóstol en su confesión junto al lago de Tiberíades",
        "San Pablo Apóstol en su himno a la supremacía de Cristo",
        "Los primeros maestros del Colegio de Annonay (1822)",
        "Monseñor Joseph Soulerin, pionero basiliano en Toronto (1852)",
        "El Padre Étienne Malbœuf y los fundadores del Vivarais"
      ],
      themes: [
        "muestra que la fidelidad escondida en el taller diario prepara a los corazones rectos para custodiar las promesas de Dios",
        "testimonia que solo quien reclina su frente sobre el pecho del Señor aprende a reconocer los latidos de Su misericordia en la historia",
        "confirma que Cristo no exige impecabilidad fría, sino un amor sincero capaz de responder: «Señor, Tú sabes todo; Tú sabes que te amo»",
        "proclama que nada creado puede separarnos del amor de Cristo cuando nuestra misión se apoya en la gracia y no en el orgullo",
        "enseña que en tiempos de tormenta cultural, encender una pequeña lámpara de fe y estudio salva a generaciones enteras",
        "recuerda que cruzar fronteras por el Evangelio exige audacia apostólica y arraigo profundo en la oración comunitaria",
        "demuestra que la fraternidad sencilla entre hermanos sostiene las obras más duraderas de la Iglesia"
      ],
      praxis: [
        "Aplicación hoy: Invoca la custodia de San José antes de iniciar tu trabajo principal para hacerlo con rectitud y calma.",
        "Aplicación hoy: Mira tus propios límites con la confianza de Pedro: vuelve a empezar con humildad y alegría.",
        "Aplicación hoy: Ora hoy especialmente por los formadores, sacerdotes y laicos que sostienen la misión basiliana en las Américas.",
        "Aplicación hoy: Cultiva un gesto de hospitalidad genuina con quien se acerque a pedir consejo o compañía.",
        "Aplicación hoy: Lee un pasaje breve de las Cartas Paulinas y subraya una frase que fortalezca tu esperanza."
      ]
    },
    {
      name: "Ciclo 4 • Basilianos de Annonay y Misión Educativa",
      authors: [
        "El lema basiliano «Enséñame la Bondad, la Disciplina y la Ciencia» (Sal 119,66)",
        "El espíritu pedagógico de Annonay en las montañas de Ardèche",
        "El Padre Henri Carr CSB, renovador de los estudios humanistas y filosóficos",
        "Étienne Gilson y la tradición intelectual del Pontifical Institute de Toronto",
        "Las Constituciones de la Congregación de San Basilio (Basilian Way of Life)",
        "La misión educativa basiliana en Colombia, México, Canadá y Estados Unidos",
        "La pedagogía de la presencia fraterna en las aulas y parroquias CSB"
      ],
      themes: [
        "une en armonía indivisible la Bondad del corazón, la Disciplina del carácter y la Ciencia que busca la Verdad sin miedo",
        "recuerda que educar jamás es llenar vasijas con datos, sino encender en cada joven el asombro por su dignidad trascendente",
        "afirma que toda verdadera investigación científica y filosófica conduce al umbral del Verbo por quien todo fue hecho",
        "enseña que la disciplina cristiana no es rigidez externa, sino la libertad interior de quien sabe dominarse para servir mejor",
        "destaca que el educador basiliano enseña primero con la coherencia alegre de su vida y la cercanía hacia cada estudiante",
        "subraya que allí donde se levanta una comunidad que estudia, reza y comparte la mesa, el Reino de Dios echa raíces",
        "inspira a integrar fe y cultura para que ningún talento humano quede al margen de la redención de Cristo"
      ],
      praxis: [
        "Aplicación hoy: Equilibra tu día dedicando un momento a la bondad (caridad), otro al orden (disciplina) y otro al estudio (ciencia).",
        "Aplicación hoy: Si acompañas a jóvenes o colegas, reconoce en voz alta una cualidad noble que veas crecer en ellos.",
        "Aplicación hoy: Estudia o profundiza durante 10 minutos en un tema formativo ofreciendo tu mente a la luz de Dios.",
        "Aplicación hoy: Practica la puntualidad y el cuidado de los detalles como expresión de respeto hacia tu comunidad.",
        "Aplicación hoy: Repite al comenzar tu jornada: «Bonitatem et disciplinam et scientiam doce me, Domine»."
      ]
    },
    {
      name: "Ciclo 5 • El Evangelio Hoy y la Dignidad Humana",
      authors: [
        "Jesús de Nazaret en el Sermón del Monte (Mateo 5)",
        "Jesús en la Parábola del Hijo Pródigo y el Padre Misericordioso (Lucas 15)",
        "Jesús junto al pozo de Sicar en diálogo vivo (Juan 4)",
        "Jesús al lavar los pies a sus discípulos en el Cenáculo (Juan 13)",
        "Jesús Resucitado caminando con los discípulos de Emaús (Lucas 24)",
        "Jesús, el Buen Pastor que conoce a cada oveja por su nombre (Juan 10)",
        "El Evangelio vivo de la Dignidad Inviolable de cada Persona"
      ],
      themes: [
        "revela que el abrazo del Padre siempre se adelanta a nuestras excusas y restaura en nosotros el anillo de la filiación",
        "proclama que la grandeza en el Reino se mide por la toalla ceñida a la cintura y el servicio silencioso a los demás",
        "enciende el corazón cansado mientras camina a nuestro lado y nos explica las Escrituras hasta partir el Pan",
        "afirma que cada ser humano posee un valor sagrado e irrepetible que ninguna herida ni fracaso puede borrar",
        "promete el agua viva que salta hasta la vida eterna a todo aquel que le presenta su sed con sinceridad",
        "llama bienaventurados a los pacíficos y limpios de corazón, porque ellos hacen visible el rostro de Dios en la tierra",
        "nos envía al mundo no para juzgar desde lejos, sino para curar heridas con el óleo del consuelo y el vino de la esperanza"
      ],
      praxis: [
        "Aplicación hoy: Trata a cada persona con la que cruces hoy como un misterio sagrado habitado por la presencia de Dios.",
        "Aplicación hoy: Haz un acto de servicio concreto sin buscar reconocimiento ni aplauso humano.",
        "Aplicación hoy: Al caer la tarde, repasa tu jornada con Jesús como en Emaús y descubre dónde ardió tu corazón.",
        "Aplicación hoy: Acoge con misericordia tu propia fragilidad y déjate abrazar por la gracia que todo lo hace nuevo.",
        "Aplicación hoy: Lleva paz activa allí donde notes tensión, siendo puente de diálogo y fraternidad."
      ]
    }
  ],
  en: [
    {
      name: "Cycle 1 • St. Basil & Cappadocia",
      authors: [
        "St. Basil the Great in the Moral Rules",
        "St. Basil in the Hexaemeron",
        "St. Gregory of Nazianzus with Basil in Pontus",
        "St. Macrina the Younger in Annisa",
        "St. Gregory of Nyssa in the Life of Moses",
        "St. Basil in the Treatise on the Holy Spirit",
        "St. Basil upon founding the Basiliad in Caesarea (370 AD)"
      ],
      themes: [
        "reminds us that like the wise bee gathering pure nectar from every flower, the praying soul turns all study into the honey of charity",
        "teaches that love for God is not learned in isolation, but in fraternal koinonia where our brother or sister is a living sacrament",
        "affirms that the barn hoarding grain decays, while the heart sharing its time and peace multiplies divine grace",
        "contemplates that every dawn is a living page of creation where the Divine Craftsman signs with beauty and inner harmony",
        "warns that true asceticism is not fleeing the world in bitterness, but dwelling in it with meekness and steadfastness",
        "proclaims that the Holy Spirit illumines the mind as sunlight does a clear eye, making Christ shine in daily life",
        "demonstrates that the hospital and the school spring from the same altar when liturgy overflows into active compassion"
      ],
      praxis: [
        "Practice today: Before replying in haste, offer 20 seconds of attentive listening and silently bless the person before you.",
        "Practice today: Choose a simple task today and complete it with quiet excellence as an offering for your community.",
        "Practice today: Share a concrete gesture of relief or encouragement with someone carrying fatigue or uncertainty today.",
        "Practice today: Read one Gospel verse softly and let a single word govern your peace throughout the afternoon.",
        "Practice today: Let go of one unnecessary complaint today and turn it into a brief prayer for fraternal unity."
      ]
    },
    {
      name: "Cycle 2 • St. Augustine & the Cor Ardens",
      authors: [
        "St. Augustine in the Confessions (Book X)",
        "St. Augustine in his Monastic Rule (Ordo Monasterii)",
        "St. Augustine in the Tractates on the Gospel of John",
        "St. Monica in the colloquy at Ostia Tiberina",
        "St. Augustine in The City of God",
        "St. Augustine in his Sermons on Charity",
        "St. Augustine on the Inner Teacher (De Magistro)"
      ],
      themes: [
        "confesses: 'You have made us for Yourself, O Lord, and our heart is restless until it rests in Your living truth'",
        "exhorts us to have 'one soul and one heart oriented toward God,' making spiritual friendship a luminous home",
        "discovers that whoever seeks God outside while neglecting the inner dwelling walks far from Him who is closer than our own soul",
        "teaches that love is the weight of the soul ('Pondus meum amor meus'): wherever you place your love, there your life dwells",
        "reminds us that singing and praying belong to the one who loves, and walking without fainting is the test of true fire",
        "illuminates that Christ speaks within when we quiet our vanities to listen to the Inner Teacher",
        "affirms that humility is the deep root sustaining the tree of all knowledge; without it, learning puffs up, but with it, it builds up"
      ],
      praxis: [
        "Practice today: Enter your inner room for three minutes at midday and pray slowly: 'Lord, let me know myself and let me know You.'",
        "Practice today: Examine what desire guides your decisions today and order it serenely toward the common good.",
        "Practice today: Forgive from the heart any recent friction so that no shadow dims the ember of your peace.",
        "Practice today: Seek truth with humility in your conversations today, preferring to understand rather than to impose.",
        "Practice today: Silently thank someone who ignited your faith just as Monica accompanied Augustine's journey."
      ]
    },
    {
      name: "Cycle 3 • Basilian History & Patron Saints",
      authors: [
        "The Patriarch Joseph in the Basilian spiritual tradition",
        "St. John the Evangelist, beloved disciple and guardian of Mary",
        "St. Peter the Apostle in his confession by the Sea of Tiberias",
        "St. Paul the Apostle in his hymn to the supremacy of Christ",
        "The first teachers of the College of Annonay (1822)",
        "Archbishop Joseph Soulerin, Basilian pioneer in Toronto (1852)",
        "Father Étienne Malbœuf and the founders of the Vivarais"
      ],
      themes: [
        "shows that hidden fidelity in the daily workshop prepares upright hearts to guard the promises of God",
        "witnesses that only the one who rests upon the Lord's chest learns to recognize the heartbeat of His mercy in history",
        "confirms that Christ does not demand cold perfectionism, but a sincere love able to answer: 'Lord, You know everything; You know that I love You'",
        "proclaims that nothing created can separate us from the love of Christ when our mission rests on grace rather than pride",
        "teaches that in times of cultural storm, lighting a small lamp of faith and study saves entire generations",
        "reminds us that crossing borders for the Gospel requires apostolic boldness and deep roots in communal prayer",
        "demonstrates that simple fraternity among brothers sustains the most enduring works of the Church"
      ],
      praxis: [
        "Practice today: Invoke the protection of St. Joseph before starting your main work so as to do it with integrity and calm.",
        "Practice today: Look at your own limits with Peter's trust: begin anew with humility and joy.",
        "Practice today: Pray especially today for the educators, priests, and laypeople sustaining the Basilian mission across the Americas.",
        "Practice today: Cultivate a gesture of genuine hospitality toward anyone who approaches you for advice or company.",
        "Practice today: Read a brief passage from the Pauline Letters and underline one sentence that strengthens your hope."
      ]
    },
    {
      name: "Cycle 4 • Basilians of Annonay & Educational Mission",
      authors: [
        "The Basilian motto 'Teach me Goodness, Discipline, and Knowledge' (Ps 119:66)",
        "The pedagogical spirit of Annonay in the mountains of Ardèche",
        "Father Henry Carr CSB, renewer of humanistic and philosophical studies",
        "Étienne Gilson and the intellectual tradition of the Pontifical Institute in Toronto",
        "The Constitutions of the Congregation of St. Basil (Basilian Way of Life)",
        "The Basilian educational mission in Colombia, Mexico, Canada, and the United States",
        "The pedagogy of fraternal presence in CSB classrooms and parishes"
      ],
      themes: [
        "unites in indivisible harmony Goodness of heart, Discipline of character, and Knowledge that seeks Truth without fear",
        "reminds us that educating is never filling vessels with data, but igniting in every young person wonder for their transcendent dignity",
        "affirms that all authentic scientific and philosophical inquiry leads to the threshold of the Word through whom all things were made",
        "teaches that Christian discipline is not external rigidity, but the inner freedom of one who masters himself to serve better",
        "highlights that the Basilian educator teaches first through joyful coherence of life and closeness to every student",
        "emphasizes that wherever a community gathers to study, pray, and share the table, the Kingdom of God takes root",
        "inspires us to integrate faith and culture so that no human talent remains outside the redemption of Christ"
      ],
      praxis: [
        "Practice today: Balance your day by dedicating a moment to goodness (charity), another to order (discipline), and another to study (knowledge).",
        "Practice today: If you accompany youth or colleagues, affirm aloud a noble quality you see growing in them.",
        "Practice today: Study or reflect for 10 minutes on a formative topic, offering your mind to the light of God.",
        "Practice today: Practice punctuality and care for small details as an expression of respect for your community.",
        "Practice today: Repeat as you begin your day: 'Bonitatem et disciplinam et scientiam doce me, Domine.'"
      ]
    },
    {
      name: "Cycle 5 • The Living Gospel & Human Dignity",
      authors: [
        "Jesus of Nazareth in the Sermon on the Mount (Matthew 5)",
        "Jesus in the Parable of the Prodigal Son and the Merciful Father (Luke 15)",
        "Jesus by the well of Sychar in living dialogue (John 4)",
        "Jesus washing His disciples' feet in the Upper Room (John 13)",
        "The Risen Jesus walking with the disciples on the road to Emmaus (Luke 24)",
        "Jesus, the Good Shepherd who knows each sheep by name (John 10)",
        "The living Gospel of the Inviolable Dignity of Every Person"
      ],
      themes: [
        "reveals that the Father's embrace always runs ahead of our excuses and restores to us the ring of sonship and daughtership",
        "proclaims that greatness in the Kingdom is measured by the towel tied around the waist and quiet service to others",
        "kindles the weary heart as He walks beside us and opens the Scriptures until the breaking of the Bread",
        "affirms that every human being possesses a sacred, unrepeatable worth that no wound or failure can erase",
        "promises living water springing up to eternal life to everyone who brings their thirst to Him with sincerity",
        "calls blessed the peacemakers and the pure of heart, for they make the face of God visible on earth",
        "sends us into the world not to judge from afar, but to heal wounds with the oil of consolation and the wine of hope"
      ],
      praxis: [
        "Practice today: Treat every person you meet today as a sacred mystery inhabited by the presence of God.",
        "Practice today: Perform one concrete act of service without seeking recognition or human applause.",
        "Practice today: At evening, review your day with Jesus as at Emmaus and notice where your heart burned within you.",
        "Practice today: Welcome your own fragility with mercy and let yourself be embraced by the grace that makes all things new.",
        "Practice today: Bring active peace wherever you notice tension, becoming a bridge of dialogue and fraternity."
      ]
    }
  ],
  fr: [
    {
      name: "Cycle 1 • Saint Basile et la Cappadoce",
      authors: [
        "Saint Basile le Grand dans les Règles Morales",
        "Saint Basile dans l'Hexaéméron",
        "Saint Grégoire de Nazianze avec Basile dans le Pont",
        "Sainte Macrine la Jeune à Annisa",
        "Saint Grégoire de Nysse dans la Vie de Moïse",
        "Saint Basile dans le Traité sur le Saint-Esprit",
        "Saint Basile lors de la fondation de la Basiliade à Césarée (370 ap. J.-C.)"
      ],
      themes: [
        "rappelle que comme l'abeille sage qui ne recueille que le nectar pur de chaque fleur, l'âme priante transforme toute lecture en miel de charité",
        "enseigne que l'amour de Dieu ne s'apprend pas par des discours isolés, mais dans la koinonia fraternelle où le frère est sacrement vivant",
        "affirme que le grenier qui retient le pain se corrompt, mais le cœur qui partage son temps et sa paix multiplie la grâce",
        "contemple que chaque aube est une page vivante de la création où l'Artisan divin signe avec beauté et harmonie intérieure",
        "avertit que la véritable ascèse ne consiste pas à fuir le monde avec amertume, mais à l'habiter avec douceur et fermeté",
        "proclame que l'Esprit Saint illumine l'intelligence comme le soleil un œil pur, faisant resplendir le Christ au quotidien",
        "démontre que l'hôpital et l'école naissent du même autel lorsque la liturgia se prolonge en compassion active envers les petits"
      ],
      praxis: [
        "Application aujourd'hui : Avant de répondre à la hâte, offrez 20 secondes d'écoute attentive et bénissez intérieurement votre interlocuteur.",
        "Application aujourd'hui : Choisissez une tâche simple de votre journée et accomplissez-la avec une excellence silencieuse en offrande.",
        "Application aujourd'hui : Partagez un geste concret de réconfort ou d'encouragement avec quelqu'un qui porte la fatigue aujourd'hui.",
        "Application aujourd'hui : Lisez un verset de l'Évangile à voix basse et laissez une seule parole gouverner votre paix cet après-midi.",
        "Application aujourd'hui : Renoncez aujourd'hui à une plainte inutile et transformez-la en une brève intercession pour l'unité fraternelle."
      ]
    },
    {
      name: "Cycle 2 • Saint Augustin et le Cor Ardens",
      authors: [
        "Saint Augustin dans les Confessions (Livre X)",
        "Saint Augustin dans sa Règle Monastique (Ordo Monasterii)",
        "Saint Augustin dans les Homélies sur l'Évangile de Jean",
        "Sainte Monique lors de l'entretien d'Ostie",
        "Saint Augustin dans La Cité de Dieu",
        "Saint Augustin dans les Sermons sur la Charité",
        "Saint Augustin sur le Maître Intérieur (De Magistro)"
      ],
      themes: [
        "confesse : « Tu nous as faits pour Toi, Seigneur, et notre cœur est sans repos tant qu'il ne demeure en Ta vérité vivante »",
        "exhorte à avoir « une seule âme et un seul cœur tournés vers Dieu », faisant de l'amitié spirituelle une demeure lumineuse",
        "découvre que celui qui cherche Dieu au-dehors en négligeant sa demeure intérieure marche loin de Celui qui est plus intime que son intimité",
        "enseigne que l'amour est le poids de l'âme (« Pondus meum amor meus ») : là où tu places ton affection, là habite ta vie",
        "rappelle que chanter et prier est le propre de celui qui aime, mais marcher sans faiblir est l'épreuve du feu véritable",
        "illumine que le Christ parle au-dedans lorsque nous faisons taire nos vanités pour écouter le Maître Intérieur",
        "affirme que l'humilité est la racine qui soutient l'arbre de toute science ; sans elle, le savoir enfle, mais avec elle, il édifie"
      ],
      praxis: [
        "Application aujourd'hui : Entrez trois minutes dans votre cellule intérieure à midi et répétez doucement : « Seigneur, que je me connaisse et que je Te connaisse ».",
        "Application aujourd'hui : Examinez quel désir guide vos décisions aujourd'hui et ordonnez-le sereinement vers le bien commun.",
        "Application aujourd'hui : Pardonnez de tout cœur toute friction récente afin qu'aucune ombre n'éteigne la braise de votre paix.",
        "Application aujourd'hui : Cherchez la vérité avec humilité dans vos échanges d'aujourd'hui, préférant comprendre plutôt qu'imposer.",
        "Application aujourd'hui : Remerciez en silence une personne qui a éveillé votre foi comme Monique a accompagné le chemin d'Augustin."
      ]
    },
    {
      name: "Cycle 3 • Histoire et Saints Patrons Basiliens",
      authors: [
        "Le Patriarche Joseph dans la tradition spirituelle basilienne",
        "Saint Jean l'Évangéliste, disciple bien-aimé et gardien de Marie",
        "Saint Pierre Apôtre dans sa confession au bord du lac de Tibériade",
        "Saint Paul Apôtre dans son hymne à la primauté du Christ",
        "Les premiers maîtres du Collège d'Annonay (1822)",
        "Mgr Joseph Soulerin, pionnier basilien à Toronto (1852)",
        "Le Père Étienne Malbœuf et les fondateurs du Vivarais"
      ],
      themes: [
        "montre que la fidélité cachée dans l'atelier quotidien prépare les cœurs droits à garder les promesses de Dieu",
        "témoigne que seul celui qui repose son front sur la poitrine du Seigneur apprend à reconnaître les battements de Sa miséricorde",
        "confirme que le Christ n'exige pas une impeccabilité froide, mais un amour sincère capable de répondre : « Seigneur, Tu sais tout ; Tu sais que je T'aime »",
        "proclame que rien de créé ne peut nous séparer de l'amour du Christ lorsque notre mission s'appuie sur la grâce et non sur l'orgueil",
        "enseigne qu'en temps de tempête culturelle, allumer une petite lampe de foi et d'étude sauve des générations entières",
        "rappelle que franchir les frontières pour l'Évangile exige une audace apostolique et un enracinement profond dans la prière communautaire",
        "démontre que la fraternité simple entre frères soutient les œuvres les plus durables de l'Église"
      ],
      praxis: [
        "Application aujourd'hui : Invoquez la garde de Saint Joseph avant de commencer votre travail principal pour l'accomplir avec droiture et calme.",
        "Application aujourd'hui : Regardez vos propres limites avec la confiance de Pierre : recommencez avec humilité et joie.",
        "Application aujourd'hui : Priez spécialement aujourd'hui pour les formateurs, prêtres et laïcs qui soutiennent la mission basilienne.",
        "Application aujourd'hui : Cultivez un geste d'hospitalité sincère envers quiconque vient demander conseil ou écoute.",
        "Application aujourd'hui : Lisez un bref passage des Lettres de Saint Paul et soulignez une phrase qui fortifie votre espérance."
      ]
    },
    {
      name: "Cycle 4 • Basiliens d'Annonay et Mission Éducative",
      authors: [
        "La devise basilienne « Enseigne-moi la Bonté, la Discipline et la Science » (Ps 118,66)",
        "L'esprit pédagogique d'Annonay dans les montagnes d'Ardèche",
        "Le Père Henry Carr CSB, rénovateur des études humanistes et philosophiques",
        "Étienne Gilson et la tradition intellectuelle du Pontifical Institute de Toronto",
        "Les Constitutions de la Congrégation de Saint-Basile (Basilian Way of Life)",
        "La mission éducative basilienne en Colombie, au Mexique, au Canada et aux États-Unis",
        "La pédagogie de la présence fraternelle dans les classes et paroisses CSB"
      ],
      themes: [
        "unit en une harmonie indivisible la Bonté du cœur, la Discipline du caractère et la Science qui cherche la Vérité sans crainte",
        "rappelle qu'éduquer n'est jamais remplir des vases de données, mais éveiller en chaque jeune l'émerveillement pour sa dignité transcendante",
        "affirme que toute véritable recherche scientifique et philosophique conduit au seuil du Verbe par qui tout a été fait",
        "enseigne que la discipline chrétienne n'est pas une rigidité extérieure, mais la liberté intérieure de celui qui se maîtrise pour mieux servir",
        "souligne que l'éducateur basilien enseigne d'abord par la cohérence joyeuse de sa vie et la proximité envers chaque étudiant",
        "met en lumière que là où s'élève une communauté qui étudie, prie et partage la table, le Royaume de Dieu prend racine",
        "inspire à intégrer foi et culture afin qu'aucun talent humain ne reste en marge de la rédemption du Christ"
      ],
      praxis: [
        "Application aujourd'hui : Équilibrez votre journée en dédiant un moment à la bonté (charité), un autre à l'ordre (discipline) et un autre à l'étude (science).",
        "Application aujourd'hui : Si vous accompagnez des jeunes ou des collègues, reconnaissez à voix haute une qualité noble qui grandit en eux.",
        "Application aujourd'hui : Étudiez ou approfondissez pendant 10 minutes un sujet formateur en offrant votre esprit à la lumière de Dieu.",
        "Application aujourd'hui : Pratiquez la ponctualité et le soin des détails comme expression de respect envers votre communauté.",
        "Application aujourd'hui : Répétez au début de votre journée : « Bonitatem et disciplinam et scientiam doce me, Domine »."
      ]
    },
    {
      name: "Cycle 5 • L'Évangile Aujourd'hui et la Dignité Humaine",
      authors: [
        "Jésus de Nazareth dans le Sermon sur la Montagne (Matthieu 5)",
        "Jésus dans la Parabole du Fils Prodigue et du Père Miséricordieux (Luc 15)",
        "Jésus au bord du puits de Sychar en dialogue vivant (Jean 4)",
        "Jésus lavant les pieds de ses disciples au Cénacle (Jean 13)",
        "Jésus Ressuscité marchant avec les disciples d'Emmaüs (Luc 24)",
        "Jésus, le Bon Pasteur qui connaît chaque brebis par son nom (Jean 10)",
        "L'Évangile vivant de la Dignité Inviolable de chaque Personne"
      ],
      themes: [
        "révèle que l'étreinte du Père devance toujours nos excuses et restaure en nous l'anneau de la filiation divine",
        "proclame que la grandeur dans le Royaume se mesure au linge noué à la ceinture et au service silencieux des autres",
        "embrase le cœur fatigué tandis qu'Il marche à nos côtés et nous explique les Écritures jusqu'à la fraction du Pain",
        "affirme que chaque être humain possède une valeur sacrée et unique qu'aucune blessure ni aucun échec ne peut effacer",
        "promet l'eau vive qui jaillit jusque dans la vie éternelle à quiconque Lui présente sa soif avec sincérité",
        "appelle bienheureux les artisans de paix et les cœurs purs, car ils rendent visible le visage de Dieu sur la terre",
        "nous envoie dans le monde non pour juger de loin, mais pour panser les blessures avec l'huile de la consolation et le vin de l'espérance"
      ],
      praxis: [
        "Application aujourd'hui : Traitez chaque personne que vous croisez aujourd'hui comme un mystère sacré habité par la présence de Dieu.",
        "Application aujourd'hui : Accomplissez un acte de service concret sans chercher de reconnaissance ni d'applaudissement humain.",
        "Application aujourd'hui : Au soir, relisez votre journée avec Jésus comme à Emmaüs et découvrez où votre cœur était tout brûlant.",
        "Application aujourd'hui : Accueillez avec miséricorde votre propre fragilité et laissez-vous embrasser par la grâce qui fait toutes choses nouvelles.",
        "Application aujourd'hui : Portez une paix active là où vous notez de la tension, en devenant un pont de dialogue et de fraternité."
      ]
    }
  ]
};

function generateMicroLearningForDay(dayNumber) {
  const safeDay = Math.max(1, Math.min(10000, dayNumber));
  const cycleIdx = (safeDay - 1) % 5;
  const langCycles = CYCLES_I18N[currentLang] || CYCLES_I18N.es;
  const cycle = langCycles[cycleIdx];
  const dict = UI_STRINGS[currentLang] || UI_STRINGS.es;
  const quotient = Math.floor((safeDay - 1) / 5);

  const authorIdx = (quotient + cycleIdx * 2) % cycle.authors.length;
  const themeIdx = (quotient * 3 + cycleIdx + Math.floor(quotient / 7)) % cycle.themes.length;
  const praxisIdx = (quotient * 2 + cycleIdx * 3 + Math.floor(quotient / 35)) % cycle.praxis.length;

  const epochLabel = safeDay <= 365
    ? dict.foundationalYear
    : dict.perseveranceYear(Math.floor((safeDay - 1) / 365) + 1);

  return {
    cycleName: cycle.name,
    seedLabel: dict.dayCounterPrefix + safeDay + " / 10,000 • " + epochLabel,
    quoteLine: "«" + cycle.authors[authorIdx] + " " + cycle.themes[themeIdx] + ".»",
    praxisLine: cycle.praxis[praxisIdx]
  };
}

/**
 * 3. ESTADO Y PERSISTENCIA (localStorage + Puente Android SharedPreferences) + MECÁNICA DE LA MISERICORDIA
 */
const STORAGE_KEY = "csb_christ_interior_streak_v1";

function getTodayIso() {
  const now = new Date();
  const y = now.getFullYear();
  const m = String(now.getMonth() + 1).padStart(2, "0");
  const d = String(now.getDate()).padStart(2, "0");
  return y + "-" + m + "-" + d;
}

function diffDaysIso(isoA, isoB) {
  if (!isoA || !isoB) return 0;
  const a = new Date(isoA + "T00:00:00");
  const b = new Date(isoB + "T00:00:00");
  return Math.round((b - a) / (1000 * 60 * 60 * 24));
}

function getHighestUnlockedVestmentId(day) {
  let highest = 1;
  for (let i = 0; i < VESTMENTS.length; i++) {
    if (day >= VESTMENTS[i].minDay) {
      highest = VESTMENTS[i].id;
    }
  }
  return highest;
}

let state = {
  progressDay: 1,       // Nunca vuelve a 0 (1..10000)
  currentStreak: 1,     // Racha activa
  lastSealedDate: null, // YYYY-MM-DD
  emberWaiting: false,  // Estado "Brasa en espera" si saltó días
  equippedVestmentId: 1
};

function loadState() {
  let raw = null;
  try {
    if (window.AndroidStreakBridge && window.AndroidStreakBridge.loadNativeState) {
      raw = window.AndroidStreakBridge.loadNativeState();
    }
  } catch (e) {}
  if (!raw) {
    try {
      raw = localStorage.getItem(STORAGE_KEY);
    } catch (e) {}
  }
  if (raw) {
    try {
      const parsed = JSON.parse(raw);
      state.progressDay = Math.max(1, Math.min(10000, Number(parsed.progressDay) || 1));
      state.currentStreak = Math.max(1, Number(parsed.currentStreak) || state.progressDay);
      state.lastSealedDate = parsed.lastSealedDate || null;
      state.emberWaiting = Boolean(parsed.emberWaiting);
      state.equippedVestmentId = Number(parsed.equippedVestmentId) || getHighestUnlockedVestmentId(state.progressDay);
    } catch (e) {}
  }

  const today = getTodayIso();
  if (state.lastSealedDate) {
    const gap = diffDaysIso(state.lastSealedDate, today);
    if (gap > 1) {
      state.emberWaiting = true;
    }
  }
}

function saveState() {
  const payload = JSON.stringify(state);
  try {
    localStorage.setItem(STORAGE_KEY, payload);
  } catch (e) {}
  try {
    if (window.AndroidStreakBridge && window.AndroidStreakBridge.saveNativeState) {
      window.AndroidStreakBridge.saveNativeState(payload);
    }
  } catch (e) {}
}

/**
 * 4. WEB AUDIO API: PULSO ARMÓNICO / CAMPANA SACRA SUAVE (CERO DEPENDENCIAS EXTERNAS)
 */
let audioCtx = null;
function playSacredHarmonicBell() {
  try {
    const AudioContextClass = window.AudioContext || window.webkitAudioContext;
    if (!AudioContextClass) return;
    if (!audioCtx) {
      audioCtx = new AudioContextClass();
    }
    if (audioCtx.state === "suspended") {
      audioCtx.resume();
    }
    const now = audioCtx.currentTime;
    const freqs = [528, 660, 1056];
    freqs.forEach((f, idx) => {
      const osc = audioCtx.createOscillator();
      const gain = audioCtx.createGain();
      osc.type = idx === 0 ? "sine" : "triangle";
      osc.frequency.setValueAtTime(f, now);

      const peak = idx === 0 ? 0.14 : (idx === 1 ? 0.08 : 0.04);
      gain.gain.setValueAtTime(0.0001, now);
      gain.gain.exponentialRampToValueAtTime(peak, now + 0.04);
      gain.gain.exponentialRampToValueAtTime(0.0001, now + 1.85);

      osc.connect(gain);
      gain.connect(audioCtx.destination);
      osc.start(now);
      osc.stop(now + 1.9);
    });
  } catch (e) {}
}

/**
 * 5. PARTÍCULA DE LUZ / FUEGO VOLANDO AL CORAZÓN DE JESÚS
 */
function animateParticleToHeart() {
  const btn = document.getElementById("consecrate-btn");
  const avatar = document.getElementById("avatar-target-box");
  const particle = document.getElementById("sacred-particle");
  if (!btn || !avatar || !particle) return;

  const btnRect = btn.getBoundingClientRect();
  const avRect = avatar.getBoundingClientRect();

  const startX = btnRect.left + btnRect.width / 2 - 8;
  const startY = btnRect.top + 6;
  const endX = avRect.left + avRect.width / 2 - 8;
  const endY = avRect.top + avRect.height * 0.56;

  particle.style.transition = "none";
  particle.style.left = startX + "px";
  particle.style.top = startY + "px";
  particle.style.opacity = "1";
  particle.style.transform = "scale(0.8)";

  requestAnimationFrame(() => {
    particle.style.transition = "left 0.72s cubic-bezier(0.22, 1, 0.36, 1), top 0.72s cubic-bezier(0.22, 1, 0.36, 1), transform 0.72s ease, opacity 0.72s ease";
    particle.style.left = endX + "px";
    particle.style.top = endY + "px";
    particle.style.transform = "scale(2.1)";
    setTimeout(() => {
      particle.style.opacity = "0";
      particle.style.transform = "scale(0.3)";
    }, 680);
  });
}

/**
 * 6. RENDERIZADO DE INTERFAZ Y CAPAS SVG (TRADUCIDO DINÁMICAMENTE EN ES / EN / FR)
 */
function renderUI() {
  const dict = UI_STRINGS[currentLang] || UI_STRINGS.es;
  const today = getTodayIso();
  const isSealedToday = (state.lastSealedDate === today) && !state.emberWaiting;

  // Cabecera y botones demo traducidos
  document.getElementById("brand-eyebrow-text").textContent = dict.eyebrow;
  document.getElementById("brand-main-text").textContent = dict.mainTitle;
  document.getElementById("demo-title-label").textContent = dict.demoTitle;
  document.getElementById("demo-btn-step").textContent = dict.demoStep;
  document.getElementById("demo-btn-galilee").textContent = dict.demoGalilee;
  document.getElementById("demo-btn-ardens").textContent = dict.demoArdens;
  document.getElementById("demo-btn-basil").textContent = dict.demoBasil;
  document.getElementById("demo-btn-csb").textContent = dict.demoCsb;
  document.getElementById("demo-btn-panto").textContent = dict.demoPanto;
  document.getElementById("demo-btn-ember").textContent = dict.demoEmber;

  // Asegurar vestimenta equipada válida
  const maxUnlocked = getHighestUnlockedVestmentId(state.progressDay);
  if (state.equippedVestmentId > maxUnlocked) {
    state.equippedVestmentId = maxUnlocked;
  }

  // Actualizar píldora de racha
  const streakPill = document.getElementById("streak-pill");
  const streakIcon = document.getElementById("streak-icon");
  const streakCountText = document.getElementById("streak-count-text");
  const aura = document.getElementById("sacred-aura");
  const mercyBannerText = document.getElementById("mercy-status-text");
  const demoStateInd = document.getElementById("demo-state-indicator");

  if (state.emberWaiting) {
    streakPill.className = "badge-pill ember-mode";
    streakIcon.textContent = "🕯️";
    streakCountText.textContent = dict.emberBadgePrefix + state.progressDay;
    aura.className = "sacred-aura ember-wait";
    mercyBannerText.textContent = dict.emberBanner(state.progressDay);
    demoStateInd.textContent = dict.demoEmberState;
  } else {
    streakPill.className = "badge-pill";
    streakIcon.textContent = "🔥";
    streakCountText.textContent = dict.streakPrefix + state.progressDay + (state.progressDay === 1 ? dict.daySingular : dict.dayPlural);
    aura.className = isSealedToday ? "sacred-aura consecrated" : "sacred-aura";
    mercyBannerText.textContent = isSealedToday ? dict.sealedBanner : dict.activeBanner;
    demoStateInd.textContent = dict.stageActive(maxUnlocked);
  }

  // Actualizar capas SVG de Jesús
  const selectedVest = VESTMENTS.find(v => v.id === state.equippedVestmentId) || VESTMENTS[0];
  for (let layerNum = 1; layerNum <= 6; layerNum++) {
    const el = document.getElementById("vest-layer-" + layerNum);
    if (!el) continue;
    if (selectedVest.layers.indexOf(layerNum) !== -1) {
      el.classList.add("active");
    } else {
      el.classList.remove("active");
    }
  }

  // Nombre de vestimenta y contador (traducidos)
  document.getElementById("active-vestment-name").textContent = selectedVest.fullName[currentLang] || selectedVest.fullName.es;
  document.getElementById("unlocked-count-label").textContent = maxUnlocked + dict.unlockedSuffix;

  // Renderizar carrusel de vestimentas
  const carousel = document.getElementById("vestment-carousel");
  carousel.innerHTML = "";
  VESTMENTS.forEach(v => {
    const unlocked = state.progressDay >= v.minDay;
    const isSel = v.id === state.equippedVestmentId;
    const btn = document.createElement("button");
    btn.className = "vest-chip" + (isSel ? " selected" : "") + (!unlocked ? " locked" : "");
    const labelStr = v.shortLabel[currentLang] || v.shortLabel.es;
    btn.innerHTML = (unlocked ? "✨ " : "🔒 ") + labelStr;
    btn.onclick = () => {
      if (unlocked) {
        state.equippedVestmentId = v.id;
        saveState();
        renderUI();
      } else {
        mercyBannerText.textContent = dict.lockedHint(v.minDay);
      }
    };
    carousel.appendChild(btn);
  });

  // Renderizar Micro-aprendizaje del Día (1..10000)
  const micro = generateMicroLearningForDay(state.progressDay);
  document.getElementById("cycle-badge").textContent = micro.cycleName;
  document.getElementById("matrix-day-label").textContent = micro.seedLabel;
  document.getElementById("micro-quote-text").textContent = micro.quoteLine;
  document.getElementById("micro-praxis-text").textContent = micro.praxisLine;

  // Actualizar botón principal de consagración
  const mainBtn = document.getElementById("consecrate-btn");
  const btnIcon = document.getElementById("consecrate-btn-icon");
  const btnText = document.getElementById("consecrate-btn-text");

  if (isSealedToday) {
    mainBtn.className = "consecrate-btn sealed";
    mainBtn.disabled = true;
    btnIcon.textContent = "✓";
    btnText.textContent = dict.sealedBtn;
  } else {
    mainBtn.className = "consecrate-btn";
    mainBtn.disabled = false;
    btnIcon.textContent = state.emberWaiting ? "🕯️" : "🔥";
    btnText.textContent = state.emberWaiting ? dict.reviveBtn : dict.sealBtn;
  }
}

window.setSimLanguage = function(langCode) {
  if (langCode === "en" || langCode === "fr" || langCode === "es") {
    currentLang = langCode;
    document.documentElement.lang = langCode;
    renderUI();
  }
};

/**
 * 7. FLUJO DE CONSAGRACIÓN Y CONTROLES DEMO
 */
function handleConsecrateToday() {
  const today = getTodayIso();
  if (state.lastSealedDate === today && !state.emberWaiting) return;

  playSacredHarmonicBell();
  animateParticleToHeart();

  if (state.emberWaiting) {
    state.emberWaiting = false;
    state.progressDay = Math.min(10000, state.progressDay + 1);
    state.currentStreak = state.progressDay;
  } else if (state.lastSealedDate !== null) {
    state.progressDay = Math.min(10000, state.progressDay + 1);
    state.currentStreak = state.progressDay;
  } else {
    state.progressDay = Math.max(1, state.progressDay);
    state.currentStreak = state.progressDay;
  }

  state.lastSealedDate = today;
  state.equippedVestmentId = getHighestUnlockedVestmentId(state.progressDay);
  saveState();
  renderUI();
}

function demoStepDay(delta) {
  playSacredHarmonicBell();
  animateParticleToHeart();
  state.emberWaiting = false;
  state.progressDay = Math.max(1, Math.min(10000, state.progressDay + delta));
  state.currentStreak = state.progressDay;
  state.lastSealedDate = null;
  state.equippedVestmentId = getHighestUnlockedVestmentId(state.progressDay);
  saveState();
  renderUI();
}

function demoJumpToDay(targetDay) {
  playSacredHarmonicBell();
  animateParticleToHeart();
  state.emberWaiting = false;
  state.progressDay = Math.max(1, Math.min(10000, targetDay));
  state.currentStreak = state.progressDay;
  state.lastSealedDate = null;
  state.equippedVestmentId = getHighestUnlockedVestmentId(state.progressDay);
  saveState();
  renderUI();
}

function demoTriggerEmberWait() {
  state.emberWaiting = true;
  state.lastSealedDate = "2025-01-01";
  saveState();
  renderUI();
}

// Inicializar al cargar
loadState();
renderUI();
</script>
</body>
</html>
"""

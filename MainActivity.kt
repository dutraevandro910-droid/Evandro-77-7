package com.tvplay.app

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

class MainActivity : Activity() {
    private var player: ExoPlayer? = null
    private lateinit var playerView: PlayerView
    private lateinit var urlInput: EditText
    private lateinit var statusText: TextView

    private val backgroundColor = Color.rgb(7, 13, 25)
    private val panelColor = Color.rgb(17, 35, 59)
    private val blueColor = Color.rgb(45, 139, 240)
    private val whiteColor = Color.WHITE
    private val mutedColor = Color.rgb(174, 190, 210)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        buildScreen()
    }

    private fun buildScreen() {
        val page = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(18), dp(22), dp(18), dp(18))
            setBackgroundColor(backgroundColor)
        }

        val brand = TextView(this).apply {
            text = "TV PLAY"
            textSize = 27f
            setTextColor(whiteColor)
            gravity = Gravity.CENTER
            letterSpacing = 0.12f
        }
        page.addView(brand, matchWrap())

        val subtitle = TextView(this).apply {
            text = "Sua TV na palma da mão"
            textSize = 14f
            setTextColor(mutedColor)
            gravity = Gravity.CENTER
            setPadding(0, dp(5), 0, dp(18))
        }
        page.addView(subtitle, matchWrap())

        val categories = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
        }
        categories.addView(categoryButton("TV AO VIVO"), LinearLayout.LayoutParams(0, dp(46), 1f).apply {
            marginEnd = dp(6)
        })
        categories.addView(categoryButton("FILMES"), LinearLayout.LayoutParams(0, dp(46), 1f).apply {
            marginStart = dp(6)
        })
        page.addView(categories, matchWrap())

        val playerTitle = TextView(this).apply {
            text = "REPRODUTOR"
            textSize = 13f
            setTextColor(blueColor)
            setPadding(0, dp(20), 0, dp(8))
        }
        page.addView(playerTitle, matchWrap())

        playerView = PlayerView(this).apply {
            setBackgroundColor(Color.BLACK)
            useController = true
        }
        page.addView(playerView, LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, dp(205)
        ))

        statusText = TextView(this).apply {
            text = "Para assistir, informe um endereço de transmissão autorizado."
            textSize = 13f
            setTextColor(mutedColor)
            setPadding(0, dp(12), 0, dp(8))
        }
        page.addView(statusText, matchWrap())

        urlInput = EditText(this).apply {
            hint = "Cole o link autorizado (HLS .m3u8)"
            setHintTextColor(Color.GRAY)
            setTextColor(whiteColor)
            textSize = 14f
            setSingleLine(true)
            setPadding(dp(12), 0, dp(12), 0)
            setBackgroundColor(panelColor)
        }
        page.addView(urlInput, LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, dp(50)
        ).apply { bottomMargin = dp(10) })

        val playButton = Button(this).apply {
            text = "ASSISTIR"
            setTextColor(whiteColor)
            setBackgroundTintList(android.content.res.ColorStateList.valueOf(blueColor))
            setOnClickListener { playEnteredUrl() }
        }
        page.addView(playButton, LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, dp(50)
        ))

        val note = TextView(this).apply {
            text = "Use somente links de canais e vídeos que você tenha autorização para transmitir."
            textSize = 11f
            setTextColor(mutedColor)
            gravity = Gravity.CENTER
            setPadding(0, dp(14), 0, 0)
        }
        page.addView(note, matchWrap())

        setContentView(page)
    }

    private fun categoryButton(label: String): Button = Button(this).apply {
        text = label
        textSize = 12f
        setTextColor(whiteColor)
        setBackgroundTintList(android.content.res.ColorStateList.valueOf(panelColor))
        setOnClickListener {
            statusText.text = if (label == "TV AO VIVO") {
                "TV ao vivo: adicione um link autorizado abaixo."
            } else {
                "Filmes: adicione um link autorizado abaixo."
            }
        }
    }

    private fun playEnteredUrl() {
        val url = urlInput.text.toString().trim()
        if (!url.startsWith("https://")) {
            Toast.makeText(this, "Informe um link HTTPS válido e autorizado.", Toast.LENGTH_LONG).show()
            return
        }

        releasePlayer()
        val newPlayer = ExoPlayer.Builder(this).build()
        player = newPlayer
        playerView.player = newPlayer
        newPlayer.addListener(object : Player.Listener {
            override fun onPlayerError(error: PlaybackException) {
                statusText.text = "Não foi possível reproduzir. Confira o link e a autorização da transmissão."
            }
            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_READY) statusText.text = "Reprodução pronta."
                if (playbackState == Player.STATE_BUFFERING) statusText.text = "Carregando transmissão..."
            }
        })
        newPlayer.setMediaItem(MediaItem.fromUri(url))
        newPlayer.prepare()
        newPlayer.playWhenReady = true
    }

    private fun releasePlayer() {
        playerView.player = null
        player?.release()
        player = null
    }

    override fun onStop() {
        super.onStop()
        player?.pause()
    }

    override fun onDestroy() {
        releasePlayer()
        super.onDestroy()
    }

    private fun dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()
    private fun matchWrap() = LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
    )
}

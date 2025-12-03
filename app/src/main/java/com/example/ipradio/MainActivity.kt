package com.example.ipradio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.ipradio.ui.theme.IPRadioTheme
import android.content.res.Configuration

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Observer

import com.example.ipradio.RadioData.Radio


class MainActivity : ComponentActivity() {
    private var songInfo by mutableStateOf<String>("Empty...")
    private lateinit var playbackManager: PlaybackManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.playbackManager = PlaybackManager.getInstance(this)
        // Show played song info
        playbackManager.songInfo.observe(this, Observer {it ->
            songInfo = it.toString()
        })


        setContent {
            IPRadioTheme {
                Column {
                    ShowRadioList(playbackManager.getRadiosList())
                    Row {
                        Button(
                            onClick = {
                                playbackManager.playPrevRadio()
                            },
                            modifier = Modifier.padding(horizontal = 3.dp)
                        )
                        {
                            Text("Prev")
                        }
                        Button(onClick = {
                            playbackManager.stop()
                        }, modifier = Modifier.padding(horizontal = 3.dp)) {
                            Text("Stop")
                        }
                        Button(
                            onClick = {
                                playbackManager.playNextRadio()
                            },
                            modifier = Modifier.padding(horizontal = 3.dp)
                        )
                        {
                            Text("Next")
                        }
                    }
                    Row {
                        Text(
                            text = songInfo,
                            modifier = Modifier
                        )
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        playbackManager.stop()
    }

    @Composable
    fun ShowRadioList(radios: List<Radio>) {
        LazyColumn {
            items(radios) { radio ->
                RadioCard(radio)
            }
        }
    }

    @Composable
    fun RadioCard(radio: Radio) {

        Row(modifier = Modifier.padding(all = 8.dp)) {
            Text(
                text = radio.name,
                modifier = Modifier.clickable(
                ) {
                    if (playbackManager.selectedRadio == radio) {
                        // If the same radio is selected, stop the player
                        playbackManager.stop()
                        // Set selectedRadio to null since no radio is currently selected
                        playbackManager.selectedRadio = null
                    } else {
                        // If a different radio is selected, play the new radio
                        playbackManager.play(radio)
                    }
                },
                color = if (playbackManager.selectedRadio == radio) Color.Red else Color.Black
            )
        }
    }


    @Composable
    fun Greeting(name: String) {
        Text(text = "Hello $name!")
    }

    @Preview(name = "Light Mode")
    @Preview(
        uiMode = Configuration.UI_MODE_NIGHT_YES,
        showBackground = true,
        name = "Dark Mode"
    )
    @Composable
    fun GreetingPreview() {
        IPRadioTheme {
            Greeting("Androida")
        }
    }




}

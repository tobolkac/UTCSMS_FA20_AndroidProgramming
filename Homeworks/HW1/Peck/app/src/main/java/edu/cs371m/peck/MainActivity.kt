package edu.cs371m.peck

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.coroutines.*
import kotlin.random.Random

class MainActivity : AppCompatActivity(), CoroutineScope by MainScope() {

    // A round lasts this many milliseconds
    private val durationMillis = 8000L
    private var score = 0
    private val numWords = 6
    private val seed = 100L
    private var random = Random(seed)
    var testing = false

    override fun onDestroy() {
        cancel() // destroy all coroutines

        super.onDestroy()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        // XML button
        button.setOnClickListener {
            // If user gets impatient and reclicks button, cancel running coroutines
            coroutineContext.cancelChildren()
            newGame()
        }
    }

    private fun doScore(millisLeft: Long) {
        // User won Get one point plus tenths of a sec left
        if(millisLeft > 0) {
            if (testing) {
                score++
            } else {
                score += (millisLeft / 100).toInt()
                score++
            }
            scoreTV.text = score.toString()
            Log.d(localClassName, "User wins $score")
        }
    }

    private fun newGame() {
        // Play a round
        val timer = Timer(button)
        val words  = Words(sentence, frame, random)

        launch {
            val timerJob = async {
                timer.timerCo(durationMillis)
            }
            // Ok, this is an ugly hack.  We want to give the user
            // full credit for winning, even if we pass a constraint that
            // results in a zero-length word list.  But that code executes
            // so quickly the timer might not have even initialized.
            // So delay the words.playRound just slightly to give the
            // timer a chance to initialize
            delay(1)
            words.playRound(numWords) {
                val score = timer.millisLeft()
                doScore(score)
                launch { timerJob.cancelAndJoin() }
            }
        }
    }
}

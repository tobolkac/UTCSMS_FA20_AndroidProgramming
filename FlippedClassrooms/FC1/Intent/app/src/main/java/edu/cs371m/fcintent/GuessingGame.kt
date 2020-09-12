package edu.cs371m.fcintent

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.game.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random


// Recoded by witchel 8/21/2019
class GuessingGame : AppCompatActivity() {

    private var currentUser: String? = null
    private var theAnswer: Int = 0
    private var guesses: Int = 0
    private var mainToast: Toast? = null
    private val deterministic = false // Make it true for deterministic testing
    private var random = Random(if (deterministic) 1000L else System.currentTimeMillis())

    private fun doToast(message: String) {
        if (this.mainToast != null) {
            this.mainToast!!.cancel()
        }
        this.mainToast = Toast.makeText(this, message, Toast.LENGTH_LONG)
        this.mainToast!!.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game)

        currentUser = intent.getStringExtra(MainActivity.userKey)

        // From here to the end of the function is correct
        guesses = 0
        theAnswer = random.nextInt(101)

        currentUser?.apply {
            if (startsWith("Hint", ignoreCase = true)) {
                doToast("The number is $theAnswer")
                maxScoreText.text = "Max score possible: 1000" // XML
            }
        }
        guessButton.setOnClickListener {
            doGuess()
        }
    }

    private fun doGuess() {
        // XML
        if (theGuess.text.toString() == "") return // No number, no guess
        val guessInt = Integer.parseInt(theGuess.text.toString())

        when {
            guessInt < theAnswer -> { doToast("Too low")  }
            guessInt > theAnswer -> { doToast("Too high") }
            else -> {
                doToast("Correct")
                // This is a Kotlin coroutine, a light-weight thread that executes
                // independently from the main thread
                // Delay 2s to allow message
                GlobalScope.launch {
                    delay(2000)
                    finishGame()
                }
                return // Don't update guess count or display text
            }
        }
        theGuess.setText("")
        guesses++
        currentGuessText.text = "Guesses: $guesses"
        maxScoreText.text = String.format(
            "Max score possible: %3d",
            computeScore()
        )
    }

    private fun computeScore(): Int {
        return when(guesses) {
            in 0..3 -> 1000 - 15 * guesses
            in 4..7 -> 1000 - 30 * guesses
            in 8..11 -> 1000 - 50 * guesses
            else -> 0
        }
    }

    private fun finishGame() {
        setResult(
            RESULT_OK,
            Intent().apply {
                putExtra(MainActivity.userKey, currentUser)
                putExtra(MainActivity.scoreIntKey, computeScore())
            })

        finish()
    }
}


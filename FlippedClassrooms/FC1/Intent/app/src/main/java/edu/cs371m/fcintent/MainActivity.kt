package edu.cs371m.fcintent

import android.R.attr
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_score_board.*


// Recoded by witchel 8/21/2019
class MainActivity : AppCompatActivity() {
    companion object {
        const val GUESSING_GAME_REQUEST_CODE = 444
        const val userKey = "userKey"
        const val scoreIntKey = "scoreIntKey"
    }

    // List is  sorted descending by score
    private var highScores = mutableListOf<Score>()
    private var mainToast: Toast? = null

    class Score(var name: String, var score: Int) {
        override fun toString(): String {
            return this.name + ": " + this.score
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        highScores.addAll(
            listOf(
                Score("Frank Zappa", 997),
                Score("A Student", 997),
                Score("VSCO kid", 13)
            )
        )
        this.renderHighScores()
        playButton.setOnClickListener {
            play()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        if (id == R.id.menu_exit) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun addHighScore(score: Score) {
        highScores.add(score)
        renderHighScores()
    }

    private fun renderHighScores() {
        // Sort list.  NB: I love you Kotlin!
        highScores.sortWith(compareByDescending<Score> { it.score }.thenBy { it.name })
        // Convert Score objects into a list of strings
        val stringList = highScores.map { it.toString() }
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            stringList
        )
        highScoreList.adapter = adapter
    }

    private fun play() {
        val name = nameField.text.trim().toString()
        if (name.isEmpty()) {
            showEmptyNameToast()
        } else {
            startGuessingGameActivity(name)
        }
    }

    // NB: If you declare data: Intent, you get onActivityResult overrides nothing
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GUESSING_GAME_REQUEST_CODE && resultCode == RESULT_OK) {
            data?.let {
                val name = it.getStringExtra(userKey)
                val score = it.getIntExtra(scoreIntKey, -1)

                if (!name.isNullOrEmpty() && score > -1) {
                    addHighScore(Score(name, score))
                }
            }
        }
    }

    private fun showEmptyNameToast() {
        mainToast?.cancel()

        mainToast = Toast.makeText(this, "Name field is empty", Toast.LENGTH_LONG)
        mainToast?.show()
    }

    private fun startGuessingGameActivity(name: String) {
        val intent = Intent(this, GuessingGame::class.java).apply {
            putExtra(userKey, name)
        }

        startActivityForResult(intent, GUESSING_GAME_REQUEST_CODE)
    }
}

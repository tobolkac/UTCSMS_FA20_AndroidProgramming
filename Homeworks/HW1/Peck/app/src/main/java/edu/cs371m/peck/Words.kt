package edu.cs371m.peck

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.res.Resources
import android.graphics.Color
import android.text.TextUtils
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.view.setPadding
import kotlin.math.floor
import kotlin.math.min
import kotlin.random.Random


class Words(
    private val sentenceTV: TextView,
    private val frame: FrameLayout,
    private val random: Random
) {

    // public for testing
    var sourceText = PrideAndPrejudice
    private val neutralBgColor = Color.rgb(0xCD, 0xCD, 0xCD)
    private val outOfOrderColor = Color.rgb(200, 0, 0)
    private val punctSpaceStr = " \t\n._,:;“”?!-"
    private lateinit var words: List<String>
    private var currentWordIndex = 0
    private val minX = 8.dpToPx()
    private val maxX = frame.width - 8.dpToPx()
    val numberOfRows by lazy {
        floor(frame.height.toDouble()/textViewHeight).toInt()
    }
    private val availableRows by lazy {
        mutableListOf<Int>().also {
            (0 until numberOfRows).map { value -> it.add(value) }
        }
    }
    private val textViewHeight by lazy {
        val textView = createTextView("Doesn't matter", 0)
        textView.measure(0, 0)
        textView.measuredHeight
    }
    private val ids by lazy {
        sentenceTV.context.resources.obtainTypedArray(R.array.ids)
    }

    private fun findTVWidth(textView: TextView): Int {
        textView.measure(0, 0)
        return textView.measuredWidth
    }

    private fun createTextView(text: String, index: Int): TextView {
        return TextView(frame.context).apply {
            layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            this.text = text
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 18F)
            setPadding(8.dpToPx())
            setBackgroundColor(neutralBgColor)
            tag = index
            setSingleLine()
            ellipsize = TextUtils.TruncateAt.END
            if (index < ids.length()) {
                id = ids.getResourceId(index, 0)
            }
        }
    }

    private fun outOfOrderPick(view: View) {
        val colorToWarn : Animator = ValueAnimator
            .ofObject(ArgbEvaluator(), neutralBgColor, outOfOrderColor)
            .apply{duration = 200} // milliseconds
            .apply{addUpdateListener { animator -> view.setBackgroundColor(animator.animatedValue as Int) }}
        val colorFromWarn = ValueAnimator
            .ofObject(ArgbEvaluator(), outOfOrderColor, neutralBgColor)
            .apply{duration = 350}
            .apply{addUpdateListener { animator -> view.setBackgroundColor(animator.animatedValue as Int) }}
        val animatorSet = AnimatorSet()
        animatorSet.playSequentially(
            colorToWarn,
            colorFromWarn
        )
        animatorSet.start()
    }

    fun getWords() = words.toList()

    // Public for testing
    fun pickWords(start: Int, numWords: Int): List<String> {
        val words = mutableListOf<String>()
        val wordStrings = mutableListOf<String>()

        if (start < 0 || numWords <= 0 ) return  wordStrings

        //find next space to start at
        var wordStart = findNextPunctSpaceStrCharacter(start)
        var wordEnd = wordStart

        for (i in 0 until numWords) {
            //find start of word
            wordStart = findNextNonPunctSpaceStrCharacter(wordStart)
            wordEnd = wordStart

            //find end of word
            wordEnd = findNextPunctSpaceStrCharacter(wordEnd)

            val word = if(wordStart < sourceText.length) sourceText.substring(wordStart, wordEnd) else ""

            if (word.isNotEmpty()) {
                //add numbered suffix if needed
                val wordCount = words.count { it == word }
                wordStrings.add(if (wordCount == 0) word else "$word($wordCount)")

                words.add(word)
            }

            if (wordEnd == sourceText.length) {
                break
            }

            wordStart = wordEnd
        }

        return wordStrings
    }

    fun playRound(numWords: Int, wordsDone: () -> Unit) {
        frame.removeAllViews()

        words = pickWords(
            random.nextInt(0, sourceText.length),
            min(numWords, numberOfRows))

        // no need to play if there are no words
        if (words.isEmpty()) {
            wordsDone()
            return
        }

        sentenceTV.text = words.joinToString(separator = " ")

        words.withIndex().map { word ->
            createTextView(word.value, word.index).apply {
                randomlyAddToFrame(this)

                setOnClickListener {
                    if (it.tag == currentWordIndex) {
                        frame.removeView(it)
                        currentWordIndex++

                        if (currentWordIndex == words.size) {
                            wordsDone()
                        }
                    } else {
                        outOfOrderPick(it)
                    }
                }
            }
        }
    }

    private fun randomlyAddToFrame(view: TextView) {
        val rowIndex = random.nextInt(0, availableRows.size)
        val row = availableRows[rowIndex]
        availableRows.removeAt(rowIndex)

        val width = findTVWidth(view)

        (view.layoutParams as FrameLayout.LayoutParams).apply {
            this.topMargin = textViewHeight * row

            if (width >= maxX - minX) {
                this.marginStart = minX
                view.layoutParams.width = maxX - minX
            } else {
                this.marginStart = (minX..(maxX - findTVWidth(view))).random()
            }
        }

        frame.addView(view)
    }

    private fun findNextPunctSpaceStrCharacter(index: Int): Int {
        var internalIndex = index
        while (internalIndex < sourceText.length
                    && (!punctSpaceStr.contains(sourceText[internalIndex])
                        && !sourceText[internalIndex].isWhitespace())) {
            internalIndex++
        }

        return internalIndex
    }

    private fun findNextNonPunctSpaceStrCharacter(index: Int): Int {
        var internalIndex = index
        while (internalIndex < sourceText.length
                    && (punctSpaceStr.contains(sourceText[internalIndex])
                        || sourceText[internalIndex].isWhitespace())) {
            internalIndex++
        }

        return internalIndex
    }

    // Taken from https://medium.com/@johanneslagos/dp-to-px-and-viceversa-for-kotlin-d797815d852b
    private fun Int.dpToPx() = (this * Resources.getSystem().displayMetrics.density).toInt()

}

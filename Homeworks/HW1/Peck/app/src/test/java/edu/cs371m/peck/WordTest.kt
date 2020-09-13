//import androidx.test.core.app.ActivityScenario.launch
import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.marginTop
import androidx.core.view.setPadding
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import edu.cs371m.peck.Words
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.util.concurrent.CountDownLatch
import kotlin.random.Random


@RunWith(
    RobolectricTestRunner::class)
class WordTest {
    private val act: Context = getApplicationContext()
    val random  = Random(3)

    private fun testWords(start: Int, numWords: Int, expectedList: List<String>) {
        // XXX write me Initialize Words, call pickWords, assertEquals(expectedList, wordList)
    }

    // test cases from instructions
    @Test
    fun pickWords_instructionsPickingWordsTest1() {
        val sentenceTV = TextView(act)
        val frame = FrameLayout(act)

        val words = Words(sentenceTV, frame, random)

        val wordsList =  words.pickWords(0, 1)

        assertEquals(listOf("PRIDE"), wordsList)
    }

    @Test
    fun pickWords_instructionsPickingWordsTest2() {
        val sentenceTV = TextView(act)
        val frame = FrameLayout(act)
        val words = Words(sentenceTV, frame, random)

        val wordsList =  words.pickWords(1, 1)

        assertEquals(listOf("AND"), wordsList)
    }

    @Test
    fun pickWords_instructionsPickingWordsTest3() {
        val sentenceTV = TextView(act)
        val frame = FrameLayout(act)
        val words = Words(sentenceTV, frame, random)

        val wordsList =  words.pickWords(2, 1)

        assertEquals(listOf("AND"), wordsList)
    }

    @Test
    fun pickWords_instructionsDuplicateWordsTest() {
        val sentenceTV = TextView(act)
        val frame = FrameLayout(act)
        val words = Words(sentenceTV, frame, random)
        words.sourceText = "asdf he said that he"

        val wordsList =  words.pickWords(0, 4)

        assertEquals(listOf("he", "said", "that", "he(1)"), wordsList)
    }

    @Test
    fun pickWords_duplicateWords() {
        val sentenceTV = TextView(act)
        val frame = FrameLayout(act)
        val words = Words(sentenceTV, frame, random)
        words.sourceText = "adsf dog ran with a dog to another dog asdf"

        val wordsList =  words.pickWords(1, 8)

        assertEquals(listOf("dog", "ran", "with", "a", "dog(1)", "to", "another", "dog(2)"), wordsList)
    }

    @Test
    fun pickWords_allDuplicates() {
        val sentenceTV = TextView(act)
        val frame = FrameLayout(act)
        val words = Words(sentenceTV, frame, random)
        words.sourceText = "asdf dog dog dog dog dog dog dog"

        val wordsList =  words.pickWords(1, 7)

        assertEquals(listOf("dog", "dog(1)", "dog(2)", "dog(3)", "dog(4)", "dog(5)", "dog(6)"), wordsList)
    }

    @Test
    fun pickWords_duplicateWords_runIntoEnd() {
        val sentenceTV = TextView(act)
        val frame = FrameLayout(act)
        val words = Words(sentenceTV, frame, random)
        words.sourceText = "adsf dog ran with a dog to another dog"

        val wordsList =  words.pickWords(18, 8)

        assertEquals(listOf("dog", "to", "another", "dog(1)"), wordsList)
    }

    @Test
    fun pickWords_allDuplicates_runIntoEnd() {
        val sentenceTV = TextView(act)
        val frame = FrameLayout(act)
        val words = Words(sentenceTV, frame, random)
        words.sourceText = "asdf dog dog dog dog dog dog dog"

        val wordsList =  words.pickWords(17, 7)

        assertEquals(listOf("dog", "dog(1)", "dog(2)"), wordsList)
    }

    @Test
    fun pickWords_runIntoEnd_noWords() {
        val sentenceTV = TextView(act)
        val frame = FrameLayout(act)
        val words = Words(sentenceTV, frame, random)
        words.sourceText = "aasdfasdfasdf"

        val wordsList =  words.pickWords(1, 7)

        assertEquals(listOf<String>(), wordsList)
    }

    @Test
    fun pickWords_noWords() {
        val sentenceTV = TextView(act)
        val frame = FrameLayout(act)
        val words = Words(sentenceTV, frame, random)

        val wordsList =  words.pickWords(1, 0)

        assertEquals(listOf<String>(), wordsList)
    }

    @Test
    fun pickWords_startPastEnd() {
        val sentenceTV = TextView(act)
        val frame = FrameLayout(act)
        val words = Words(sentenceTV, frame, random)
        words.sourceText = "asdf he said that he"

        val wordsList =  words.pickWords(21, 4)

        assertEquals(listOf<String>(), wordsList)
    }

    @Test
    fun pickWords_startNegative() {
        val sentenceTV = TextView(act)
        val frame = FrameLayout(act)
        val words = Words(sentenceTV, frame, random)
        words.sourceText = "asdf he said that he"

        val wordsList =  words.pickWords(-3, 4)

        assertEquals(listOf<String>(), wordsList)
    }

    @Test
    fun pickWords_numWordsNegative() {
        val sentenceTV = TextView(act)
        val frame = FrameLayout(act)
        val words = Words(sentenceTV, frame, random)
        words.sourceText = "asdf he said that he"

        val wordsList =  words.pickWords(3, -4)

        assertEquals(listOf<String>(), wordsList)
    }

    @Test
    fun pickWords_startOnPunctuation() {
        val sentenceTV = TextView(act)
        val frame = FrameLayout(act)
        val words = Words(sentenceTV, frame, random)
        words.sourceText = "asdf he. said that he"

        val wordsList =  words.pickWords(8, 4)

        assertEquals(listOf("said", "that", "he"), wordsList)
    }

    @Test
    fun pickWords_startOnWord() {
        val sentenceTV = TextView(act)
        val frame = FrameLayout(act)
        val words = Words(sentenceTV, frame, random)
        words.sourceText = "asdf he. said that he"

        val wordsList =  words.pickWords(10, 4)

        assertEquals(listOf("that", "he"), wordsList)
    }

    @Test
    fun playRound_sentenceTVSet() {
        val sentenceTV = TextView(act)
        val frame = FrameLayout(act)
        val words = Words(sentenceTV, frame, random)
        frame.mockLayout(200, 200)

        words.playRound(7) {}

        assertEquals(sentenceTV.text.toString(), words.getWords().joinToString(separator = " "))
    }

    @Test
    fun playRound_correctNumberOfRows() {
        val sentenceTV = TextView(act)
        val frame = FrameLayout(act)
        val words = Words(sentenceTV, frame, random)
        frame.mockLayout(200, 400)

        words.playRound(6) {}

        assertEquals(7, words.numberOfRows)
    }

    @Test
    fun playRound_textViewConfig() {
        val sentenceTV = TextView(act)
        val frame = FrameLayout(act)
        val words = Words(sentenceTV, frame, random)
        frame.mockLayout(200, 400)

        words.playRound(6) {}

        val child = frame.getChildAt(0) as TextView
        assertEquals("between", child.text.toString())
        assertEquals(18F, child.textSize)
        assertEquals(8, child.paddingTop)
        assertEquals(8, child.paddingBottom)
        assertEquals(8, child.paddingStart)
        assertEquals(8, child.paddingEnd)
        assertEquals(0, child.tag as Int)
        assertEquals(1, child.lineCount)
        assertEquals(TextUtils.TruncateAt.END, child.ellipsize)
    }

    @Test
    fun playRound_first6TextViewIds() {
        val sentenceTV = TextView(act)
        val frame = FrameLayout(act)
        val words = Words(sentenceTV, frame, random)
        frame.mockLayout(200, 400)

        words.playRound(6) {}

        for (i in 0..5) {
            val child = frame.getChildAt(i) as TextView
            assertEquals("edu.cs371m.peck:id/text_id_${i + 1}", child.resources.getResourceName(child.id))
        }
    }

    @Test
    fun playRound_viewsOnSeparateRows() {
        val sentenceTV = TextView(act)
        val frame = FrameLayout(act)
        val words = Words(sentenceTV, frame, random)
        frame.mockLayout(200, 400)

        words.playRound(5) {}

        val seen = mutableSetOf<Int>()
        for (i in 0..4) {
            val child = frame.getChildAt(i) as TextView
            assert(!seen.contains(child.marginTop))
            seen.add(child.marginTop)
        }
    }

    @Test
    fun playRound_lessWordsThanNumberOfRows() {
        val sentenceTV = TextView(act)
        val frame = FrameLayout(act)
        val words = Words(sentenceTV, frame, random)
        frame.mockLayout(200, 400)

        words.playRound(6) {}

        assertEquals(7, words.numberOfRows)

        assertEquals(6, frame.childCount)
    }

    @Test
    fun playRound_MoreWordsThanNumberOfRows() {
        val sentenceTV = TextView(act)
        val frame = FrameLayout(act)
        val words = Words(sentenceTV, frame, random)
        frame.mockLayout(200, 400)

        words.playRound(12) {}

        assertEquals(7, words.numberOfRows)

        assertEquals(7, frame.childCount)
    }

    @Test
    fun playRound_WordsEqualsThanNumberOfRows() {
        val sentenceTV = TextView(act)
        val frame = FrameLayout(act)
        val words = Words(sentenceTV, frame, random)
        frame.mockLayout(200, 400)

        words.playRound(7) {}

        assertEquals(7, words.numberOfRows)

        assertEquals(7, frame.childCount)
    }

    /**
     * Mock the layout of a [ViewGroup] to test features that rely on the laid out size of a [View].
     */
    fun ViewGroup.mockLayout(width: Int = -1, height: Int = -1) {
        val widthMeasureSpec = if (width == -1) View.MeasureSpec.UNSPECIFIED else View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY)
        val heightMeasureSpec = if (height == -1) View.MeasureSpec.UNSPECIFIED else View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)

        measure(widthMeasureSpec, heightMeasureSpec)
        layout(0, 0, measuredWidth, measuredHeight)
        viewTreeObserver.dispatchOnGlobalLayout()
        measure(widthMeasureSpec, heightMeasureSpec)
        layout(0, 0, measuredWidth, measuredHeight)
    }
}


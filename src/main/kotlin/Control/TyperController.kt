package Control

import Model.ITextDeliverer
import Model.ITypingTextHighlighter
import javafx.application.Platform
import javafx.beans.property.SimpleStringProperty
import tornadofx.*
import kotlin.math.max

class TyperController(val textDeliverer: ITextDeliverer, val typingTextHighlighter: ITypingTextHighlighter) {
    val inputTextProperty = SimpleStringProperty("")
    private var inputText by inputTextProperty

    // All the committed text, not including that in the inputText
    private lateinit var typedText: String

    val highlightedTextSegments get() = typingTextHighlighter.highlightedSegments(typedText + inputText)

    init {
        next()
    }

    private fun next() {
        typedText = ""
        typingTextHighlighter.text = textDeliverer.next()
    }

    fun updateText(c: Char) {

        if ((c == ' ' || typedText.length + inputText.length == typingTextHighlighter.text.length)
                && typingTextHighlighter.currentWordCorrect(typedText.length, inputText)) {
            typedText += inputText
            inputText = ""

            if (typedText.length == typingTextHighlighter.text.length)
                next()
        }
    }

}
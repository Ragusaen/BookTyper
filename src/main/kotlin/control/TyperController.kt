package control

import model.ITextDeliverer
import model.ITypingTextHighlighter
import javafx.beans.property.SimpleStringProperty
import tornadofx.*

class TyperController(val textDeliverer: ITextDeliverer, val typingTextHighlighter: ITypingTextHighlighter, val updateMetaText: (String) -> Unit) {
    val inputTextProperty = SimpleStringProperty("")
    private var inputText by inputTextProperty

    // All the committed text, not including that in the inputText
    private lateinit var typedText: String

    val highlightedTextSegments get() = typingTextHighlighter.highlightedSegments(typedText + inputText)

    init {
        next()
    }

    fun next() {
        typedText = ""
        typingTextHighlighter.text = textDeliverer.next()
        updateMetaText(textDeliverer.currentSection)
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
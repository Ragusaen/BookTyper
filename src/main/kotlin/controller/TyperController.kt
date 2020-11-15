package controller

import model.ITextDeliverer
import model.ITypingTextHighlighter
import javafx.beans.property.SimpleStringProperty
import model.UserData
import tornadofx.*

class TyperController(val textDeliverer: ITextDeliverer, val typingTextHighlighter: ITypingTextHighlighter, val updateMetaText: (String) -> Unit) {
    val inputTextProperty = SimpleStringProperty("")
    private var inputText by inputTextProperty

    // All the committed text, not including that in the inputText
    private lateinit var typedText: String

    val highlightedTextSegments get() = typingTextHighlighter.highlightedSegments(typedText + inputText)

    val updates = mutableListOf<(TyperController) -> Unit>()

    init {
        next(advanceText = false)
    }

    fun next(wasTyped: Boolean = false, advanceText: Boolean = true) {
        if (wasTyped)
            UserData.current.totalCharactersTyped += typedText.length

        typedText = ""
        typingTextHighlighter.text = textDeliverer.next(advanceText)
        updateMetaText(textDeliverer.currentSection)

        updates.forEach { it.invoke(this) }
    }

    fun updateText(c: Char) {

        if ((c == ' ' || typedText.length + inputText.length == typingTextHighlighter.text.length)
                && typingTextHighlighter.currentWordCorrect(typedText.length, inputText)) {
            typedText += inputText
            inputText = ""

            if (typedText.length == typingTextHighlighter.text.length)
                next(true)
        }
    }

}
package view

import controller.TyperController
import model.*
import javafx.application.Platform
import javafx.scene.input.KeyEvent
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.text.TextFlow
import tornadofx.*

class TyperScope(val textDeliverer: ITextDeliverer, val textHighlighter: ITypingTextHighlighter, val updateMetaText: (String) -> Unit) : Scope()

class TyperView() : View() {
    override val scope = super.scope as TyperScope

    val controller = TyperController(scope.textDeliverer, scope.textHighlighter, scope.updateMetaText)

    override val root = VBox()

    fun highlightToColor(highlight: TextHighlight): Color =
            when (highlight) {
                TextHighlight.Correct -> Color.GREEN
                TextHighlight.Incorrect -> Color.RED
                TextHighlight.Upcoming -> Color.GRAY
            }


    lateinit var typeText: TextFlow

    init {
        with(root) {
            minWidth = 500.0
            maxWidth = 1000.0
            minHeight = 500.0
            maxHeight = 600.0

            vbox {
                border = Border(BorderStroke(Color.LIGHTGRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT))

                typeText = textflow() {
                    paddingAll = 5.0
                    style(append = true) {
                        backgroundColor += Color.WHITE
                    }
                }
            }
            updateTypeText()

            textfield(controller.inputTextProperty) {

                addEventHandler(KeyEvent.KEY_TYPED) {
                    // Run later to wait for textfield to update
                    Platform.runLater {
                        controller.updateText(it.character.first())
                        updateTypeText()
                    }
                }
            }
        }
    }

    fun updateTypeText() {
        typeText.children.clear()

        for (htext in controller.highlightedTextSegments.filter { it.text.isNotEmpty() }) {
            val t = if (htext.highlight == TextHighlight.Incorrect)
                            htext.text.replace(' ', '_')
                        else htext.text

            typeText.text(t) {
                fill = highlightToColor(htext.highlight)
                font = Font.font("Monospace", 24.0)
            }
        }
    }
}
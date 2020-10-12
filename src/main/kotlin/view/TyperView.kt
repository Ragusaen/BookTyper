package view

import control.TyperController
import model.*
import view.styles.GeneralStyle
import javafx.application.Platform
import javafx.scene.input.KeyEvent
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.text.TextFlow
import tornadofx.*

class TyperScope(val textDeliverer: ITextDeliverer, val textHighlighter: ITypingTextHighlighter) : Scope()

class TyperView() : View() {
    override val scope = super.scope as TyperScope

    val controller = TyperController(scope.textDeliverer, scope.textHighlighter)

    override val root = VBox()

    fun highlightToColor(highlight: TextHighlight): Color =
            when (highlight) {
                TextHighlight.Correct -> Color.GREEN
                TextHighlight.Incorrect -> Color.RED
                TextHighlight.Upcoming -> Color.GRAY
            }

    init {
        with(root) {
            minWidth = 200.0
            maxWidth = 1000.0

            style {
                backgroundColor += GeneralStyle.colorTheme.background
            }

            lateinit var typeText: TextFlow
            vbox {
                border = Border(BorderStroke(Color.LIGHTGRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT))
                paddingAll = 5.0

                typeText = textflow()
            }
            updateTypeText(typeText)

            textfield(controller.inputTextProperty) {

                addEventHandler(KeyEvent.KEY_TYPED) {
                    // Run later to wait for textfield to update
                    Platform.runLater {
                        controller.updateText(it.character.first())
                        updateTypeText(typeText)
                    }
                }
            }
        }
    }

    fun updateTypeText(typeText: TextFlow) {
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
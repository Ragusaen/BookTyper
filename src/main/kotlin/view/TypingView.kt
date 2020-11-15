package view

import model.*
import view.styles.GeneralStyle
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Orientation
import javafx.geometry.Pos
import javafx.scene.text.Font
import tornadofx.*
import java.io.File
import java.io.FileInputStream

class TypingScope(val textDeliverer: ITextDeliverer) : Scope()

class TypingView : View() {
    override val scope = super.scope as TypingScope

    private val textHighlighter = ErrorCorrectionHighlighter()

    private lateinit var typerView: TyperView

    override fun onDock() {
        with(currentStage!!) {
            minWidth = 700.0
            minHeight = 400.0
        }
    }

    override val root = vbox {
        paddingAll = 25.0

        alignment = Pos.CENTER

        label(SimpleStringProperty(scope.textDeliverer.title)) {
            style (append = true) {
                font = Font(48.0)
            }
        }
        val chapterLabel = label("Chapter") {
            style (append = true) {
                font = Font(32.0)
            }
        }

        listmenu {
            orientation = Orientation.HORIZONTAL
            items.addAll(listOf(ListMenuItem("Error Correct"), ListMenuItem("Free Errors")))

            items.apply {
                addClass(GeneralStyle.listMenuItem)
            }
        }

        val typerScope = TyperScope(scope.textDeliverer, textHighlighter) { chapterName ->
            chapterLabel.text = chapterName
        }
        typerView = find(typerScope)
        this += typerView

        hbox {
            button("Previous section") {
                action {
                    scope.textDeliverer.previous()
                    typerView.controller.next(advanceText = false)
                    typerView.updateTypeText()
                }
            }

            button("Skip chapter") {
                action {
                    scope.textDeliverer.skipChapter()
                    typerView.controller.next(advanceText = false)
                    typerView.updateTypeText()
                }
            }
        }



        hbox {
            alignment = Pos.CENTER

            label("${typerView.controller.textDeliverer.sectionProgress}") {
                typerView.controller.updates += { this.text = "${it.textDeliverer.sectionProgress}"}

                style(append = true) {
                    font = Font(32.0)
                }
            }
        }
    }

}
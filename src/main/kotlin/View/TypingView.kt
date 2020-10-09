package View

import Model.*
import View.styles.GeneralStyle
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.ObservableList
import javafx.geometry.Orientation
import javafx.geometry.Pos
import javafx.scene.text.Font
import tornadofx.*
import java.io.File
import java.io.FileInputStream

class TypingView : View() {

    val textDeliverer: ITextDeliverer = BookTextDeliverer(FileInputStream(File("/home/ragusa/Downloads/pg63400.epub")))

    val textHighlighter = ErrorCorrectionHighlighter()

    override val root = vbox {
        alignment = Pos.CENTER

        label(SimpleStringProperty(textDeliverer.title)) {
            style (append = true) {
                font = Font(48.0)
            }
        }
        label(SimpleStringProperty(textDeliverer.currentSection)) {
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

        val typerScope = TyperScope(textDeliverer, textHighlighter)
        this += find<TyperView>(typerScope)

    }

}
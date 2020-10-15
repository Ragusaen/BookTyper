package view.styles

import javafx.scene.paint.Color
import javafx.scene.text.Font
import tornadofx.*

class GeneralStyle : Stylesheet() {


    companion object {
        val listMenuItem by cssclass()

        var colorTheme: IColorTheme = LightColorTheme()
    }

    init {
        star {
            backgroundColor += colorTheme.background
        }

        label {
            textFill = colorTheme.foreground
        }

        listMenuItem {
            backgroundColor += colorTheme.alternative
            this.focusColor = colorTheme.contrast
        }

        button {
            backgroundColor += colorTheme.alternative
            focusColor = colorTheme.alternative2
        }

        textField {
            backgroundColor.elements.clear()
            backgroundColor += Color.WHITE
            font = Font.font("Monospace", 18.0)
        }
    }
}
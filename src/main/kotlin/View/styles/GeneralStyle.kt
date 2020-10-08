package View.styles

import javafx.scene.paint.Color
import tornadofx.*

class GeneralStyle : Stylesheet() {


    companion object {
        val listMenuItem by cssclass()

        var colorTheme: IColorTheme = LightColorTheme()
    }

    init {
        label {
            textFill = colorTheme.foreground
        }

        listMenuItem {
            backgroundColor += colorTheme.alternative
            this.focusColor = colorTheme.contrast
        }
    }
}
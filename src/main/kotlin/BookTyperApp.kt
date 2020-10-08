import View.TypingView
import View.styles.GeneralStyle
import tornadofx.*

class BookTyperApp : App(TypingView::class, GeneralStyle::class) {
    init {
        reloadStylesheetsOnFocus()


    }
}
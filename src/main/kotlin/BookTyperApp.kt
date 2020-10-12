import view.TypingView
import view.styles.GeneralStyle
import tornadofx.*

class BookTyperApp : App(TypingView::class, GeneralStyle::class) {
    init {
        reloadStylesheetsOnFocus()


    }
}
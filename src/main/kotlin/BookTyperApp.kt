import view.TypingView
import view.styles.GeneralStyle
import tornadofx.*
import view.TextSelectionView

class BookTyperApp : App(TextSelectionView::class, GeneralStyle::class) {
    init {
        reloadStylesheetsOnFocus()


    }
}
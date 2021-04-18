import javafx.scene.image.Image
import javafx.stage.Stage
import view.TypingView
import view.styles.GeneralStyle
import tornadofx.*
import view.TextSelectionView

class BookTyperApp : App(TextSelectionView::class, GeneralStyle::class) {

    override fun start(stage: Stage) {
        super.start(stage)
        stage.icons.add(Image(this.javaClass.getResourceAsStream("/icon.png")))
    }

    init {
        reloadStylesheetsOnFocus()


    }
}
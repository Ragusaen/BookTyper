package View

import Control.MainController
import Model.Book
import com.sun.tools.javac.Main
import javafx.scene.Parent
import tornadofx.*

class MainView : View() {
    override val root = vbox()

    private val controller:MainController by inject()

    private val books = controller.getLocalBooks().observable()

    init {
        label("All books")
        tableview(books) {
            readonlyColumn("Title", Book::title)
        }
    }
}
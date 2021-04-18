package view

import controller.MainController
import model.Book
import tornadofx.*

class MainView : View("BookTyper") {
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
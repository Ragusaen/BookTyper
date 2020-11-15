package view

import controller.TextSelectionController
import javafx.geometry.Pos
import javafx.scene.image.Image
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.text.Font
import model.Book
import model.BookTextDeliverer
import model.UserData
import tornadofx.*
import java.io.FileInputStream

class TextSelectionView : View() {
    override val root = VBox()

    val controller = TextSelectionController()

    init {
        with(root) {

            minHeight = 800.0
            minWidth = minHeight / 1.4

            val booksBox = vbox {}

            val updateBooks = {
                booksBox.clear()
                for (book in UserData.current.books)
                    booksBox.add(bookElement(book))
            }
            updateBooks()

            add(bookElement("Add new book", Image("file:" + this::class.java.getResource("/addbook.png").path))
                {controller.addBook(); updateBooks()})

            add(booksBox)
        }
    }

    private fun bookElement(book: Book): HBox = bookElement(book.title, Image(FileInputStream(book.coverPath))) { chooseText(book) }

    private fun bookElement(title: String, cover: Image, action: () -> Unit): HBox = hbox {
        paddingAll = 5.0
        imageview(cover) {
            isPreserveRatio = true
            fitHeight = 200.0
        }

        vbox {
            paddingAll = 50.0
            alignment = Pos.CENTER
            label(title) {
                font = Font(48.0)
            }
        }
        this.setOnMouseClicked { event ->
            if (event.button.ordinal == 1)
                action()
        }
    }

    private fun chooseText(book: Book) {
        replaceWith(find<TypingView>(TypingScope(BookTextDeliverer(book))))
    }
}
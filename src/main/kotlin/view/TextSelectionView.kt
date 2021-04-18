package view

import controller.TextSelectionController
import javafx.geometry.Pos
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import javafx.scene.image.Image
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
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

    lateinit var updateBooks: () -> Unit

    init {
        with(root) {

            minHeight = 800.0
            minWidth = minHeight / 1.4

            val booksBox = vbox {}

            updateBooks = {
                booksBox.clear()
                for (book in UserData.current.books)
                    booksBox.add(bookElement(book))
            }
            updateBooks()

            add(bookElement("Add new book", Image(this.javaClass.getResourceAsStream("/addbook.png")),
                {controller.addBook(); updateBooks()}))

            add(booksBox)
        }
    }

    private fun bookElement(book: Book): HBox = bookElement(book.title, Image(FileInputStream(book.coverPath)), { chooseText(book) }, { deleteBook(book)} )

    private fun bookElement(title: String, cover: Image, action: () -> Unit, delAction: (() -> Unit)? = null): HBox = hbox {
        paddingAll = 5.0
        hbox {
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
            hgrow = Priority.ALWAYS
        }
        if (delAction != null) {
            vbox {
                alignment = Pos.CENTER_RIGHT
                imageview(Image(this.javaClass.getResourceAsStream("/removebook.png"))) {
                    fitHeight = 50.0
                    fitWidth = 50.0
                }
                paddingRight = 15.0
                paddingLeft = 15.0
                this.setOnMouseClicked { event ->
                    if (event.button.ordinal == 1)
                        delAction()
                }
            }
        }
    }

    private fun chooseText(book: Book) {
        replaceWith(find<TypingView>(TypingScope(BookTextDeliverer(book))))
    }

    private fun deleteBook(book: Book) {
        alert(Alert.AlertType.CONFIRMATION, "Delete ${book.title}?",
            "Are you sure you want to delete ${book.title}?\nThis does not delete the epub file from your drive," +
                    " only removes it from BookTyper.", ButtonType.YES, ButtonType.NO, actionFn = { bt: ButtonType ->
                if (bt == ButtonType.YES) {
                    UserData.current.books.remove(book)
                    UserData.saveCurrent()
                    updateBooks()
                }
            })


    }
}
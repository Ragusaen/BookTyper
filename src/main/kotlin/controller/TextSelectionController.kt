package controller

import com.google.gson.Gson
import javafx.stage.FileChooser
import model.Book
import model.UserData
import nl.siegmann.epublib.epub.EpubReader
import tornadofx.FileChooserMode
import tornadofx.chooseFile
import java.io.File

class TextSelectionController {
    fun addBook() {
        val file = chooseFile("Add book", arrayOf(FileChooser.ExtensionFilter("Epub books (*.epub)", "*.epub")), FileChooserMode.Single)
                            .firstOrNull() ?: return

        val epubBook = EpubReader().readEpub(file.inputStream())

        val coverImage = File("${OS.appdataPath}covers/${epubBook.title.replace("[/\\\\?%*:|\"<>]".toRegex(), "-")}${UserData.current.bookCount}${epubBook.coverImage.mediaType.defaultExtension}")
        UserData.current.bookCount++


        coverImage.createNewFile()
        coverImage.writeBytes(epubBook.coverImage.data)

        UserData.current.books.add(Book(
                epubBook.title,
                file.absolutePath,
                coverImage.absolutePath,
                0,
                0
        ))

        UserData.saveCurrent()
    }
}
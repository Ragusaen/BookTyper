package control

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

        val coverImage = File("${OS.appdataPath}covers/${epubBook.title}${userData.bookCount}${epubBook.coverImage.mediaType.defaultExtension}")

        coverImage.createNewFile()
        coverImage.writeBytes(epubBook.coverImage.data)

        userData.books.add(Book(
                epubBook.title,
                file.absolutePath,
                coverImage.absolutePath,
                0,
                0
        ))

        saveUserData()
    }

    val userData: UserData

    private val userDataFile = File("${OS.appdataPath}user.data")

    fun saveUserData() {
        userDataFile.writeText(Gson().toJson(userData))
    }

    init {
        if (!userDataFile.exists()) {
            userData = UserData.Default
            val coversDir = File("${OS.appdataPath}covers")
            if (!coversDir.exists())
                coversDir.mkdirs()
        } else {
            userData = Gson().fromJson(userDataFile.reader(), UserData::class.java)
        }
    }
}
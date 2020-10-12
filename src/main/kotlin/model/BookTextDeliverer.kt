package model

import nl.siegmann.epublib.domain.Book as EpubBook
import nl.siegmann.epublib.domain.Resource
import nl.siegmann.epublib.epub.EpubReader
import java.io.FileInputStream

class BookTextDeliverer(epubFileStream: FileInputStream) : ITextDeliverer {
    private class BookIterator(book: EpubBook) {
        private val contents = book.contents
        private var index = 0

        val current get() = contents[index]

        fun advance() {
            index = (index + 1) % contents.size
        }
    }
    private val book = EpubReader().readEpub(epubFileStream)

    private val bookIterator = BookIterator(book)

    override val title = book.title
    override val currentSection = bookIterator.current.title


    override fun next(): String = bookIterator.apply { advance() }.current.reader.readText()

}
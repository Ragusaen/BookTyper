package Model

import nl.siegmann.epublib.epub.EpubReader
import java.io.FileInputStream

class BookTextDeliverer(epubFileStream: FileInputStream) : ITextDeliverer {
    private val book = EpubReader().readEpub(epubFileStream)

    override val title = book.title

    override val currentSection = "ars"

    private object BookIterator {
        private val index = 0
        val current = 
    }

    private val contentIterator = book.contents.iterator()

    override fun next(): String = contentIterator.next().mediaType.name

}
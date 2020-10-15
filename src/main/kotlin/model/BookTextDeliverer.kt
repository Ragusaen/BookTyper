package model

import nl.siegmann.epublib.domain.Resource
import nl.siegmann.epublib.epub.EpubReader
import org.jsoup.Jsoup
import org.jsoup.parser.Parser
import java.io.FileInputStream



class BookTextDeliverer(val book: Book, override val save: () -> Unit) : ITextDeliverer {
    companion object {
        val cleaningMap = mapOf(
                '“' to "\"",
                '”' to "\"",
                '’' to "\'",
                '—' to " - ",
                '…' to "..."
        )
    }


    data class BookSection(val name: String, val resource: Resource)

    private val epubBook = EpubReader().readEpub(FileInputStream(book.bookPath))

    private val contents: List<BookSection>

    private var contentIndex = book.contentsIndex

    private var texts = listOf<String>()
    private var textIndex = book.sectionIndex

    override val title: String

    override val currentSection get() = contents[contentIndex].name

    init {
        val doc = Jsoup.parse(epubBook.spine.tocResource.reader.readText())
        val sections = mutableListOf<BookSection>()
        val tcontents = epubBook.contents.toMutableList()

        for (sec in doc.select("navPoint")) {
            val name = sec.select("text").text()
            val href = sec.select("content").attr("src").substringBefore('#')

            val resource = tcontents.firstOrNull { it.href == href }
            if (resource != null) {
                tcontents.remove(resource)

                sections.add(BookSection(name, resource))
            }
        }
        contents = sections
        title = doc.select("docTitle").text()

        updateHtmlElements()
    }



    override fun next(): String  {
        val text = texts[textIndex]

        book.contentsIndex = contentIndex
        book.sectionIndex = textIndex
        save()

        textIndex++

        if (textIndex >= texts.size) {
            contentIndex++
            textIndex = 0

            updateHtmlElements()
        }



        return text
    }

    override fun skipChapter() {
        contentIndex++
        textIndex = 0

        updateHtmlElements()
    }

    private fun updateHtmlElements() {
        val getElements = {
            val html = contents[contentIndex].resource.reader.readText()
            val elems = Jsoup.parse(html).body().allElements

            texts = elems.filter {
                when (it.tagName()) {
                    "p" -> true
                    "h1" -> true
                    "h2" -> true
                    "h3" -> true
                    else -> false
                }
            }.map { it.text() }.filter { it.length > 1 }
        }

        getElements()
        while (texts.isEmpty()) {
            getElements()

            contentIndex++
        }

        // Fix text
        texts = texts.map { cleanText(it) }
    }


    fun cleanText(text: String): String =
            String(text.fold(mutableListOf<Char>()) { acc, c -> acc.apply { addAll((cleaningMap[c] ?: "$c").toList()) } }.toCharArray())
}

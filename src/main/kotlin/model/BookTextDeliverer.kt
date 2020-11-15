package model

import nl.siegmann.epublib.domain.Resource
import nl.siegmann.epublib.epub.EpubReader
import org.jsoup.Jsoup
import org.jsoup.parser.Parser
import java.io.FileInputStream
import java.lang.Integer.max
import java.lang.Integer.min


class BookTextDeliverer(val book: Book) : ITextDeliverer {
    companion object {
        val cleaningMap: Map<Char,String> = mapOf(
                '“' to "\"",
                '”' to "\"",
                '’' to "\'",
                '—' to " - ",
                '…' to "...",
                '–' to "-",
                '‘' to "'",
                '’' to "'"
        )
    }

    data class BookSection(val name: String, val resource: Resource)

    private val epubBook = EpubReader().readEpub(FileInputStream(book.bookPath))

    private val contents: List<BookSection>

    private var contentIndex = book.contentsIndex
        set(value) {
            field = max(0, min(value, contents.size - 1))
            textIndex = 0
            updateHtmlElements()
        }

    private var texts = listOf<String>()
    private var textIndex = book.sectionIndex
        set(value) {
            field = if (value < 0) {
                contentIndex--
                texts.size - 1
            } else if (value >= texts.size) {
                contentIndex++
                0
            } else
                value
        }

    override val sectionProgress: Double get() = textIndex.toDouble() / texts.size

    override val title: String

    override val currentSection get() = contents[contentIndex].name

    init {
        val doc = Jsoup.parse(epubBook.spine.tocResource.reader.readText())
        val sections = mutableListOf<BookSection>()
        var tcontents = epubBook.contents.toMutableList()

        for (sec in doc.select("navPoint")) {
            val name = sec.select("text").text()
            val href = sec.select("content").attr("src").substringBefore('#')

            val resource = tcontents.firstOrNull { it.href == href }
            if (resource != null) {
                tcontents.remove(resource)

                sections.add(BookSection(name, resource))
            }
        }

        // Try to determine play order using opf
        if (tcontents.size > 0) {
            println("${tcontents.size} spine elements could not be determined with toc.nxc, trying .opf")
            tcontents = epubBook.contents.toMutableList()
            val opfDoc = Jsoup.parse(epubBook.opfResource.reader.readText())

            val spine = opfDoc.select("spine")
            val manifest = opfDoc.select("manifest").select("item").map { Pair(it.attr("id"), it) }

            var sectionIndex = 0

            for (itemref in spine.select("itemref")) {
                val item = (manifest.firstOrNull { it.first == itemref.attr("idref") } ?: continue).second

                val resource = tcontents.firstOrNull { it.href == item.attr("href") } ?: continue

                if (!sections.any { it.resource.id == resource.id})
                    sections.add(sectionIndex, BookSection("Section ${sectionIndex + 1}", resource))
                sectionIndex++


                tcontents.remove(resource)
            }
            println("${tcontents.size} spine elements were not determined with .opf")
            println(sections)
        }


        contents = sections
        title = doc.select("docTitle").text()

        updateHtmlElements()
    }

    override fun next(advance: Boolean): String {
        if (advance) {
            textIndex++

            book.contentsIndex = contentIndex
            book.sectionIndex = textIndex
            UserData.saveCurrent()
        }

        return texts[textIndex]
    }

    override fun previous() {
        textIndex -= 1
    }

    override fun skipChapter() {
        contentIndex++
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


    private fun cleanText(text: String): String =
            String(text.fold(mutableListOf<Char>()) { acc, c -> acc.apply { addAll((cleaningMap[c] ?: "$c").toList()) } }.toCharArray())
}

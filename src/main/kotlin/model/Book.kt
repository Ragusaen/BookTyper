package model

import nl.siegmann.epublib.domain.Book

data class Book(val title: String,
                val bookPath: String,
                val coverPath: String,
                var contentsIndex: Int,
                var sectionIndex: Int
)
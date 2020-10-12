package model

data class HighlightedTextSegment(val text: String, val highlight: TextHighlight)

enum class TextHighlight { Correct, Incorrect, Upcoming }

interface ITypingTextHighlighter {
    var text: String
    fun highlightedSegments(typed: String): List<HighlightedTextSegment>
    fun currentWordCorrect(startIndex: Int, current: String): Boolean

}
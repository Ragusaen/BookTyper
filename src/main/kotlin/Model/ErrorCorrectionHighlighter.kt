package Model

import java.lang.Integer.min

class ErrorCorrectionHighlighter : ITypingTextHighlighter {

    // The correctly typed text
    override var text: String = ""

    override fun highlightedSegments(typed: String): List<HighlightedTextSegment> {
        if (text.isBlank())
            return listOf()

        val correct = text.commonPrefixWith(typed)
        val incorrect = text.substring(correct.length, min(typed.length, text.length))
        val upcoming = text.substring(min(typed.length, text.length))
        return listOf(
                HighlightedTextSegment(correct, TextHighlight.Correct),
                HighlightedTextSegment(incorrect, TextHighlight.Incorrect),
                HighlightedTextSegment(upcoming, TextHighlight.Upcoming)
        )
    }

    override fun currentWordCorrect(startIndex: Int, current: String): Boolean =
            text.substring(startIndex, startIndex + current.length) == current
}
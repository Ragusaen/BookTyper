package model

interface ITextDeliverer {
    val sectionProgress: Double

    val title: String
    val currentSection: String

    fun next(advance: Boolean = true): String
    fun previous()
    fun skipChapter() //Skip to the next major thing
}
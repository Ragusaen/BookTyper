package model

interface ITextDeliverer {
    val title: String
    val currentSection: String

    fun next(): String
    fun skipChapter() //Skip to the next major thing

    val save: () -> Unit
}
package model

interface ITextDeliverer {
    val title: String
    val currentSection: String

    fun next(): String
}
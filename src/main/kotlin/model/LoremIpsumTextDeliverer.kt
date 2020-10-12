package model

class LoremIpsumTextDeliverer : ITextDeliverer {
    override val title: String
        get() = "Lorem Ipsum"

    override val currentSection: String
        get() = "One of one"

    override fun next(): String = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque sit amet sollicitudin erat. Cras eu mi gravida, aliquet dui ut, varius neque"

}
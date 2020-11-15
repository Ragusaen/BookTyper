package model

class LoremIpsumTextDeliverer : ITextDeliverer {
    override val sectionProgress: Double get() = 0.5

    override val title: String
        get() = "Lorem Ipsum"

    override val currentSection: String
        get() = "One of one"

    override fun next(advance: Boolean): String = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque sit amet sollicitudin erat. Cras eu mi gravida, aliquet dui ut, varius neque"
    override fun previous() = Unit

    override fun skipChapter() {}

}
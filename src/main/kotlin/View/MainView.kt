package View

import Control.MainController
import com.sun.tools.javac.Main
import javafx.scene.Parent
import tornadofx.View
import tornadofx.vbox

class MainView : View() {
    override val root = vbox()

    val controller:MainController by inject()

    val books = controller.getLocalBooks()


}
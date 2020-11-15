package model

import com.google.gson.Gson
import java.io.File

class UserData(val books: MutableList<Book>, var bookCount: Int, var totalCharactersTyped: Int) {
    companion object {
        val Default = UserData(mutableListOf(), 0, 0)


        private val userDataFile = File("${OS.appdataPath}userdata.json")

        var current = run {
            if (!userDataFile.exists()) {
                val coversDir = File("${OS.appdataPath}covers")
                if (!coversDir.exists())
                    coversDir.mkdirs()

                UserData.Default
            } else
                Gson().fromJson(userDataFile.reader(), UserData::class.java)

        }

        fun saveCurrent() {
            userDataFile.writeText(Gson().toJson(current))
        }
    }
}
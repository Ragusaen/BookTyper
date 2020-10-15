package model

import com.google.gson.Gson
import java.io.File



class UserData(val books: MutableList<Book>, val bookCount: Int) {
    companion object {
        val Default = UserData(mutableListOf(), 0)
        
    }
}
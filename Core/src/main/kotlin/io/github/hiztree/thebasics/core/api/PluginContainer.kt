package io.github.hiztree.thebasics.core.api

import io.github.hiztree.thebasics.core.api.cmd.sender.ConsoleSender
import io.github.hiztree.thebasics.core.api.user.User
import java.io.File
import java.util.*

interface PluginContainer {

    val users: ArrayList<User>

    fun init()

    fun getUser(name: String = "", uniqueID: UUID? = null): User? {
        if (name.isEmpty() && uniqueID != null) {
            return users.firstOrNull { it.getUniqueID() == uniqueID }
        }

        return users.firstOrNull { it.getName().equals(name, true) }
    }

    fun getConsoleSender(): ConsoleSender

    fun getImplementation(): Implementation

    fun getConfigDir(): File {
        return if(getImplementation() == Implementation.BUKKIT)
            File("plugins/TheBasics")
        else
            File("config/thebasics")
    }

    fun getPlayerDir(): File {
        return if(getImplementation() == Implementation.BUKKIT)
            File("plugins/TheBasics/Players")
        else
            File("config/thebasics/players")
    }
}
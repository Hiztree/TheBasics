package io.github.hiztree.thebasics.spigot.impl

import io.github.hiztree.thebasics.core.api.cmd.sender.ConsoleSender
import org.bukkit.ChatColor
import org.bukkit.command.ConsoleCommandSender
import java.util.*

class SpigotConsoleSender(private val console: ConsoleCommandSender) : ConsoleSender {

    override fun getName(): String {
        return "console"
    }

    override fun getUniqueID(): UUID {
        return UUID.randomUUID()
    }

    override fun sendMsg(msg: String) {
        console.sendMessage(ChatColor.translateAlternateColorCodes('&', msg))
    }

    override fun hasPermission(permission: String): Boolean {
        return true
    }
}
package io.github.hiztree.thebasics.core.api.cmd.sender

import io.github.hiztree.thebasics.core.api.lang.LangKey
import java.util.*

interface CommandSender {

    fun getName(): String
    fun getUniqueID(): UUID
    fun sendMsg(msg: String)

    fun sendMsg(key: LangKey, vararg args: Any) {
        var msg = key.msg

        for (index in args.indices) {
            msg = msg.replace("{$index}", args[index].toString())
        }

        sendMsg(msg)
    }

    fun hasPermission(permission: String): Boolean
}
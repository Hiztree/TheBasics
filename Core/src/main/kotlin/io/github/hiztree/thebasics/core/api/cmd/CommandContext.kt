package io.github.hiztree.thebasics.core.api.cmd

import com.google.common.reflect.TypeToken
import io.github.hiztree.thebasics.core.api.cmd.sender.CommandSender

abstract class CommandContext<T>(val type: TypeToken<T>) {

    @Throws(CommandException::class)
    abstract fun complete(sender: CommandSender, input: String): T

    open fun tab(sender: CommandSender, last: String): List<String> = emptyList()
}


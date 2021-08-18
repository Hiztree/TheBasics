package io.github.hiztree.thebasics.core.api.cmd

import io.github.hiztree.thebasics.core.api.cmd.sender.CommandSender
import io.github.hiztree.thebasics.core.api.lang.LangKey

class UsageException(target: CommandSender, key: LangKey, vararg args: Any) : Exception() {

    init {
        target.sendMsg(key, *args)
    }

}
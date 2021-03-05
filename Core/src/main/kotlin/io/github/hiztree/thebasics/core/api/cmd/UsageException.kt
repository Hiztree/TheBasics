package io.github.hiztree.thebasics.core.api.cmd

import io.github.hiztree.thebasics.core.api.cmd.sender.CommandSender
import io.github.hiztree.thebasics.core.api.lang.LangKey

class UsageException(target: CommandSender, key: LangKey) : Exception() {

    init {
        target.sendMsg(key.msg)
    }

}
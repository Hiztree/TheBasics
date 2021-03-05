package io.github.hiztree.thebasics.core.commands

import io.github.hiztree.thebasics.core.TheBasics
import io.github.hiztree.thebasics.core.api.cmd.JoinedString
import io.github.hiztree.thebasics.core.api.cmd.UsageException
import io.github.hiztree.thebasics.core.api.cmd.annotation.Arg
import io.github.hiztree.thebasics.core.api.cmd.annotation.BasicCmd
import io.github.hiztree.thebasics.core.api.cmd.annotation.DefaultCmd
import io.github.hiztree.thebasics.core.api.cmd.sender.CommandSender
import io.github.hiztree.thebasics.core.api.lang.LangKey
import io.github.hiztree.thebasics.core.api.user.User

@BasicCmd("kick", "Kick a user from the server.")
class KickCmd {

    @DefaultCmd
    fun kickCmd(sender: CommandSender, @Arg("target") user: User, @Arg("reason") reason: JoinedString) {
        user.kick(reason.toString())

        TheBasics.instance.users
                .filter { it.hasPermission("thebasics.kick.notify") }
                .forEach { it.sendMsg(LangKey.KICK_NOTIFY, user.getName()) }
    }
}
package io.github.hiztree.thebasics.core.commands

import io.github.hiztree.thebasics.core.TheBasics
import io.github.hiztree.thebasics.core.api.BasicTime
import io.github.hiztree.thebasics.core.api.cmd.JoinedString
import io.github.hiztree.thebasics.core.api.cmd.UsageException
import io.github.hiztree.thebasics.core.api.cmd.annotation.Arg
import io.github.hiztree.thebasics.core.api.cmd.annotation.BasicCmd
import io.github.hiztree.thebasics.core.api.cmd.annotation.DefaultCmd
import io.github.hiztree.thebasics.core.api.cmd.sender.CommandSender
import io.github.hiztree.thebasics.core.api.lang.LangKey
import io.github.hiztree.thebasics.core.api.user.User

@BasicCmd("mute", "Mute a user for a specific duration.")
class MuteCmd {

    @DefaultCmd
    fun muteCmd(
        sender: CommandSender,
        @Arg("target") user: User,
        @Arg("duration") duration: BasicTime,
        @Arg("reason") reason: JoinedString
    ) {
        if (user.isMuted())
            throw UsageException(sender, LangKey.MUTE_ERROR)

        user.mute(duration, reason.toString())

        TheBasics.instance.users
            .filter { it.hasPermission("thebasics.mute.notify") }
            .forEach { it.sendMsg(LangKey.MUTE_NOTIFY, user.getName(), duration.toString()) }
    }
}
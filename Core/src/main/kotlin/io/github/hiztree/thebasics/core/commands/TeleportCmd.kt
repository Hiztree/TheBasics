package io.github.hiztree.thebasics.core.commands

import io.github.hiztree.thebasics.core.api.cmd.annotation.Arg
import io.github.hiztree.thebasics.core.api.cmd.annotation.BasicCmd
import io.github.hiztree.thebasics.core.api.cmd.annotation.DefaultCmd
import io.github.hiztree.thebasics.core.api.lang.LangKey
import io.github.hiztree.thebasics.core.api.user.User

@BasicCmd("teleport|tp", "Teleport to a players locations.")
class TeleportCmd {

    @DefaultCmd
    fun tpCmd(sender: User, @Arg("target") target: User) {
        sender.teleport(target.getWorld(), target.getLocation())

        sender.sendMsg(LangKey.TELEPORT_SENDER, target.getName())
    }
}
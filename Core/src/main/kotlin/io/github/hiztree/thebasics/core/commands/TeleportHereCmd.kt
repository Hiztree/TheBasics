package io.github.hiztree.thebasics.core.commands

import io.github.hiztree.thebasics.core.api.cmd.annotation.Arg
import io.github.hiztree.thebasics.core.api.cmd.annotation.BasicCmd
import io.github.hiztree.thebasics.core.api.cmd.annotation.DefaultCmd
import io.github.hiztree.thebasics.core.api.lang.LangKey
import io.github.hiztree.thebasics.core.api.user.User

@BasicCmd("teleporthere|tphere", "Teleport a player to your location.")
class TeleportHereCmd {

    @DefaultCmd
    fun tpHereCmd(sender: User, @Arg("target") target: User) {
        target.teleport(sender.getWorld(), sender.getLocation())

        target.sendMsg(LangKey.TELEPORT_HERE_TARGET, sender.getName())
    }
}
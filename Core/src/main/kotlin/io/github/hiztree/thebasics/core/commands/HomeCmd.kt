package io.github.hiztree.thebasics.core.commands

import io.github.hiztree.thebasics.core.api.cmd.annotation.Arg
import io.github.hiztree.thebasics.core.api.cmd.annotation.BasicCmd
import io.github.hiztree.thebasics.core.api.cmd.annotation.DefaultCmd
import io.github.hiztree.thebasics.core.api.lang.LangKey
import io.github.hiztree.thebasics.core.api.user.User
import io.github.hiztree.thebasics.core.api.user.data.Home

@BasicCmd("home|homes", "Teleport to your home.")
class HomeCmd {

    @DefaultCmd
    fun homeCmd(sender: User, @Arg("home", true) home: Home?) {
        if (home != null) {
            sender.teleport(home.world, home.location)
            return
        } else {
            val homes = sender.homes

            when (homes.size) {
                1 -> {
                    val singleHome = sender.homes.first()

                    sender.teleport(singleHome.world, singleHome.location)
                    return
                }
                0 -> {
                    sender.sendMsg(LangKey.REQ_HOME)
                    return
                }
                else -> {
                    sender.sendMsg(LangKey.HOME_LIST, sender.homes.joinToString(", ") { it.name })
                    return
                }
            }
        }
    }
}
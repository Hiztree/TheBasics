package io.github.hiztree.thebasics.core.commands

import io.github.hiztree.thebasics.core.api.cmd.annotation.Arg
import io.github.hiztree.thebasics.core.api.cmd.annotation.BasicCmd
import io.github.hiztree.thebasics.core.api.cmd.annotation.DefaultCmd
import io.github.hiztree.thebasics.core.api.lang.LangKey
import io.github.hiztree.thebasics.core.api.user.User
import io.github.hiztree.thebasics.core.api.user.data.Home

@BasicCmd("sethome", "Sets your current location as a home.")
class SetHomeCmd {

    @DefaultCmd
    fun setHomeCmd(sender: User, @Arg("name") name: String) {
        try {
            sender.homes.add(Home(name, sender.getLocation(), sender.getWorld()))
            sender.save()

            sender.sendMsg(LangKey.SET_HOME, name)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
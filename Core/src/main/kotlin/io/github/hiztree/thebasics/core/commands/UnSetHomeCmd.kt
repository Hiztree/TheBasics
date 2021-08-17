package io.github.hiztree.thebasics.core.commands

import io.github.hiztree.thebasics.core.api.cmd.annotation.Arg
import io.github.hiztree.thebasics.core.api.cmd.annotation.BasicCmd
import io.github.hiztree.thebasics.core.api.cmd.annotation.DefaultCmd
import io.github.hiztree.thebasics.core.api.lang.LangKey
import io.github.hiztree.thebasics.core.api.user.User
import io.github.hiztree.thebasics.core.api.user.data.Home

@BasicCmd("unsethome", "Removes a home by its name.")
class UnSetHomeCmd {

    @DefaultCmd
    fun unSetHomeCmd(sender: User, @Arg("home") home: Home) {
        sender.homes.remove(home)
        sender.save()

        sender.sendMsg(LangKey.UN_SET_HOME, home.name)
    }
}
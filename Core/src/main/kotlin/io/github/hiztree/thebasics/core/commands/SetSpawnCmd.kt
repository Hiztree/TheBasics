package io.github.hiztree.thebasics.core.commands

import io.github.hiztree.thebasics.core.TheBasics
import io.github.hiztree.thebasics.core.api.cmd.annotation.BasicCmd
import io.github.hiztree.thebasics.core.api.cmd.annotation.DefaultCmd
import io.github.hiztree.thebasics.core.api.lang.LangKey
import io.github.hiztree.thebasics.core.api.user.User

@BasicCmd("setspawn", "Set the servers spawn.")
class SetSpawnCmd {

    @DefaultCmd
    fun setSpawnCmd(sender: User) {
        val node = TheBasics.instance.dataConfig["spawn"]
        node.node("world").set(sender.getWorld().uniqueID.toString())
        node.node("location").set(sender.getLocation())

        TheBasics.instance.dataConfig.save()

        sender.sendMsg(LangKey.SET_SPAWN)
    }
}
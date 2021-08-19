package io.github.hiztree.thebasics.core.commands

import io.github.hiztree.thebasics.core.TheBasics
import io.github.hiztree.thebasics.core.api.cmd.annotation.Arg
import io.github.hiztree.thebasics.core.api.cmd.annotation.BasicCmd
import io.github.hiztree.thebasics.core.api.cmd.annotation.DefaultCmd
import io.github.hiztree.thebasics.core.api.data.Warp
import io.github.hiztree.thebasics.core.api.lang.LangKey
import io.github.hiztree.thebasics.core.api.user.User

@BasicCmd("setwarp", "Add a new server warp.")
class SetWarpCmd {

    @DefaultCmd
    fun setWarpCmd(sender: User, @Arg("name") name: String) {
        var replace = false

        for (w in WarpCmd.warps) {
            if (w.name.equals(name, true)) {
                w.world = sender.getWorld()
                w.location = sender.getLocation()

                sender.sendMsg(LangKey.REPLACE_WARP, name)
                replace = true
                break
            }
        }

        if (!replace) {
            WarpCmd.warps.add(Warp(name, sender.getWorld(), sender.getLocation()))
            sender.sendMsg(LangKey.SET_WARP, name)
        }

        TheBasics.instance.dataConfig["warps"].setList(Warp::class.java, WarpCmd.warps)
        TheBasics.instance.dataConfig.save()
    }
}
package io.github.hiztree.thebasics.core.commands

import io.github.hiztree.thebasics.core.TheBasics
import io.github.hiztree.thebasics.core.api.cmd.UsageException
import io.github.hiztree.thebasics.core.api.cmd.annotation.Arg
import io.github.hiztree.thebasics.core.api.cmd.annotation.BasicCmd
import io.github.hiztree.thebasics.core.api.cmd.annotation.DefaultCmd
import io.github.hiztree.thebasics.core.api.data.Warp
import io.github.hiztree.thebasics.core.api.lang.LangKey
import io.github.hiztree.thebasics.core.api.user.User

@BasicCmd("warp", "Teleport to a defined warp.")
class WarpCmd {

    companion object {
        val warps: MutableList<Warp> =
            TheBasics.instance.dataConfig["warps"].getList(Warp::class.java, arrayListOf())
    }

    @DefaultCmd
    fun warpCmd(sender: User, @Arg("warp", true) warp: Warp?) {
        if (warp != null && warp.valid) {
            if (!sender.hasPermission("thebasics.warp.${warp.name}"))
                throw UsageException(sender, LangKey.WARP_PERMISSION)

            sender.teleport(warp.world!!, warp.location!!)
            sender.sendMsg(LangKey.WARP_USE, warp.name)
        } else {
            sender.sendMsg(LangKey.WARP_LIST, warps.joinToString {
                if (it.valid)
                    "&r${it.name}"
                else
                    "&c&m${it.name}&r"
            })
        }
    }
}
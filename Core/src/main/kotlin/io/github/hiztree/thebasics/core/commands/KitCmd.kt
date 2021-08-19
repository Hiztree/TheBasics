package io.github.hiztree.thebasics.core.commands

import io.github.hiztree.thebasics.core.api.cmd.UsageException
import io.github.hiztree.thebasics.core.api.cmd.annotation.Arg
import io.github.hiztree.thebasics.core.api.cmd.annotation.BasicCmd
import io.github.hiztree.thebasics.core.api.cmd.annotation.DefaultCmd
import io.github.hiztree.thebasics.core.api.cmd.sender.CommandSender
import io.github.hiztree.thebasics.core.api.data.Kit
import io.github.hiztree.thebasics.core.api.lang.LangKey
import io.github.hiztree.thebasics.core.api.user.User
import io.github.hiztree.thebasics.core.configs.KitConfig

@BasicCmd("kit", "Give yourself or another player a kit.")
class KitCmd {

    @DefaultCmd
    fun kitCmd(sender: CommandSender, @Arg("kit") kit: Kit?, @Arg("target", true) target: User?) {
        if (kit == null) {
            if (sender !is User) {
                sender.sendMsg(LangKey.KIT_LIST, KitConfig.kits.joinToString { it.name })
            } else {
                val list =
                    KitConfig.kits.filter { sender.hasPermission("thebasics.kit.${it.name}") }
                        .joinToString {
                            if (sender.canUseKit(it).seconds > 0)
                                "&r&m${it.name}&r"
                            else
                                "&r${it.name}"
                        }
                sender.sendMsg(
                    LangKey.KIT_LIST, list
                )
            }
        } else {
            if (target != null) {
                if (!sender.hasPermission("thebasics.kit.others"))
                    throw UsageException(sender, LangKey.NO_PERMISSION)

                if (target.giveKit(kit)) {
                    target.sendMsg(LangKey.KIT_GIVE_TARGET, kit.name.toLowerCase())
                    sender.sendMsg(
                        LangKey.KIT_GIVE_SENDER,
                        kit.name.toLowerCase(),
                        target.getName()
                    )
                }
            } else {
                if (sender !is User)
                    throw UsageException(sender, LangKey.USER_ERROR)

                if (sender.giveKit(kit))
                    sender.sendMsg(LangKey.KIT_GIVE_TARGET, kit.name.toLowerCase())
            }
        }
    }
}
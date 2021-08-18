package io.github.hiztree.thebasics.core.commands

import io.github.hiztree.thebasics.core.api.Kit
import io.github.hiztree.thebasics.core.api.cmd.UsageException
import io.github.hiztree.thebasics.core.api.cmd.annotation.Arg
import io.github.hiztree.thebasics.core.api.cmd.annotation.BasicCmd
import io.github.hiztree.thebasics.core.api.cmd.annotation.DefaultCmd
import io.github.hiztree.thebasics.core.api.cmd.sender.CommandSender
import io.github.hiztree.thebasics.core.api.lang.LangKey
import io.github.hiztree.thebasics.core.api.user.User

@BasicCmd("kit", "Give yourself or another player a kit.")
class KitCmd {

    @DefaultCmd
    fun kitCmd(sender: CommandSender, @Arg("kit") kit: Kit?, @Arg("target", true) user: User?) {
        if (kit == null)
            throw UsageException(sender, LangKey.INVALID_USAGE, "kit")

        if (user != null) {
            if (!sender.hasPermission("thebasics.kit.others"))
                throw UsageException(sender, LangKey.NO_PERMISSION)

            if (user.giveKit(kit)) {
                user.sendMsg(LangKey.KIT_GIVE_TARGET, kit.name.toLowerCase())
                sender.sendMsg(LangKey.KIT_GIVE_SENDER, kit.name.toLowerCase(), user.getName())
            }
        } else {
            if (sender !is User)
                throw UsageException(sender, LangKey.USER_ERROR)

            if (sender.giveKit(kit))
                sender.sendMsg(LangKey.KIT_GIVE_TARGET, kit.name.toLowerCase())
        }
    }
}
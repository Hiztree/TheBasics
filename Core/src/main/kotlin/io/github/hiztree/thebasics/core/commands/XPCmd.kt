package io.github.hiztree.thebasics.core.commands

import io.github.hiztree.thebasics.core.api.cmd.UsageException
import io.github.hiztree.thebasics.core.api.cmd.annotation.Arg
import io.github.hiztree.thebasics.core.api.cmd.annotation.BasicCmd
import io.github.hiztree.thebasics.core.api.cmd.annotation.DefaultCmd
import io.github.hiztree.thebasics.core.api.cmd.annotation.SubCmd
import io.github.hiztree.thebasics.core.api.cmd.sender.CommandSender
import io.github.hiztree.thebasics.core.api.lang.LangKey
import io.github.hiztree.thebasics.core.api.user.User

@BasicCmd("xp", "Get or modify a users xp.")
class XPCmd {

    @DefaultCmd
    fun xpCmd(sender: CommandSender, @Arg("target") user: User) {
        sender.sendMsg(LangKey.XP, user.getName(), user.getXP(), user.getXPLevel())
    }

    @SubCmd("give")
    fun xpGiveCmd(sender: CommandSender, @Arg("target") user: User, @Arg("level") lvl: Int) {
        user.setXPLevel(user.getXPLevel() + lvl)

        sender.sendMsg(LangKey.XP_GIVE_SENDER, lvl, user.getName())
        user.sendMsg(LangKey.XP_GIVE_TARGET, lvl)
    }

    @SubCmd("remove")
    fun xpRemoveCmd(sender: CommandSender, @Arg("target") user: User, @Arg("level") lvl: Int) {
        if (user.getXPLevel() <= lvl)
            throw UsageException(sender, LangKey.INVALID_LVL)

        user.setXPLevel(user.getXPLevel() - lvl)

        sender.sendMsg(LangKey.XP_RMV_SENDER, lvl, user.getName())
        user.sendMsg(LangKey.XP_RMV_TARGET, lvl)
    }
}
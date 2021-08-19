package io.github.hiztree.thebasics.core.commands

import io.github.hiztree.thebasics.core.api.cmd.UsageException
import io.github.hiztree.thebasics.core.api.cmd.annotation.Arg
import io.github.hiztree.thebasics.core.api.cmd.annotation.BasicCmd
import io.github.hiztree.thebasics.core.api.cmd.annotation.DefaultCmd
import io.github.hiztree.thebasics.core.api.cmd.sender.CommandSender
import io.github.hiztree.thebasics.core.api.lang.LangKey
import io.github.hiztree.thebasics.core.api.user.User

@BasicCmd("feed", "Feed yourself or another user.")
class FeedCmd {

    @DefaultCmd
    fun feedCmd(sender: CommandSender, @Arg("target", true) user: User?) {
        if (user != null) {
            if (!sender.hasPermission("thebasics.feed.others"))
                throw UsageException(sender, LangKey.NO_PERMISSION)

            user.setHunger(20.0)
            user.sendMsg(LangKey.FED_TARGET, sender.getName())

            sender.sendMsg(LangKey.FED_SENDER, user.getName())
        } else {
            if (sender !is User)
                throw UsageException(sender, LangKey.USER_ERROR)

            sender.setHunger(20.0)
            sender.sendMsg(LangKey.FED_SELF)
        }
    }
}
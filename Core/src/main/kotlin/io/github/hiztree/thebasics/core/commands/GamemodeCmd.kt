package io.github.hiztree.thebasics.core.commands

import io.github.hiztree.thebasics.core.api.cmd.UsageException
import io.github.hiztree.thebasics.core.api.cmd.annotation.Arg
import io.github.hiztree.thebasics.core.api.cmd.annotation.BasicCmd
import io.github.hiztree.thebasics.core.api.cmd.annotation.DefaultCmd
import io.github.hiztree.thebasics.core.api.cmd.sender.CommandSender
import io.github.hiztree.thebasics.core.api.lang.LangKey
import io.github.hiztree.thebasics.core.api.user.Gamemode
import io.github.hiztree.thebasics.core.api.user.User

@BasicCmd("gamemode|gm", "Modify a players game mode.")
class GamemodeCmd {

    @DefaultCmd
    fun gamemodeCmd(
        sender: CommandSender,
        @Arg("target", true) user: User?,
        @Arg("gamemode", true) gamemode: Gamemode?
    ) {
        if (user != null) {
            if (!sender.hasPermission("thebasics.gamemode.others"))
                throw UsageException(sender, LangKey.NO_PERMISSION)

            val mode = gamemode ?: user.getGamemode().opposite()

            user.setGamemode(mode)

            user.sendMsg(LangKey.GAMEMODE_CHANGE_TARGET, mode.name.toLowerCase())
            sender.sendMsg(LangKey.GAMEMODE_CHANGE_SENDER, user.getName(), mode.name.toLowerCase())
        } else {
            if (sender !is User)
                throw UsageException(sender, LangKey.USER_ERROR)

            val mode = gamemode ?: sender.getGamemode().opposite()

            sender.setGamemode(mode)
            sender.sendMsg(LangKey.GAMEMODE_CHANGE_TARGET, mode.name.toLowerCase())
        }
    }
}
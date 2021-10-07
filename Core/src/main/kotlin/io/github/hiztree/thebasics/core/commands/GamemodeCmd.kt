/*
 * MIT License
 *
 * Copyright (c) 2021 Levi Pawlak
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
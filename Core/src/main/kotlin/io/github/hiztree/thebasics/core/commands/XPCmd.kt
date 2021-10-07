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
import io.github.hiztree.thebasics.core.api.cmd.annotation.SubCmd
import io.github.hiztree.thebasics.core.api.cmd.sender.CommandSender
import io.github.hiztree.thebasics.core.api.lang.LangKey
import io.github.hiztree.thebasics.core.api.user.User

@BasicCmd("xp|exp|experience", "Get or modify a users xp.")
class XPCmd {

    @DefaultCmd
    fun xpCmd(sender: CommandSender, @Arg("target") user: User) {
        sender.sendMsg(LangKey.XP, user.getName(), user.getXP(), user.getXPLevel())
    }

    @SubCmd("give")
    fun xpGiveCmd(sender: CommandSender, @Arg("target") user: User, @Arg("level") lvl: Int) {
        if (lvl <= 0)
            throw UsageException(sender, LangKey.INVALID_AMOUNT)

        user.setXPLevel(user.getXPLevel() + lvl)

        sender.sendMsg(LangKey.XP_GIVE_SENDER, lvl, user.getName())
        user.sendMsg(LangKey.XP_GIVE_TARGET, lvl)
    }

    @SubCmd("remove")
    fun xpRemoveCmd(sender: CommandSender, @Arg("target") user: User, @Arg("level") lvl: Int) {
        if (user.getXPLevel() <= lvl)
            throw UsageException(sender, LangKey.INVALID_LVL)

        if (lvl <= 0)
            throw UsageException(sender, LangKey.INVALID_AMOUNT)

        user.setXPLevel(user.getXPLevel() - lvl)

        sender.sendMsg(LangKey.XP_RMV_SENDER, lvl, user.getName())
        user.sendMsg(LangKey.XP_RMV_TARGET, lvl)
    }
}
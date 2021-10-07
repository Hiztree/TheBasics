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
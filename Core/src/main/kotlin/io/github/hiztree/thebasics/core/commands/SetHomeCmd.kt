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

import io.github.hiztree.thebasics.core.api.cmd.annotation.Arg
import io.github.hiztree.thebasics.core.api.cmd.annotation.BasicCmd
import io.github.hiztree.thebasics.core.api.cmd.annotation.DefaultCmd
import io.github.hiztree.thebasics.core.api.lang.LangKey
import io.github.hiztree.thebasics.core.api.user.User
import io.github.hiztree.thebasics.core.api.user.data.Home

@BasicCmd("sethome", "Sets your current location as a home.")
class SetHomeCmd {

    @DefaultCmd
    fun setHomeCmd(sender: User, @Arg("name") name: String) {
        if (sender.homes.find { it.name.equals(name, true) } != null) {
            sender.sendMsg(LangKey.HOME_IS_EXIST_ERROR, name)
            return
        }

        sender.homes.add(Home(name, sender.getLocation(), sender.getWorld()))
        sender.save()

        sender.sendMsg(LangKey.SET_HOME, name)
    }
}
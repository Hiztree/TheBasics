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

package io.github.hiztree.thebasics.core.api

import io.github.hiztree.thebasics.core.api.cmd.sender.ConsoleSender
import io.github.hiztree.thebasics.core.api.data.World
import io.github.hiztree.thebasics.core.api.log.BasicLogger
import io.github.hiztree.thebasics.core.api.user.User
import java.io.File
import java.util.*

interface PluginContainer {

    val users: ArrayList<User>

    fun init()

    fun getUser(name: String = "", uniqueID: UUID? = null): User? {
        if (name.isEmpty() && uniqueID != null) {
            return users.firstOrNull { it.getUniqueID() == uniqueID }
        }

        return users.firstOrNull { it.getName().equals(name, true) }
    }

    fun getConsoleSender(): ConsoleSender

    fun getImplementation(): Implementation

    fun getConfigDir(): File {
        return if (getImplementation() == Implementation.BUKKIT)
            File("plugins/TheBasics")
        else
            File("config/thebasics")
    }

    fun getPlayerDir(): File {
        return if (getImplementation() == Implementation.BUKKIT)
            File("plugins/TheBasics/Players")
        else
            File("config/thebasics/players")
    }

    fun getWorld(uniqueID: UUID): World?

    fun getLog(): BasicLogger
}
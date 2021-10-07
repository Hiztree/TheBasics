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

import io.github.hiztree.thebasics.core.TheBasics
import io.github.hiztree.thebasics.core.api.cmd.UsageException
import io.github.hiztree.thebasics.core.api.cmd.annotation.BasicCmd
import io.github.hiztree.thebasics.core.api.cmd.annotation.DefaultCmd
import io.github.hiztree.thebasics.core.api.data.Location
import io.github.hiztree.thebasics.core.api.data.World
import io.github.hiztree.thebasics.core.api.lang.LangKey
import io.github.hiztree.thebasics.core.api.user.User
import java.util.*

@BasicCmd("spawn", "Teleport to the server spawn.")
class SpawnCmd {

    @DefaultCmd
    fun spawnCmd(sender: User) {
        val spawn = getSpawn()

        if (spawn.first == null || spawn.second == null)
            throw UsageException(sender, LangKey.NO_SPAWN)

        sender.teleport(spawn.first!!, spawn.second!!)
    }

    private fun getSpawn(): Pair<World?, Location?> {
        val spawn = TheBasics.instance.dataConfig["spawn"]
        val worldID = spawn.node("world").get(UUID::class.java) ?: return Pair(null, null)
        val world = TheBasics.instance.getWorld(worldID) ?: return Pair(null, null)
        val location = spawn.node("location").get(Location::class.java) ?: return Pair(null, null)

        return Pair(world, location)
    }
}
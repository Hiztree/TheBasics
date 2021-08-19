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
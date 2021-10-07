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

package io.github.hiztree.thebasics.spigot.impl

import io.github.hiztree.thebasics.core.api.data.Location
import io.github.hiztree.thebasics.core.api.data.World
import io.github.hiztree.thebasics.core.api.inventory.item.BasicItem
import io.github.hiztree.thebasics.core.api.user.Gamemode
import io.github.hiztree.thebasics.core.api.user.User
import io.github.hiztree.thebasics.spigot.toBasics
import io.github.hiztree.thebasics.spigot.toSponge
import io.github.hiztree.thebasics.spigot.translateColor
import org.spongepowered.api.data.key.Keys
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.entity.living.player.gamemode.GameModes
import java.util.*

class SpongeUser(private val base: Player) : User(base.uniqueId) {

    override fun getName(): String {
        return base.name
    }

    override fun getUniqueID(): UUID {
        return base.uniqueId
    }

    override fun sendMsg(msg: String) {
        base.sendMessage(msg.translateColor())
    }

    override fun getHealth(): Double {
        return base.health().get()
    }

    override fun setHealth(health: Double) {
        base.health().set(health)
    }

    override fun hasPermission(permission: String): Boolean {
        return base.hasPermission(permission)
    }

    override fun getHunger(): Double {
        return base.foodLevel().get().toDouble()
    }

    override fun setHunger(hunger: Double) {
        base.foodLevel().set(hunger.toInt())
    }

    override fun giveItem(item: BasicItem) {
        base.inventory.offer(item.toSponge())
    }

    override fun kick(reason: String) {
        base.kick(reason.translateColor())
    }

    override fun getXP(): Int {
        return base.get(Keys.TOTAL_EXPERIENCE).orElse(0)
    }

    override fun getXPLevel(): Int {
        return base.get(Keys.EXPERIENCE_LEVEL).orElse(1)
    }

    override fun setXP(xp: Int) {
        base.offer(Keys.TOTAL_EXPERIENCE, xp)
    }

    override fun setXPLevel(lvl: Int) {
        base.offer(Keys.EXPERIENCE_LEVEL, lvl)
    }

    override fun teleport(world: World, location: Location) {
        base.location = location.toSponge(world.toSponge())
    }

    override fun getLocation(): Location {
        return base.location.toBasics()
    }

    override fun getWorld(): World {
        return base.world.toBasics()
    }

    override fun getGamemode(): Gamemode {
        return Gamemode.getByInput(base.gameMode().get().name)!!
    }

    override fun setGamemode(gamemode: Gamemode) {
        when (gamemode) {
            Gamemode.ADVENTURE -> base.gameMode().set(GameModes.ADVENTURE)
            Gamemode.SPECTATOR -> base.gameMode().set(GameModes.SPECTATOR)
            Gamemode.SURVIVAL -> base.gameMode().set(GameModes.SURVIVAL)
            Gamemode.CREATIVE -> base.gameMode().set(GameModes.CREATIVE)
        }
    }
}
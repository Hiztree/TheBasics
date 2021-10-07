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
import io.github.hiztree.thebasics.spigot.toBukkit
import org.bukkit.ChatColor
import org.bukkit.GameMode
import org.bukkit.entity.Player
import java.util.*

class SpigotUser(private val base: Player) : User(base.uniqueId) {

    override fun getName(): String {
        return base.name
    }

    override fun getUniqueID(): UUID {
        return base.uniqueId
    }

    override fun sendMsg(msg: String) {
        base.sendMessage(ChatColor.translateAlternateColorCodes('&', msg))
    }

    override fun getHealth(): Double {
        return base.health
    }

    override fun setHealth(health: Double) {
        base.health = health
    }

    override fun hasPermission(permission: String): Boolean {
        return base.hasPermission(permission)
    }

    override fun getHunger(): Double {
        return base.foodLevel.toDouble()
    }

    override fun setHunger(hunger: Double) {
        base.foodLevel = hunger.toInt()
    }

    override fun giveItem(item: BasicItem) {
        base.inventory.addItem(item.toBukkit())
    }

    override fun kick(reason: String) {
        base.kickPlayer(reason)
    }

    override fun getXP(): Int {
        return base.exp.toInt()
    }

    override fun getXPLevel(): Int {
        return base.expToLevel
    }

    override fun setXP(xp: Int) {
        base.exp = xp.toFloat()
    }

    override fun setXPLevel(lvl: Int) {
        base.level = lvl
    }

    override fun teleport(world: World, location: Location) {
        base.teleport(location.toBukkit(world.toBukkit()))
    }

    override fun getLocation(): Location {
        return base.location.toBasics()
    }

    override fun getWorld(): World {
        return base.world.toBasics()
    }

    override fun getGamemode(): Gamemode {
        return Gamemode.getByInput(base.gameMode.name)!!
    }

    override fun setGamemode(gamemode: Gamemode) {
        base.gameMode = GameMode.valueOf(gamemode.name)
    }
}
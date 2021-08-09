package io.github.hiztree.thebasics.spigot.impl

import io.github.hiztree.thebasics.core.api.Location
import io.github.hiztree.thebasics.core.api.World
import io.github.hiztree.thebasics.core.api.inventory.item.BasicItem
import io.github.hiztree.thebasics.core.api.user.User
import io.github.hiztree.thebasics.spigot.toBasics
import io.github.hiztree.thebasics.spigot.toBukkit
import org.bukkit.ChatColor
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
}
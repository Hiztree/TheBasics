package io.github.hiztree.thebasics.spigot.impl

import io.github.hiztree.thebasics.core.TheBasics
import io.github.hiztree.thebasics.core.api.BasicTime
import io.github.hiztree.thebasics.core.api.config.BasicConfig
import io.github.hiztree.thebasics.core.api.inventory.item.BasicItem
import io.github.hiztree.thebasics.core.api.user.User
import io.github.hiztree.thebasics.spigot.toBukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import java.time.Duration
import java.time.Instant
import java.util.*

class SpigotUser(private val base: Player) : User, BasicConfig("${base.uniqueId}.conf", TheBasics.instance.getPlayerDir()) {

    private var muteEnd: Instant? = null

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

    override fun isMuted(): Boolean {
        if(muteEnd != null) {
            if(Duration.between(Instant.now(), muteEnd).isNegative) {
                muteEnd = null
                return false
            }

            return true
        }

        return false
    }

    override fun mute(duration: BasicTime, reason: String) {
        super.mute(duration, reason)

        muteEnd = duration.toInstant()
    }

    override fun save() {
        this["muteEnd"].set(muteEnd)

        super.save()
    }

    override fun serialize() {
        this.muteEnd = this["muteEnd"].get(Instant::class.java)
    }

    override fun deserialize() {
        save()
    }
}
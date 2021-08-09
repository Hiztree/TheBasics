package io.github.hiztree.thebasics.core.api.user

import io.github.hiztree.thebasics.core.TheBasics
import io.github.hiztree.thebasics.core.api.BasicTime
import io.github.hiztree.thebasics.core.api.Location
import io.github.hiztree.thebasics.core.api.World
import io.github.hiztree.thebasics.core.api.cmd.sender.CommandSender
import io.github.hiztree.thebasics.core.api.config.BasicConfig
import io.github.hiztree.thebasics.core.api.config.Serializable
import io.github.hiztree.thebasics.core.api.inventory.item.BasicItem
import io.github.hiztree.thebasics.core.api.lang.LangKey
import io.github.hiztree.thebasics.core.api.user.data.Home
import java.time.Duration
import java.time.Instant
import java.util.*

abstract class User(uniqueId: UUID) : OfflineUser, CommandSender, Serializable,
    BasicConfig("${uniqueId}.conf", TheBasics.instance.getPlayerDir()) {

    private var muteEnd: Instant? = null
    val homes: MutableList<Home> = mutableListOf()

    abstract fun getHealth(): Double
    abstract fun setHealth(health: Double)

    abstract fun getHunger(): Double
    abstract fun setHunger(hunger: Double)

    abstract fun giveItem(item: BasicItem)
    abstract fun kick(reason: String)

    fun isMuted(): Boolean {
        if (muteEnd != null) {
            if (Duration.between(Instant.now(), muteEnd).isNegative) {
                muteEnd = null
                return false
            }

            return true
        }

        return false
    }

    fun mute(duration: BasicTime, reason: String) {
        muteEnd = duration.toInstant()
        save()

        sendMsg(LangKey.MUTED, duration.toString(), reason)
    }

    abstract fun getXP(): Int

    abstract fun getXPLevel(): Int

    abstract fun setXP(xp: Int)

    abstract fun setXPLevel(lvl: Int)

    abstract fun teleport(world: World, location: Location)

    abstract fun getLocation(): Location

    abstract fun getWorld(): World

    override fun save() {
        deserialize()
        super.save()
    }

    override fun serialize() {
        this.muteEnd = this["muteEnd"].get(Instant::class.java)
        this.homes.addAll(
            this["homes"].getList(Home::class.java)?.toMutableList() ?: mutableListOf()
        )
    }

    override fun deserialize() {
        this["muteEnd"].set(muteEnd)
        this["homes"].setList(Home::class.java, homes)
    }
}
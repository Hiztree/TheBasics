package io.github.hiztree.thebasics.core.api.user

import com.google.common.collect.Maps
import io.github.hiztree.thebasics.core.TheBasics
import io.github.hiztree.thebasics.core.api.BasicTime
import io.github.hiztree.thebasics.core.api.Kit
import io.github.hiztree.thebasics.core.api.Location
import io.github.hiztree.thebasics.core.api.World
import io.github.hiztree.thebasics.core.api.cmd.sender.CommandSender
import io.github.hiztree.thebasics.core.api.config.BasicConfig
import io.github.hiztree.thebasics.core.api.config.Serializable
import io.github.hiztree.thebasics.core.api.inventory.item.BasicItem
import io.github.hiztree.thebasics.core.api.lang.LangKey
import io.github.hiztree.thebasics.core.api.user.data.Home
import io.github.hiztree.thebasics.core.pretty
import java.time.Duration
import java.time.Instant
import java.util.*
import kotlin.time.ExperimentalTime

abstract class User(uniqueId: UUID) : OfflineUser, CommandSender, Serializable,
    BasicConfig("${uniqueId}.conf", TheBasics.instance.getPlayerDir()) {

    private var muteEnd: Instant? = null
    private val usedKits: HashMap<String, Instant> = Maps.newHashMap()

    val homes: MutableList<Home> = mutableListOf()

    init {
        deserialize()
    }

    @OptIn(ExperimentalTime::class)
    fun giveKit(kit: Kit): Boolean {
        if (!hasPermission("thebasics.kit.${kit.name}")) {
            sendMsg(LangKey.KIT_PERMISSION)
            return false
        }

        if (usedKits.containsKey(kit.name)) {
            val kitEnd = usedKits[kit.name]
            val difference = Duration.between(Instant.now(), kitEnd)

            if (difference.isNegative) {
                usedKits.remove(kit.name)
            } else {
                sendMsg(LangKey.KIT_INTERVAL, difference.pretty(), kit.name)
                return false
            }
        }

        usedKits[kit.name] = Instant.now().plusSeconds(kit.interval)

        for (item in kit.items) {
            if (item != null) {
                giveItem(item)
            }
        }

        return true
    }

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

    abstract fun getHealth(): Double
    abstract fun setHealth(health: Double)

    abstract fun getHunger(): Double
    abstract fun setHunger(hunger: Double)

    abstract fun giveItem(item: BasicItem)
    abstract fun kick(reason: String)

    abstract fun getXP(): Int

    abstract fun getXPLevel(): Int

    abstract fun setXP(xp: Int)

    abstract fun setXPLevel(lvl: Int)

    abstract fun teleport(world: World, location: Location)

    abstract fun getLocation(): Location

    abstract fun getWorld(): World

    abstract fun getGamemode(): Gamemode

    abstract fun setGamemode(gamemode: Gamemode)

    override fun save() {
        serialize()
        super.save()
    }

    override fun serialize() {
        this["muteEnd"].set(muteEnd)
        this["homes"].setList(Home::class.java, homes)
        this["usedKits"].set(usedKits)
    }

    override fun deserialize() {
        this.muteEnd = this["muteEnd"].get(Instant::class.java)
        this.homes.addAll(
            this["homes"].getList(Home::class.java)?.toMutableList() ?: mutableListOf()
        )

        for ((key, value) in this["usedKits"].childrenMap().entries) {
            usedKits[key.toString()] = value.get(Instant::class.java)!!
        }
    }
}
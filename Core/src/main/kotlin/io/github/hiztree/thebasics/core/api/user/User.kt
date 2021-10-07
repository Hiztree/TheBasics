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

package io.github.hiztree.thebasics.core.api.user

import com.google.common.collect.Maps
import io.github.hiztree.thebasics.core.api.BasicTime
import io.github.hiztree.thebasics.core.api.cmd.sender.CommandSender
import io.github.hiztree.thebasics.core.api.data.Kit
import io.github.hiztree.thebasics.core.api.data.Location
import io.github.hiztree.thebasics.core.api.data.World
import io.github.hiztree.thebasics.core.api.inventory.item.BasicItem
import io.github.hiztree.thebasics.core.api.lang.LangKey
import io.github.hiztree.thebasics.core.api.lang.pretty
import io.github.hiztree.thebasics.core.api.user.data.Home
import java.time.Duration
import java.time.Instant
import java.util.*
import kotlin.time.ExperimentalTime

abstract class User(uniqueId: UUID) : OfflineUser(uniqueId), CommandSender {

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

        val diff = canUseKit(kit)

        if (diff.seconds > 0) {
            sendMsg(LangKey.KIT_INTERVAL, diff.pretty(), kit.name)
            return false
        }

        usedKits[kit.name] = Instant.now().plusSeconds(kit.interval)

        for (item in kit.items) {
            if (item != null) {
                giveItem(item)
            }
        }

        save()

        return true
    }

    fun canUseKit(kit: Kit): Duration {
        if (!usedKits.containsKey(kit.name))
            return Duration.ZERO

        val kitEnd = usedKits[kit.name]
        val difference = Duration.between(Instant.now(), kitEnd)

        if (difference.isNegative) {
            usedKits.remove(kit.name)
        } else {
            return difference
        }

        return Duration.ZERO
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

        for (usedKit in usedKits) {
            this["usedKits", usedKit.key].set(usedKit.value)
        }
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
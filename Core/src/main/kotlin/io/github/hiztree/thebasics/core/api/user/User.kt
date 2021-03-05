package io.github.hiztree.thebasics.core.api.user

import io.github.hiztree.thebasics.core.api.BasicTime
import io.github.hiztree.thebasics.core.api.cmd.sender.CommandSender
import io.github.hiztree.thebasics.core.api.config.Serializable
import io.github.hiztree.thebasics.core.api.inventory.item.BasicItem
import io.github.hiztree.thebasics.core.api.lang.LangKey

interface User : OfflineUser, CommandSender, Serializable {

    fun getHealth(): Double
    fun setHealth(health: Double)

    fun getHunger(): Double
    fun setHunger(hunger: Double)

    fun giveItem(item: BasicItem)
    fun kick(reason: String)

    fun isMuted(): Boolean

    fun mute(duration: BasicTime, reason: String) {
        sendMsg(LangKey.MUTED, duration.toString(), reason)
    }

}
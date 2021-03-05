/*
 * Copyright (C) Fallen-Network, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Levi Pawlak <levi.pawlak17@gmail.com>, November 2020
 */

package io.github.hiztree.thebasics.spigot.impl

import io.github.hiztree.thebasics.core.TheBasics
import io.github.hiztree.thebasics.core.api.inventory.item.BasicItem
import io.github.hiztree.thebasics.core.api.inventory.item.ItemTypes
import io.github.hiztree.thebasics.spigot.toBasics
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent

class BlockListener : Listener {

    @EventHandler
    fun onBreak(event: BlockBreakEvent) {
        val apiEvent = io.github.hiztree.thebasics.core.api.event.BlockBreakEvent(
                event.player.toBasics(), BasicItem(ItemTypes.getByName(event.block.type.name) ?: ItemTypes.AIR), event.block.location.toBasics())

        TheBasics.eventBus.post(apiEvent)
        event.isCancelled = apiEvent.cancel
    }

    @EventHandler
    fun onPlace(event: BlockPlaceEvent) {
        val apiEvent = io.github.hiztree.thebasics.core.api.event.BlockPlaceEvent(
                event.player.toBasics(), BasicItem(ItemTypes.getByName(event.block.type.name) ?: ItemTypes.AIR), event.block.location.toBasics())

        TheBasics.eventBus.post(apiEvent)
        event.isCancelled = apiEvent.cancel
    }
}
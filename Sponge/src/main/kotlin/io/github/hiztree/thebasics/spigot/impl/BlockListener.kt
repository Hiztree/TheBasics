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

import io.github.hiztree.thebasics.core.TheBasics
import io.github.hiztree.thebasics.core.api.inventory.item.BasicItem
import io.github.hiztree.thebasics.core.api.inventory.item.ItemTypes
import io.github.hiztree.thebasics.spigot.toBasics
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.block.ChangeBlockEvent
import org.spongepowered.api.event.filter.cause.First

class BlockListener {

    @Listener
    fun onBreak(event: ChangeBlockEvent.Break, @First player: Player) {
        val block = event.transactions.first()

        val apiEvent = io.github.hiztree.thebasics.core.api.event.BlockBreakEvent(
            player.toBasics(),
            BasicItem(ItemTypes.getByName(block.original.state.type.name) ?: ItemTypes.AIR),
            block.original.location.get().toBasics()
        )

        TheBasics.eventBus.post(apiEvent)
        event.isCancelled = apiEvent.cancel
    }

    @Listener
    fun onPlace(event: ChangeBlockEvent.Place, @First player: Player) {
        val block = event.transactions.first()


        val apiEvent = io.github.hiztree.thebasics.core.api.event.BlockPlaceEvent(
            player.toBasics(),
            BasicItem(ItemTypes.getByName(block.original.state.type.name) ?: ItemTypes.AIR),
            block.original.location.get().toBasics()
        )

        TheBasics.eventBus.post(apiEvent)
        event.isCancelled = apiEvent.cancel
    }
}
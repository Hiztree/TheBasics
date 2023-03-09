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

package io.github.hiztree.thebasics.sponge.impl

import io.github.hiztree.thebasics.core.TheBasics
import io.github.hiztree.thebasics.core.api.inventory.item.BasicItem
import io.github.hiztree.thebasics.core.api.inventory.item.ItemTypes
import io.github.hiztree.thebasics.sponge.toBasics
import org.spongepowered.api.block.transaction.Operations
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.entity.living.player.server.ServerPlayer
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.block.ChangeBlockEvent
import org.spongepowered.api.event.filter.cause.First

class BlockListener {

    @Listener
    fun blockEvent(event: ChangeBlockEvent.All, @First player: ServerPlayer) {
        for (transaction in event.transactions()) {
            transaction.operation()
        }
        val transaction = event.transactions().first()
        val block = transaction.original()

        if(transaction.operation() == Operations.BREAK) {
            val apiEvent = io.github.hiztree.thebasics.core.api.event.BlockBreakEvent(
                player.toBasics(),
                BasicItem(block.state().type().toBasics()),
                block.location().get().toBasics()
            )

            TheBasics.eventBus.post(apiEvent)
            event.isCancelled = apiEvent.cancel
        } else if(transaction.operation() == Operations.PLACE) {
            val apiEvent = io.github.hiztree.thebasics.core.api.event.BlockPlaceEvent(
                player.toBasics(),
                BasicItem(block.state().type().toBasics()),
                block.location().get().toBasics()
            )

            TheBasics.eventBus.post(apiEvent)
            event.isCancelled = apiEvent.cancel
        }
    }
}
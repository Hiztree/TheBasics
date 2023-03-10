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
import io.github.hiztree.thebasics.core.api.event.*
import io.github.hiztree.thebasics.core.api.inventory.item.extra.EnchantType
import io.github.hiztree.thebasics.core.api.inventory.item.extra.PotionType
import io.github.hiztree.thebasics.sponge.name
import io.github.hiztree.thebasics.sponge.toBasics
import io.github.hiztree.thebasics.sponge.toRaw
import org.spongepowered.api.Sponge
import org.spongepowered.api.data.Keys
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.entity.living.player.server.ServerPlayer
import org.spongepowered.api.event.action.SleepingEvent
import org.spongepowered.api.event.block.entity.BrewingEvent
import org.spongepowered.api.event.command.ExecuteCommandEvent
import org.spongepowered.api.event.entity.BreedingEvent
import org.spongepowered.api.event.entity.DamageEntityEvent
import org.spongepowered.api.event.entity.MoveEntityEvent
import org.spongepowered.api.event.entity.TameEntityEvent
import org.spongepowered.api.event.filter.cause.First
import org.spongepowered.api.event.item.inventory.ChangeInventoryEvent
import org.spongepowered.api.event.item.inventory.CraftItemEvent
import org.spongepowered.api.event.item.inventory.EnchantItemEvent
import org.spongepowered.api.event.item.inventory.InteractItemEvent
import org.spongepowered.api.event.item.inventory.container.ClickContainerEvent
import org.spongepowered.api.event.message.PlayerChatEvent
import org.spongepowered.api.event.network.ServerSideConnectionEvent
import org.spongepowered.api.item.ItemTypes
import org.spongepowered.api.item.enchantment.Enchantment
import org.spongepowered.api.item.inventory.ContainerTypes
import org.spongepowered.api.item.inventory.Inventory
import org.spongepowered.api.item.inventory.ItemStack
import org.spongepowered.api.item.inventory.slot.OutputSlot
import org.spongepowered.api.registry.RegistryTypes

class PlayerListener {

    @org.spongepowered.api.event.Listener
    fun onAnvilRepair(event: ClickContainerEvent, @First player: ServerPlayer) {
        if (event.container().type() == ContainerTypes.ANVIL) {

            event.transactions().forEach {
                if (it.slot() is OutputSlot && it.original() == event.cursorTransaction().defaultReplacement()) {
                    if (it.original().type() != ItemTypes.AIR) {
                        TheBasics.eventBus.post(AnvilRepairEvent(player.toBasics()))
                    }
                }
            }
        }
    }

    @org.spongepowered.api.event.Listener
    fun onChat(event: PlayerChatEvent.Submit, @First player: ServerPlayer) {
        val msg = event.message().toRaw()
        val basicEvent = UserChatEvent(player.toBasics(), msg)

        TheBasics.eventBus.post(basicEvent)

        event.isCancelled = basicEvent.cancel
    }

    @org.spongepowered.api.event.Listener
    fun onPreJoin(event: ServerSideConnectionEvent.Login) {
        TheBasics.eventBus.post(UserPreJoinEvent(event.profile().uniqueId()))
    }

    @org.spongepowered.api.event.Listener
    fun onJoin(event: ServerSideConnectionEvent.Join, @First player: ServerPlayer) {

        TheBasics.eventBus.post(UserJoinEvent(player.toBasics()))
    }

    @org.spongepowered.api.event.Listener
    fun onQuit(event: ServerSideConnectionEvent.Disconnect, @First player: ServerPlayer) {
        TheBasics.eventBus.post(UserQuitEvent(player.toBasics()))
    }

    @org.spongepowered.api.event.Listener
    fun onSleep(event: SleepingEvent, @First player: ServerPlayer) {
        TheBasics.eventBus.post(UserSleepEvent(player.toBasics()))
    }

    @org.spongepowered.api.event.Listener
    fun onBreed(event: BreedingEvent.Breed, @First player: ServerPlayer) {
        TheBasics.eventBus.post(
            UserBreedEvent(
                player.toBasics(),
                event.offspringEntity().type().name(RegistryTypes.ENTITY_TYPE)
            )
        )
    }

    @org.spongepowered.api.event.Listener
    fun onPlayerDie(event: DamageEntityEvent) {
        if (event.entity() is Player && event.willCauseDeath()) {
            TheBasics.eventBus.post(UserDieEvent((event.entity() as ServerPlayer).toBasics()))
        }
    }

    @org.spongepowered.api.event.Listener
    fun onPickUpItem(event: ChangeInventoryEvent.Pickup.Pre, @First player: ServerPlayer) {
        TheBasics.eventBus.post(
            UserPickupItemEvent(
                player.toBasics(),
                event.originalStack().createStack().toBasics()
            )
        )
    }

    @org.spongepowered.api.event.Listener
    fun onPlayerKillMob(event: DamageEntityEvent, @First player: ServerPlayer) {
        if (event.entity() !is Player && event.willCauseDeath()) {
            TheBasics.eventBus.post(
                UserKillMobEvent(
                    (event.entity() as ServerPlayer).toBasics(),
                    event.entity().type().name(RegistryTypes.ENTITY_TYPE)
                )
            )
        }
    }

    @org.spongepowered.api.event.Listener
    fun onPlayerMoveEvent(event: MoveEntityEvent, @First player: ServerPlayer) {
        TheBasics.eventBus.post(UserMoveEvent(player.toBasics()))
    }

    @org.spongepowered.api.event.Listener
    fun onTameEvent(event: TameEntityEvent) {
        val entity = event.entity()

        entity.get(Keys.TAMER).ifPresent { it ->
            Sponge.server().player(it).ifPresent {
                TheBasics.eventBus.post(
                    UserTameAnimalEvent(
                        it.toBasics(),
                        entity.type().name(RegistryTypes.ENTITY_TYPE)
                    )
                )
            }
        }
    }

    @org.spongepowered.api.event.Listener
    fun onItemUse(event: InteractItemEvent.Secondary, @First player: ServerPlayer) {
        TheBasics.eventBus.post(
            ItemUseEvent(
                player.toBasics(),
                event.itemStack().createStack().toBasics()
            )
        )
    }

    @org.spongepowered.api.event.Listener
    fun onBrew(event: BrewingEvent.Finish, @First player: ServerPlayer) {
        val type = event.brewedItemStacks()[0].createStack().get(Keys.POTION_TYPE)

        if (type.isPresent) {
            TheBasics.eventBus.post(
                BrewEvent(
                    player.toBasics(),
                    PotionType.getByName(type.get().name(RegistryTypes.POTION_TYPE)) ?: PotionType.EMPTY
                )
            )
        }
    }

    @org.spongepowered.api.event.Listener
    fun onCraft(event: CraftItemEvent, @First player: ServerPlayer) {
        for (transaction in event.transactions()) {
            TheBasics.eventBus.post(
                CraftEvent(
                    player.toBasics(),
                    transaction.original().createStack().toBasics()
                )
            )
        }
    }

    @org.spongepowered.api.event.Listener
    fun onEnchant(event: EnchantItemEvent.Post, @First player: ServerPlayer) {
        event.slot().get().peek().let { item: ItemStack ->
            item.get(Keys.STORED_ENCHANTMENTS)
                .ifPresent { enchantments: MutableList<Enchantment> ->
                    for (enchantment in enchantments) {
                        val type = EnchantType.getByName(enchantment.type().name(RegistryTypes.ENCHANTMENT_TYPE)) ?: continue

                        TheBasics.eventBus.post(
                            EnchantEvent(
                                player.toBasics(),
                                item.toBasics(),
                                type
                            )
                        )
                    }
                }
        }
    }

    @org.spongepowered.api.event.Listener
    fun onForge(event: ClickContainerEvent, @First player: ServerPlayer) {
        if(event.container().type() == ContainerTypes.FURNACE || event.container().type() == ContainerTypes.BLAST_FURNACE) {
            if (event.inventory() !is Inventory)
                return

            val inventory = event.inventory() as Inventory

            event.slot().ifPresent { slot ->
                slot.get(Keys.SLOT_INDEX).ifPresent { index ->
                    if (index == 2) {
                        TheBasics.eventBus.post(
                            ForgeEvent(
                                player.toBasics(),
                                event.cursorTransaction().original().createStack().toBasics()
                            )
                        )
                    }
                }
            }
        }
    }

    @org.spongepowered.api.event.Listener
    fun onCommand(event: ExecuteCommandEvent.Pre, @First player: ServerPlayer) {
        val basicEvent =
            UserChatEvent.Command(player.toBasics(), event.command() + " " + event.arguments())

        TheBasics.eventBus.post(basicEvent)

        val cmd = basicEvent.msg!!.split(" ")[0]

        event.isCancelled = basicEvent.cancel
        event.setCommand(cmd)
        event.setArguments(basicEvent.msg!!.removePrefix(cmd))
    }
}

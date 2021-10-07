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
import io.github.hiztree.thebasics.core.api.event.*
import io.github.hiztree.thebasics.core.api.inventory.item.extra.EnchantType
import io.github.hiztree.thebasics.core.api.inventory.item.extra.PotionType
import io.github.hiztree.thebasics.spigot.toBasics
import org.spongepowered.api.Sponge
import org.spongepowered.api.data.key.Keys
import org.spongepowered.api.entity.EntityType
import org.spongepowered.api.entity.EntityTypes.*
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.event.action.SleepingEvent
import org.spongepowered.api.event.block.tileentity.BrewingEvent
import org.spongepowered.api.event.command.SendCommandEvent
import org.spongepowered.api.event.entity.BreedEntityEvent
import org.spongepowered.api.event.entity.DamageEntityEvent
import org.spongepowered.api.event.entity.MoveEntityEvent
import org.spongepowered.api.event.entity.TameEntityEvent
import org.spongepowered.api.event.filter.cause.First
import org.spongepowered.api.event.item.inventory.*
import org.spongepowered.api.event.message.MessageChannelEvent
import org.spongepowered.api.event.network.ClientConnectionEvent
import org.spongepowered.api.item.ItemTypes
import org.spongepowered.api.item.enchantment.Enchantment
import org.spongepowered.api.item.inventory.InventoryArchetypes
import org.spongepowered.api.item.inventory.ItemStack
import org.spongepowered.api.item.inventory.property.SlotIndex
import org.spongepowered.api.item.inventory.slot.OutputSlot

class PlayerListener {

    @org.spongepowered.api.event.Listener
    fun onAnvilRepair(event: ClickInventoryEvent, @First player: Player) {
        if (event.targetInventory.archetype == InventoryArchetypes.ANVIL) {

            event.transactions.forEach {
                if (it.slot is OutputSlot && it.original == event.cursorTransaction.default) {
                    if (it.original.type != ItemTypes.AIR) {
                        TheBasics.eventBus.post(AnvilRepairEvent(player.toBasics()))
                    }
                }
            }
        }
    }

    @org.spongepowered.api.event.Listener
    fun onChat(event: MessageChannelEvent.Chat, @First player: Player) {
        val msg = event.rawMessage.toPlain()
        val mazoraEvent = UserChatEvent(player.toBasics(), msg)

        TheBasics.eventBus.post(mazoraEvent)

        event.isCancelled = mazoraEvent.cancel
        event.isMessageCancelled = mazoraEvent.cancel
    }

    @org.spongepowered.api.event.Listener
    fun onPreJoin(event: ClientConnectionEvent.Login) {
        TheBasics.eventBus.post(UserPreJoinEvent(event.profile.uniqueId))
    }

    @org.spongepowered.api.event.Listener
    fun onJoin(event: ClientConnectionEvent.Join, @First player: Player) {

        TheBasics.eventBus.post(UserJoinEvent(player.toBasics()))
    }

    @org.spongepowered.api.event.Listener
    fun onQuit(event: ClientConnectionEvent.Disconnect, @First player: Player) {
        TheBasics.eventBus.post(UserQuitEvent(player.toBasics()))
    }

    @org.spongepowered.api.event.Listener
    fun onSleep(event: SleepingEvent, @First player: Player) {
        TheBasics.eventBus.post(UserSleepEvent(player.toBasics()))
    }

    @org.spongepowered.api.event.Listener
    fun onBreed(event: BreedEntityEvent.Breed, @First player: Player) {
        TheBasics.eventBus.post(
            UserBreedEvent(
                player.toBasics(),
                event.offspringEntity.type.getRawID()
            )
        )
    }

    @org.spongepowered.api.event.Listener
    fun onPlayerDie(event: DamageEntityEvent) {
        if (event.targetEntity is Player && event.willCauseDeath()) {
            TheBasics.eventBus.post(UserDieEvent((event.targetEntity as Player).toBasics()))
        }
    }

    @org.spongepowered.api.event.Listener
    fun onPickUpItem(event: ChangeInventoryEvent.Pickup.Pre, @First player: Player) {
        TheBasics.eventBus.post(
            UserPickupItemEvent(
                player.toBasics(),
                event.originalStack.createStack().toBasics()
            )
        )
    }

    @org.spongepowered.api.event.Listener
    fun onPlayerKillMob(event: DamageEntityEvent, @First player: Player) {
        if (event.targetEntity !is Player && event.willCauseDeath()) {
            TheBasics.eventBus.post(
                UserKillMobEvent(
                    (event.targetEntity as Player).toBasics(),
                    event.targetEntity.type.getRawID()
                )
            )
        }
    }

    @org.spongepowered.api.event.Listener
    fun onPlayerMoveEvent(event: MoveEntityEvent, @First player: Player) {
        TheBasics.eventBus.post(UserMoveEvent(player.toBasics()))
    }

    @org.spongepowered.api.event.Listener
    fun onTameEvent(event: TameEntityEvent) {
        val entity = event.targetEntity

        entity.get(Keys.TAMED_OWNER).ifPresent { it ->
            Sponge.getServer().getPlayer(it.get()).ifPresent {
                TheBasics.eventBus.post(
                    UserTameAnimalEvent(
                        it.toBasics(),
                        entity.type.getRawID()
                    )
                )
            }
        }
    }

    @org.spongepowered.api.event.Listener
    fun onItemUse(event: InteractItemEvent.Secondary.MainHand, @First player: Player) {
        TheBasics.eventBus.post(
            ItemUseEvent(
                player.toBasics(),
                event.itemStack.createStack().toBasics()
            )
        )
    }

    @org.spongepowered.api.event.Listener
    fun onBrew(event: BrewingEvent.Finish, @First player: Player) {
        val type = event.brewedItemStacks[0].createStack().get(Keys.POTION_TYPE)

        if (type.isPresent) {
            TheBasics.eventBus.post(
                BrewEvent(
                    player.toBasics(),
                    PotionType.getByName(type.get().name) ?: PotionType.EMPTY
                )
            )
        }
    }

    @org.spongepowered.api.event.Listener
    fun onCraft(event: CraftItemEvent, @First player: Player) {
        for (transaction in event.transactions) {
            TheBasics.eventBus.post(
                CraftEvent(
                    player.toBasics(),
                    transaction.original.createStack().toBasics()
                )
            )
        }
    }

    @org.spongepowered.api.event.Listener
    fun onEnchant(event: EnchantItemEvent.Post, @First player: Player) {
        event.slot.get().peek().ifPresent { item: ItemStack ->
            item.get(Keys.ITEM_ENCHANTMENTS)
                .ifPresent { enchantments: MutableList<Enchantment> ->
                    for (enchantment in enchantments) {
                        val type = EnchantType.getByName(enchantment.type.name) ?: continue

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
    fun onForge(event: ClickInventoryEvent, @First player: Player) {
        if (event.targetInventory.archetype == InventoryArchetypes.FURNACE) {
            event.slot.ifPresent { slot ->
                slot.getInventoryProperty(SlotIndex::class.java).ifPresent { index ->
                    if (index.value == 2) {
                        TheBasics.eventBus.post(
                            ForgeEvent(
                                player.toBasics(),
                                event.cursorTransaction.original.createStack().toBasics()
                            )
                        )
                    }
                }
            }
        }
    }

    @org.spongepowered.api.event.Listener
    fun onCommand(event: SendCommandEvent, @First player: Player) {
        val mazoraEvent =
            UserChatEvent.Command(player.toBasics(), event.command + " " + event.arguments)

        TheBasics.eventBus.post(mazoraEvent)

        val cmd = mazoraEvent.msg!!.split(" ")[0]

        event.isCancelled = mazoraEvent.cancel
        event.command = cmd
        event.arguments = mazoraEvent.msg!!.removePrefix(cmd)
    }
}


private fun EntityType.getRawID(): Int {
    return when (this) {
        ITEM -> 1
        EXPERIENCE_ORB -> 2
        AREA_EFFECT_CLOUD -> 3
        ELDER_GUARDIAN -> 4
        WITHER_SKELETON -> 5
        STRAY -> 6
        EGG -> 7
        LEASH_HITCH -> 8
        PAINTING -> 9
        SNOWBALL -> 11
        FIREBALL -> 12
        ENDER_PEARL -> 14
        SPLASH_POTION -> 16
        THROWN_EXP_BOTTLE -> 17
        ITEM_FRAME -> 18
        WITHER_SKULL -> 19
        PRIMED_TNT -> 20
        FALLING_BLOCK -> 21
        FIREWORK -> 22
        HUSK -> 23
        SPECTRAL_ARROW -> 24
        SHULKER_BULLET -> 25
        DRAGON_FIREBALL -> 26
        ZOMBIE_VILLAGER -> 27
        SKELETON_HORSE -> 28
        ZOMBIE_HORSE -> 29
        ARMOR_STAND -> 30
        DONKEY -> 31
        MULE -> 32
        EVOCATION_FANGS -> 33
        EVOCATION_ILLAGER -> 34
        VEX -> 35
        VINDICATION_ILLAGER -> 36
        ILLUSION_ILLAGER -> 37
        COMMANDBLOCK_MINECART -> 40
        BOAT -> 41
        RIDEABLE_MINECART -> 42
        CHESTED_MINECART -> 43
        FURNACE_MINECART -> 44
        TNT_MINECART -> 45
        HOPPER_MINECART -> 46
        MOB_SPAWNER_MINECART -> 47
        CREEPER -> 50
        SKELETON -> 51
        SPIDER -> 52
        GIANT -> 53
        ZOMBIE -> 54
        SLIME -> 55
        GHAST -> 56
        PIG_ZOMBIE -> 57
        ENDERMAN -> 58
        CAVE_SPIDER -> 59
        SILVERFISH -> 60
        BLAZE -> 61
        MAGMA_CUBE -> 62
        ENDER_DRAGON -> 63
        WITHER -> 64
        BAT -> 65
        WITCH -> 66
        ENDERMITE -> 67
        GUARDIAN -> 68
        SHULKER -> 69
        PIG -> 90
        SHEEP -> 91
        COW -> 92
        CHICKEN -> 93
        SQUID -> 94
        WOLF -> 95
        MUSHROOM_COW -> 96
        SNOWMAN -> 97
        OCELOT -> 98
        IRON_GOLEM -> 99
        HORSE -> 100
        RABBIT -> 101
        POLAR_BEAR -> 102
        LLAMA -> 103
        LLAMA_SPIT -> 104
        PARROT -> 105
        VILLAGER -> 120
        ENDER_CRYSTAL -> 200
        else -> -1
    }
}

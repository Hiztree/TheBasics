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
import io.github.hiztree.thebasics.core.api.inventory.item.BasicItem
import io.github.hiztree.thebasics.core.api.inventory.item.extra.EnchantType
import io.github.hiztree.thebasics.core.api.inventory.item.extra.PotionType
import io.github.hiztree.thebasics.spigot.toBasics
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.enchantment.EnchantItemEvent
import org.bukkit.event.entity.*
import org.bukkit.event.inventory.*
import org.bukkit.event.inventory.BrewEvent
import org.bukkit.event.player.*
import org.bukkit.inventory.AnvilInventory
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.Potion

class PlayerListener : Listener {

    @EventHandler(priority = EventPriority.HIGH)
    fun onClick(event: InventoryClickEvent) {
        val uuid = event.whoClicked.uniqueId

        if (event.clickedInventory?.type == InventoryType.ANVIL) {
            val anvilInv = event.clickedInventory as AnvilInventory

            if (anvilInv.contents.size == 3) {
                if (anvilInv.contents[2].type != Material.AIR) {
                    TheBasics.eventBus.post(AnvilRepairEvent(TheBasics.instance.getUser(uniqueID = uuid)!!))
                }
            }
        }
    }

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        TheBasics.eventBus.post(UserJoinEvent(SpigotUser(event.player)))
    }

    @EventHandler(priority = EventPriority.LOW)
    fun onQuit(event: PlayerQuitEvent) {
        TheBasics.eventBus.post(UserQuitEvent(event.player.toBasics()))
    }

    @EventHandler
    fun onSleep(event: PlayerBedEnterEvent) {
        TheBasics.eventBus.post(UserSleepEvent(event.player.toBasics()))
    }

    @EventHandler
    fun onBreed(event: EntityBreedEvent) {
        if (event.breeder is Player) {
            val user = event.breeder as Player

            TheBasics.eventBus.post(
                UserBreedEvent(
                    user.toBasics(),
                    event.entity.type.name
                )
            )
        }
    }

    @EventHandler
    fun onUserDie(event: PlayerDeathEvent) {
        TheBasics.eventBus.post(UserDieEvent(event.entity.toBasics()))
    }

    @EventHandler
    fun onPickUpItem(event: EntityPickupItemEvent) {
        if (event.entity is Player) {
            TheBasics.eventBus.post(
                UserPickupItemEvent(
                    (event.entity as Player).toBasics(),
                    event.item.itemStack.toBasics()
                )
            )
        }
    }

    @EventHandler
    fun onUserKillMob(event: EntityDamageByEntityEvent) {
        if (event.damager is Player) {
            val user = event.damager as Player

            TheBasics.eventBus.post(UserKillMobEvent(user.toBasics(), event.entity.type.name))
        }
    }

    @EventHandler
    fun onUserMove(event: PlayerMoveEvent) {
        TheBasics.eventBus.post(UserMoveEvent(event.player.toBasics()))
    }

    @EventHandler
    fun onUserTame(event: EntityTameEvent) {
        val owner = event.owner

        if (owner is Player) {
            TheBasics.eventBus.post(UserTameAnimalEvent(owner.toBasics(), event.entity.type.name))
        }
    }

    @EventHandler
    fun onItemUse(event: EntityInteractEvent) {
        if (event.entity is Player) {
            val user = event.entity as Player

            TheBasics.eventBus.post(ItemUseEvent(user.toBasics(), user.itemInHand.toBasics()))
        }
    }

    @EventHandler
    fun onBrew(event: BrewEvent) {
        if (event.contents.viewers.isNotEmpty()) {
            if (event.contents.viewers[0] is Player) {
                val user = event.contents.viewers[0] as Player

                val stack: ItemStack = event.contents.getItem(1) ?: return
                val p: Potion = Potion.fromItemStack(stack)

                TheBasics.eventBus.post(
                    PotionType.getByName(p.type.name)?.let {
                        io.github.hiztree.thebasics.core.api.event.BrewEvent(
                            user.toBasics(),
                            it
                        )
                    })
            }
        }
    }

    @EventHandler
    fun onCraft(event: CraftItemEvent) {
        if (event.inventory.viewers.isNotEmpty() && event.inventory.result != null) {
            if (event.inventory.viewers[0] is Player) {
                val user = event.inventory.viewers[0] as Player

                TheBasics.eventBus.post(
                    CraftEvent(
                        user.toBasics(),
                        event.inventory.result!!.toBasics()
                    )
                )
            }
        }
    }

    @EventHandler
    fun onEnchant(event: EnchantItemEvent) {
        for (key in event.enchantsToAdd.keys) {
            if (key == null)
                continue

            val type = EnchantType.getByName(key.name) ?: continue

            TheBasics.eventBus.post(
                EnchantEvent(
                    event.enchanter.toBasics(),
                    event.item.toBasics(),
                    type
                )
            )
        }
    }

    @EventHandler
    fun onForge(event: FurnaceExtractEvent) {
        TheBasics.eventBus.post(
            ForgeEvent(
                event.player.toBasics(),
                BasicItem(event.itemType.toBasics(), event.itemAmount)
            )
        )
    }

    @EventHandler
    fun onChat(event: AsyncPlayerChatEvent) {
        val chatEvent = UserChatEvent(event.player.toBasics(), event.message)
        TheBasics.eventBus.post(chatEvent)

        if (chatEvent.msg == null)
            event.isCancelled = true
        else
            event.message = chatEvent.msg!!
    }
}

/*
 * Copyright (C) Fallen-Network, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Levi Pawlak <levi.pawlak17@gmail.com>, November 2020
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

    /*
    @EventHandler
    fun onClose(event: InventoryCloseEvent) {
        val uuid = event.player.uniqueId

        if (BukkitInventoryAPI.openPages.containsKey(uuid)) {
            val page = BukkitInventoryAPI.openPages[uuid] ?: return
            page.onClose(TheBasics.instance.getUserWrapper(uuid)!!)

            BukkitInventoryAPI.openPages.remove(uuid)
        }
    }*/

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        TheBasics.eventBus.post(UserJoinEvent(SpigotUser(event.player)))
    }

    @EventHandler
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

            TheBasics.eventBus.post(UserBreedEvent(user.toBasics(), event.entity.type.typeId.toInt()))
        }
    }

    @EventHandler
    fun onUserDie(event: PlayerDeathEvent) {
        TheBasics.eventBus.post(UserDieEvent(event.entity.toBasics()))
    }

    @EventHandler
    fun onPickUpItem(event: EntityPickupItemEvent) {
        if (event.entity is Player) {
            TheBasics.eventBus.post(UserPickupItemEvent((event.entity as Player).toBasics(), event.item.itemStack.toBasics()))
        }
    }

    @EventHandler
    fun onUserKillMob(event: EntityDamageByEntityEvent) {
        if (event.damager is Player) {
            val user = event.damager as Player

            TheBasics.eventBus.post(UserKillMobEvent(user.toBasics(), event.entity.entityId))
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
            TheBasics.eventBus.post(UserTameAnimalEvent(owner.toBasics(), event.entity.entityId))
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

                TheBasics.eventBus.post(PotionType.getByName(p.type.name)?.let { io.github.hiztree.thebasics.core.api.event.BrewEvent(user.toBasics(), it) })
            }
        }
    }

    @EventHandler
    fun onCraft(event: CraftItemEvent) {
        if (event.inventory.viewers.isNotEmpty() && event.inventory.result != null) {
            if (event.inventory.viewers[0] is Player) {
                val user = event.inventory.viewers[0] as Player

                TheBasics.eventBus.post(CraftEvent(user.toBasics(), event.inventory.result!!.toBasics()))
            }
        }
    }

    @EventHandler
    fun onEnchant(event: EnchantItemEvent) {
        for (key in event.enchantsToAdd.keys) {
            if (key == null)
                continue

            val type = EnchantType.getByName(key.name) ?: continue

            TheBasics.eventBus.post(EnchantEvent(event.enchanter.toBasics(), event.item.toBasics(), type))
        }
    }

    @EventHandler
    fun onForge(event: FurnaceExtractEvent) {
        TheBasics.eventBus.post(ForgeEvent(event.player.toBasics(), BasicItem(event.itemType.toBasics(), event.itemAmount)))
    }

    @EventHandler
    fun onChat(event: AsyncPlayerChatEvent) {
        val chatEvent = UserChatEvent(event.player.toBasics(), event.message)
        TheBasics.eventBus.post(chatEvent)

        if(chatEvent.msg == null)
            event.isCancelled = true
        else
            event.message = chatEvent.msg!!
    }
}

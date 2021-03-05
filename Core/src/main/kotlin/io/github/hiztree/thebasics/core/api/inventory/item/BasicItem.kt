/*
 * Copyright (C) Fallen-Network, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Levi Pawlak <levi.pawlak17@gmail.com>, November 2020
 */

package io.github.hiztree.thebasics.core.api.inventory.item

import com.google.common.collect.Lists
import com.google.common.collect.Maps
import io.github.hiztree.thebasics.core.api.inventory.item.extra.DyeColor
import io.github.hiztree.thebasics.core.api.inventory.item.extra.EnchantType
import io.github.hiztree.thebasics.core.api.inventory.item.extra.Enchantment
import java.util.*

class BasicItem(val itemType: ItemType, var qty: Int = 1, var name: String = "", var lore: List<String> = Lists.newArrayList(), val nbt: HashMap<String, String> = Maps.newHashMap()) {

    var dyeColor: DyeColor? = null
    var enchantments: ArrayList<Enchantment> = Lists.newArrayList()

    fun copy(): BasicItem {
        val item = BasicItem(itemType, qty, name, lore, nbt)

        if (dyeColor != null) {
            item.dyeColor = dyeColor
        }

        return item
    }

    companion object {
        fun createUserHead(name: String): BasicItem {
            val itemStack = BasicItem(ItemTypes.SKULL, 1)
            itemStack.nbt["SkullOwner"] = name

            return itemStack
        }

        //Format: <itemType> [qty] [enchant] [lore] [name]
        fun parseItem(type: ItemType, args: Array<out String>) : BasicItem? {
            if(args.isEmpty())
                return null

            var qty = 1
            var displayName = ""
            var lore = ""
            val enchantments = Lists.newArrayList<Enchantment>()

            for(arg in args) {
                if(arg.toIntOrNull() != null) {
                    qty = arg.toInt()
                } else if(arg.startsWith("lore:")) {
                    lore = arg.removePrefix("lore:").replace("_", " ").replace("|", "\n")
                } else if(arg.contains("name:")) {
                    displayName = arg.removePrefix("name:")
                } else {
                    val split = arg.split(":")

                    if(split.size > 1) {
                        val enchantment = EnchantType.getByName(split[0]) ?: continue
                        val lvl = split[1].toIntOrNull() ?: continue

                        enchantments.add(Enchantment(enchantment, lvl))
                    }
                }
            }

            val item = BasicItem(type, qty, displayName, lore.split("\n").toList())
            item.enchantments.addAll(enchantments)

            return item
        }
    }
}
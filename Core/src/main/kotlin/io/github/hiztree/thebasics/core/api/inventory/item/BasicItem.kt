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

package io.github.hiztree.thebasics.core.api.inventory.item

import com.google.common.collect.Lists
import com.google.common.collect.Maps
import io.github.hiztree.thebasics.core.api.inventory.item.extra.DyeColor
import io.github.hiztree.thebasics.core.api.inventory.item.extra.EnchantType
import io.github.hiztree.thebasics.core.api.inventory.item.extra.Enchantment

class BasicItem(
    val itemType: ItemType,
    var qty: Int = 1,
    var name: String = "",
    var lore: List<String> = Lists.newArrayList(),
    val nbt: HashMap<String, String> = Maps.newHashMap()
) {

    var dyeColor: DyeColor? = null
    var enchantments: ArrayList<Enchantment> = Lists.newArrayList()

    fun copy(): BasicItem {
        val item = BasicItem(itemType, qty, name, lore, nbt)

        if (dyeColor != null) {
            item.dyeColor = dyeColor
        }

        return item
    }

    override fun toString(): String {
        val sb = StringBuilder()

        sb.append(itemType.name).append(" ")
            .append(qty).append(" ")


        val loreSb = StringBuilder()

        if (lore.isNotEmpty()) {
            loreSb.append("lore:")

            for (s in lore) {
                loreSb.append(s.replace(" ", "_")).append("|")
            }

            sb.append(loreSb.toString().removeSuffix("|")).append(" ")
        }

        if (name.isNotBlank())
            sb.append("name:").append(name.replace(" ", "_")).append(" ")

        for (enchantment in enchantments) {
            sb.append("${enchantment.type.id}:${enchantment.level}").append(" ")
        }

        return sb.toString().removeSuffix(" ")
    }

    companion object {
        fun createUserHead(name: String): BasicItem {
            val itemStack = BasicItem(ItemTypes.SKULL, 1)
            itemStack.nbt["SkullOwner"] = name

            return itemStack
        }

        //Format: <itemType> [qty] [enchant] [lore] [name]
        fun parseItem(type: ItemType, args: Array<out String>): BasicItem? {
            if (args.isEmpty())
                return BasicItem(type, 1)

            var qty = 1
            var displayName = ""
            var lore = ""
            val enchantments = Lists.newArrayList<Enchantment>()

            for (arg in args) {
                if (arg.toIntOrNull() != null) {
                    qty = arg.toInt()
                } else if (arg.startsWith("lore:")) {
                    lore = arg.removePrefix("lore:").replace("_", " ").replace("|", "\n")
                } else if (arg.contains("name:")) {
                    displayName = arg.removePrefix("name:")
                } else {
                    val split = arg.split(":")

                    if (split.size > 1) {
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
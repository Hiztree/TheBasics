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

package io.github.hiztree.thebasics.core.api.cmd

import com.google.common.collect.Lists
import com.google.common.reflect.TypeToken
import io.github.hiztree.thebasics.core.TheBasics
import io.github.hiztree.thebasics.core.api.BasicTime
import io.github.hiztree.thebasics.core.api.cmd.sender.CommandSender
import io.github.hiztree.thebasics.core.api.data.Kit
import io.github.hiztree.thebasics.core.api.data.Warp
import io.github.hiztree.thebasics.core.api.inventory.item.ItemType
import io.github.hiztree.thebasics.core.api.inventory.item.ItemTypes
import io.github.hiztree.thebasics.core.api.inventory.item.extra.EnchantType
import io.github.hiztree.thebasics.core.api.inventory.item.extra.PotionType
import io.github.hiztree.thebasics.core.api.user.Gamemode
import io.github.hiztree.thebasics.core.api.user.User
import io.github.hiztree.thebasics.core.api.user.data.Home
import io.github.hiztree.thebasics.core.commands.WarpCmd
import io.github.hiztree.thebasics.core.configs.KitConfig

object CommandContexts {

    fun registerJVMContexts() {
        TheBasics.registerCommandContext(object :
            CommandContext<Integer>(TypeToken.of(java.lang.Integer::class.java)) {
            override fun complete(sender: CommandSender, input: String): Integer {
                return try {
                    Integer(input)
                } catch (e: Exception) {
                    throw CommandException("number")
                }
            }
        })

        TheBasics.registerCommandContext(object :
            CommandContext<java.lang.Double>(TypeToken.of(java.lang.Double::class.java)) {
            override fun complete(sender: CommandSender, input: String): java.lang.Double {
                return try {
                    java.lang.Double(input)
                } catch (e: Exception) {
                    throw CommandException("number")
                }
            }
        })
    }

    fun registerKotlinContexts() {
        TheBasics.registerCommandContext(object :
            CommandContext<String>(TypeToken.of(String::class.java)) {
            override fun complete(sender: CommandSender, input: String): String {
                return input
            }
        })

        TheBasics.registerCommandContext(object :
            CommandContext<Int>(TypeToken.of(Int::class.java)) {
            override fun complete(sender: CommandSender, input: String): Int {
                return input.toIntOrNull() ?: throw CommandException("number")
            }
        })

        TheBasics.registerCommandContext(object :
            CommandContext<Double>(TypeToken.of(Double::class.java)) {
            override fun complete(sender: CommandSender, input: String): Double {
                return input.toDoubleOrNull() ?: throw CommandException("number")
            }
        })

        TheBasics.registerCommandContext(object :
            CommandContext<Boolean>(TypeToken.of(Boolean::class.java)) {
            override fun complete(sender: CommandSender, input: String): Boolean {
                return input.toBoolean()
            }
        })
    }

    fun registerBasicContexts() {
        TheBasics.registerCommandContext(object : CommandContext<User>(BasicTokens.USER_TOKEN) {
            override fun complete(sender: CommandSender, input: String): User {
                return TheBasics.instance.getUser(input) ?: throw CommandException("user")
            }

            override fun tab(sender: CommandSender, last: String): List<String> {
                val senderUser: User? = if (sender is User) sender else null
                val matched = Lists.newArrayList<String>()

                for (user in TheBasics.instance.users) {
                    val name: String = user.getName()

                    if ((senderUser != null && user != senderUser) && user.getName()
                            .startsWith(last)
                    ) {
                        matched.add(name)
                    }
                }

                return matched
            }
        })

        TheBasics.registerCommandContext(object :
            CommandContext<ItemType>(BasicTokens.ITEM_TYPE_TOKEN) {
            override fun complete(sender: CommandSender, input: String): ItemType {
                return ItemTypes.getByName(input) ?: throw CommandException("item type")
            }

            override fun tab(sender: CommandSender, last: String): List<String> {
                val matched = Lists.newArrayList<String>()

                for (item in ItemTypes.VALUES) {
                    if (item.name.startsWith(last)) {
                        matched.add(item.name)
                    }
                }

                return matched
            }
        })

        TheBasics.registerCommandContext(object :
            CommandContext<EnchantType>(BasicTokens.ENCHANT_TYPE_TOKEN) {
            override fun complete(sender: CommandSender, input: String): EnchantType {
                return EnchantType.getByName(input) ?: throw CommandException("enchantment")
            }
        })

        TheBasics.registerCommandContext(object :
            CommandContext<PotionType>(BasicTokens.POTION_TYPE_TOKEN) {
            override fun complete(sender: CommandSender, input: String): PotionType {
                return PotionType.getByName(input) ?: throw CommandException("enchantment")
            }
        })

        TheBasics.registerCommandContext(object :
            CommandContext<JoinedString>(BasicTokens.JOINED_STRING_TOKEN) {
            override fun complete(sender: CommandSender, input: String): JoinedString {
                return JoinedString.empty
            }
        })

        TheBasics.registerCommandContext(object :
            CommandContext<BasicTime>(BasicTokens.TIME_TOKEN) {
            override fun complete(sender: CommandSender, input: String): BasicTime {
                val duration = BasicTime.parseTime(input)

                if (duration.isZero())
                    throw CommandException("duration")

                return duration
            }
        })

        TheBasics.registerCommandContext(object : CommandContext<Home>(BasicTokens.HOME_TOKEN) {
            override fun complete(sender: CommandSender, input: String): Home {
                if (sender is User) {
                    for (home in sender.homes) {
                        if (home.name.equals(input, true))
                            return home
                    }
                }

                throw CommandException("home")
            }

            override fun tab(sender: CommandSender, last: String): List<String> {
                if (sender !is User)
                    return emptyList()

                val matched = Lists.newArrayList<String>()

                for (home in sender.homes) {
                    if (home.name.startsWith(last)) {
                        matched.add(home.name)
                    }
                }

                return matched
            }
        })


        TheBasics.registerCommandContext(object :
            CommandContext<Gamemode>(BasicTokens.GAMEMODE_TOKEN) {
            override fun complete(sender: CommandSender, input: String): Gamemode {
                for (value in Gamemode.values()) {
                    if (value.name.equals(input, true)) {
                        return value
                    }
                }

                throw CommandException("gamemode")
            }

            override fun tab(sender: CommandSender, last: String): List<String> {
                val matched = Lists.newArrayList<String>()

                for (mode in Gamemode.values()) {
                    val name = mode.name.toLowerCase()

                    if (name.startsWith(last)) {
                        matched.add(name)
                    }
                }

                return matched
            }
        })

        TheBasics.registerCommandContext(object :
            CommandContext<Kit>(BasicTokens.KIT_TOKEN) {
            override fun complete(sender: CommandSender, input: String): Kit {
                for (value in KitConfig.kits) {
                    if (value.name.equals(
                            input,
                            true
                        ) && sender.hasPermission("thebasics.kit.${value.name}")
                    ) {
                        return value
                    }
                }

                throw CommandException("kit")
            }

            override fun tab(sender: CommandSender, last: String): List<String> {
                val matched = Lists.newArrayList<String>()

                for (kit in KitConfig.kits) {
                    val name = kit.name.toLowerCase()

                    if (name.startsWith(last)) {
                        if (sender.hasPermission("thebasics.kit.$name"))
                            matched.add(name)
                    }
                }

                return matched
            }
        })

        TheBasics.registerCommandContext(object :
            CommandContext<Warp>(BasicTokens.WARP_TOKEN) {
            override fun complete(sender: CommandSender, input: String): Warp {
                for (value in WarpCmd.warps) {
                    if (value.name.equals(
                            input,
                            true
                        ) && sender.hasPermission("thebasics.warp.${value.name}")
                    ) {
                        return value
                    }
                }

                throw CommandException("warp")
            }

            override fun tab(sender: CommandSender, last: String): List<String> {
                val matched = Lists.newArrayList<String>()

                for (kit in WarpCmd.warps) {
                    val name = kit.name.toLowerCase()

                    if (name.startsWith(last)) {
                        if (sender.hasPermission("thebasics.warp.$name"))
                            matched.add(name)
                    }
                }

                return matched
            }
        })
    }
}
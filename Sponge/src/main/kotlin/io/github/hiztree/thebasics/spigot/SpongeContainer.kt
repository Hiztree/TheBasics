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

package io.github.hiztree.thebasics.spigot

import com.google.common.collect.Lists
import com.google.inject.Inject
import io.github.hiztree.thebasics.core.TheBasics
import io.github.hiztree.thebasics.core.api.Implementation
import io.github.hiztree.thebasics.core.api.cmd.CommandSpec
import io.github.hiztree.thebasics.core.api.cmd.UsageException
import io.github.hiztree.thebasics.core.api.cmd.sender.ConsoleSender
import io.github.hiztree.thebasics.core.api.data.World
import io.github.hiztree.thebasics.core.api.inventory.item.BasicItem
import io.github.hiztree.thebasics.core.api.inventory.item.ItemType
import io.github.hiztree.thebasics.core.api.inventory.item.ItemTypes
import io.github.hiztree.thebasics.core.api.inventory.item.extra.EnchantType
import io.github.hiztree.thebasics.core.api.log.BasicLogger
import io.github.hiztree.thebasics.core.api.user.User
import io.github.hiztree.thebasics.spigot.impl.*
import org.slf4j.Logger
import org.spongepowered.api.Sponge
import org.spongepowered.api.command.CommandCallable
import org.spongepowered.api.command.CommandResult
import org.spongepowered.api.command.CommandSource
import org.spongepowered.api.command.source.ConsoleSource
import org.spongepowered.api.data.key.Keys
import org.spongepowered.api.data.type.SkullTypes
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.Order
import org.spongepowered.api.event.filter.cause.First
import org.spongepowered.api.event.game.state.GameStartingServerEvent
import org.spongepowered.api.event.network.ClientConnectionEvent
import org.spongepowered.api.item.enchantment.Enchantment
import org.spongepowered.api.item.enchantment.EnchantmentType
import org.spongepowered.api.item.inventory.ItemStack
import org.spongepowered.api.plugin.Plugin
import org.spongepowered.api.text.Text
import org.spongepowered.api.text.serializer.TextSerializers
import org.spongepowered.api.world.Location
import java.lang.reflect.InvocationTargetException
import java.util.*

@Plugin(
    id = "thebasics",
    name = "TheBasics",
    version = "0.0.1",
    authors = ["Hiztree"],
    description = "A set of basic functions for Sponge servers."
)
class SpongeContainer {

    @Inject
    private lateinit var log: Logger

    private val core = object : TheBasics() {
        val logger = SpongeLogger(log)
        val onlineUsers = Lists.newArrayList<User>()
        private val consoleSender = SpongeConsoleSender(Sponge.getServer().console)

        override val users: ArrayList<User>
            get() = onlineUsers

        override fun getConsoleSender(): ConsoleSender {
            return consoleSender
        }

        override fun getImplementation(): Implementation {
            return Implementation.SPONGE
        }

        override fun getWorld(uniqueID: UUID): World? {
            val spongeWorld = Sponge.getServer().getWorld(uniqueID).orElse(null) ?: return null

            return SpongeWorld(spongeWorld)
        }

        override fun getLog(): BasicLogger {
            return logger
        }
    }

    @Listener
    fun onStart(event: GameStartingServerEvent) {
        core.init()

        Sponge.getEventManager().unregisterListeners(PlayerListener())
        Sponge.getEventManager().unregisterListeners(BlockListener())

        for (command in core.commands) {
            registerCommand(command)
        }
    }

    @Listener(order = Order.EARLY)
    fun onJoin(event: ClientConnectionEvent.Join, @First player: Player) {
        core.onlineUsers.add(SpongeUser(player))
    }

    @Listener(order = Order.EARLY)
    fun onQuit(event: ClientConnectionEvent.Disconnect, @First player: Player) {
        core.onlineUsers.remove(player.toBasics())
    }

    private fun registerCommand(spec: CommandSpec) {
        try {
            val command = object : CommandCallable {
                override fun process(source: CommandSource, arguments: String): CommandResult {
                    val args = arguments.split(" ").toTypedArray()

                    try {
                        if (source is Player)
                            core.getUser(source.name)?.let { spec.performCmd(it, args) }
                        else if (source is ConsoleSource)
                            spec.performCmd(core.getConsoleSender(), args)
                    } catch (e: Exception) {
                        when (e) {
                            !is UsageException, !is InvocationTargetException -> {
                                source.sendMessage("&cThere was an error performing the command.".translateColor())
                            }
                        }
                    }

                    return CommandResult.empty()
                }

                override fun getSuggestions(
                    source: CommandSource,
                    arguments: String,
                    targetPosition: Location<org.spongepowered.api.world.World>?
                ): MutableList<String> {
                    val args = arguments.split(" ").toTypedArray()

                    return if (source is Player) {
                        spec.tabComplete(source.toBasics(), args)
                    } else {
                        spec.tabComplete(source.toBasics(), args)
                    }
                }

                override fun testPermission(source: CommandSource): Boolean {
                    return source.hasPermission("thebasics.${spec.label}")
                }

                override fun getShortDescription(source: CommandSource): Optional<Text> {
                    return Optional.empty()
                }

                override fun getHelp(source: CommandSource): Optional<Text> {
                    return Optional.empty()
                }

                override fun getUsage(source: CommandSource): Text {
                    return spec.usage.translateColor()
                }
            }

            Sponge.getCommandManager()
                .register(this, command, arrayListOf(spec.label, *spec.aliases.toTypedArray()))
        } catch (e: Exception) {
            log.error("Could not register command: ${spec.label}! Please contact plugin author.")
        }
    }
}

fun CommandSource.toBasics(): io.github.hiztree.thebasics.core.api.cmd.sender.CommandSender {
    return if (this is User) {
        TheBasics.instance.getUser(this.name)!!
    } else {
        TheBasics.instance.getConsoleSender()
    }
}

fun Player.toBasics(): User {
    return TheBasics.instance.getUser(this.name)!!
}

fun Location<*>.toBasics(): io.github.hiztree.thebasics.core.api.data.Location {
    return io.github.hiztree.thebasics.core.api.data.Location(
        this.x,
        this.y,
        this.z,
        0.0,
        0.0
    )
}

fun World.toSponge(): org.spongepowered.api.world.World {
    return Sponge.getServer().getWorld(this.uniqueID).get()
}

fun org.spongepowered.api.world.World.toBasics(): World {
    return SpongeWorld(this)
}

fun io.github.hiztree.thebasics.core.api.data.Location.toSponge(world: org.spongepowered.api.world.World): Location<org.spongepowered.api.world.World> {
    return Location(world, this.x, this.y, this.z)
}

fun Text.toRaw(): String {
    return TextSerializers.FORMATTING_CODE.serialize(this)
}

fun ItemStack.toBasics(): BasicItem {
    val displayOpt = this.get(Keys.DISPLAY_NAME)
    val loreOpt = this.get(Keys.ITEM_LORE)

    val lore = if (loreOpt.isPresent) {
        loreOpt.get().map { it.toRaw() }
    } else {
        emptyList()
    }

    val item = if (displayOpt.isPresent) {
        BasicItem(this.type.toBasics(), this.quantity, displayOpt.get().toRaw(), lore)
    } else {
        BasicItem(this.type.toBasics(), this.quantity, lore = lore)
    }

    for (enchantment in this.get(Keys.ITEM_ENCHANTMENTS).orElse(emptyList())) {
        val type = EnchantType.getByName(enchantment.type.id)

        if (type != null)
            item.enchantments.add(
                io.github.hiztree.thebasics.core.api.inventory.item.extra.Enchantment(
                    type,
                    enchantment.level
                )
            )
    }

    return item
}

fun org.spongepowered.api.item.ItemType.toBasics(): ItemType {
    return ItemTypes.getByName(this.name) ?: ItemTypes.AIR
}

fun BasicItem.toSponge(): ItemStack {
    lateinit var item: ItemStack

    if (this.nbt.isNotEmpty()) {
        if (this.nbt.containsKey("SkullOwner")) {
            item = ItemStack.of(org.spongepowered.api.item.ItemTypes.SKULL, 1)
            item.offer(Keys.SKULL_TYPE, SkullTypes.PLAYER)
            item.offer(
                Keys.REPRESENTED_PLAYER,
                Sponge.getServer().gameProfileManager.get(this.nbt["SkullOwner"]).get()
            )
        }
    } else {
        val type = Sponge.getRegistry()
            .getType(org.spongepowered.api.item.ItemType::class.java, this.itemType.name)
            .orElseGet {
                Sponge.getRegistry()
                    .getType(org.spongepowered.api.item.ItemType::class.java, this.itemType.altID)
                    .orElseGet {
                        Sponge.getRegistry().getType(
                            org.spongepowered.api.item.ItemType::class.java,
                            this.itemType.actualModID
                        ).orElse(
                            org.spongepowered.api.item.ItemTypes.AIR
                        )
                    }
            }

        item = ItemStack.of(type, this.qty)
    }


    if (this.name.isNotEmpty())
        item.offer(Keys.DISPLAY_NAME, this.name.translateColor())

    if (this.lore.isNotEmpty()) {
        val textLore = Lists.newArrayList<Text>()

        for (s in this.lore) {
            textLore.add(s.translateColor())
        }

        item.offer(Keys.ITEM_LORE, textLore)
    }


    val enchantments = item.get(Keys.ITEM_ENCHANTMENTS).orElse(emptyList())

    for (enchantment in this.enchantments) {
        val spongeEnchantment = enchantment.toSponge() ?: continue

        enchantments.add(spongeEnchantment)
    }

    if (enchantments.isNotEmpty())
        item.offer(Keys.ITEM_ENCHANTMENTS, enchantments)

    return item
}

fun io.github.hiztree.thebasics.core.api.inventory.item.extra.Enchantment.toSponge(): Enchantment? {
    for (value in Sponge.getRegistry().getAllForSponge(EnchantmentType::class.java)) {
        if (value.id.equals(this.type.id, true))
            return Enchantment.of(value, this.level)
    }

    return null
}

fun String.translateColor(): Text = TextSerializers.FORMATTING_CODE.deserialize(this)
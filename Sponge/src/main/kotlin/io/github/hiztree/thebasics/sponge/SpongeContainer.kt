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

package io.github.hiztree.thebasics.sponge

import com.google.common.collect.Lists
import com.google.common.reflect.ClassPath
import com.google.inject.Inject
import io.github.hiztree.thebasics.core.TheBasics
import io.github.hiztree.thebasics.core.api.Implementation
import io.github.hiztree.thebasics.core.api.Loader
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
import io.github.hiztree.thebasics.sponge.impl.*
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.apache.logging.log4j.Logger
import org.spongepowered.api.ResourceKey
import org.spongepowered.api.Server
import org.spongepowered.api.Sponge
import org.spongepowered.api.SystemSubject
import org.spongepowered.api.block.BlockType
import org.spongepowered.api.command.Command
import org.spongepowered.api.command.CommandCause
import org.spongepowered.api.command.CommandCompletion
import org.spongepowered.api.command.CommandResult
import org.spongepowered.api.command.parameter.ArgumentReader
import org.spongepowered.api.data.Keys
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.entity.living.player.server.ServerPlayer
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.Order
import org.spongepowered.api.event.filter.cause.First
import org.spongepowered.api.event.lifecycle.ConstructPluginEvent
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent
import org.spongepowered.api.event.lifecycle.StartingEngineEvent
import org.spongepowered.api.event.network.ServerSideConnectionEvent
import org.spongepowered.api.item.enchantment.Enchantment
import org.spongepowered.api.item.enchantment.EnchantmentType
import org.spongepowered.api.item.inventory.ItemStack
import org.spongepowered.api.registry.RegistryType
import org.spongepowered.api.registry.RegistryTypes
import org.spongepowered.api.service.permission.Subject
import org.spongepowered.api.world.Location
import org.spongepowered.api.world.server.ServerLocation
import org.spongepowered.api.world.server.ServerWorld
import org.spongepowered.plugin.PluginContainer
import org.spongepowered.plugin.builtin.jvm.Plugin
import java.lang.reflect.InvocationTargetException
import java.util.*

@Plugin("thebasics")
class SpongeContainer @Inject internal constructor(private val container: PluginContainer, private val logger: Logger) {

    private val core = object : TheBasics() {
        private val basicLog = SpongeLogger(logger)
        private val onlineUsers: ArrayList<User> = Lists.newArrayList<User>()

        private val consoleSender = SpongeConsoleSender(Sponge.systemSubject())

        override val users: ArrayList<User>
            get() = onlineUsers

        override fun getConsoleSender(): ConsoleSender {
            return consoleSender
        }

        override fun getImplementation(): Implementation {
            return Implementation.SPONGE
        }

        override fun getWorld(uniqueID: UUID): World {
            val spongeWorld = Sponge.server().worldManager().world(Sponge.server().worldManager().worldKey(uniqueID).get()).get()

            return SpongeWorld(spongeWorld)
        }

        override fun getLog(): BasicLogger {
            return basicLog
        }
    }

    @Listener
    fun onConstruct(event: ConstructPluginEvent) {
        forceInit(SpongeContainer::class.java)

        core.init()

        println("Command: ${core.commands.size}")
    }

    @Listener
    fun onStart(event: StartingEngineEvent<Server>) {
        Sponge.eventManager().registerListeners(container, PlayerListener())
        Sponge.eventManager().registerListeners(container, BlockListener())
    }

    @Listener
    fun registerEvent(event: RegisterCommandEvent<Command.Raw>) {
        println("Register ME: Size ${core.commands.size}")

        for (command in core.commands) {
            println("Registered: ${command.label}")
            val result = event.register(container, createCommand(command), command.label, *command.aliases.toTypedArray())
        }
    }

    @Listener(order = Order.EARLY)
    fun onJoin(event: ServerSideConnectionEvent.Join, @First player: ServerPlayer) {
        core.users.add(SpongeUser(player))
    }

    @Listener(order = Order.EARLY)
    fun onQuit(event: ServerSideConnectionEvent.Disconnect, @First player: ServerPlayer) {
        core.users.remove(player.toBasics())
    }

    private fun createCommand(spec: CommandSpec) : Command.Raw? {
        try {
            val command = object : Command.Raw {
                override fun process(cause: CommandCause, arguments: ArgumentReader.Mutable): CommandResult {
                    val args = arguments.input().split(" ").toTypedArray()
                    val source = cause.subject() ?: return CommandResult.success()

                    try {

                        if (source is Player)
                            core.getUser(source.name())?.let { spec.performCmd(it, args) }
                        else if (source is SystemSubject)
                            spec.performCmd(core.getConsoleSender(), args)
                    } catch (e: Exception) {
                        when (e) {
                            !is UsageException, !is InvocationTargetException -> {
                                source.toBasics().sendMsg("&cThere was an error performing the command.")
                            }
                        }
                    }

                    return CommandResult.success()
                }

                override fun complete(
                    cause: CommandCause,
                    arguments: ArgumentReader.Mutable
                ): MutableList<CommandCompletion> {
                    val args = arguments.input().split(" ").toTypedArray()
                    val source = cause.subject()

                    return if (source is ServerPlayer) {
                        spec.tabComplete(source.toBasics(), args).map { CommandCompletion.of(it) }.toMutableList()
                    } else {
                        spec.tabComplete(source.toBasics(), args).map { CommandCompletion.of(it) }.toMutableList()
                    }
                }

                override fun canExecute(cause: CommandCause): Boolean {
                    return cause.subject().hasPermission("thebasics.${spec.label}")
                }

                override fun shortDescription(cause: CommandCause): Optional<Component> {
                    return Optional.empty()
                }

                override fun extendedDescription(cause: CommandCause): Optional<Component> {
                    return Optional.empty()
                }

                override fun usage(cause: CommandCause): Component {
                    return spec.usage.translateColor()
                }
            }

            return command
        } catch (e: Exception) {
            logger.error("Could not register command: ${spec.label}! Please contact plugin author.")
        }

        return null
    }
}

fun Subject.toBasics(): io.github.hiztree.thebasics.core.api.cmd.sender.CommandSender {
    return if (this is User) {
        TheBasics.instance.getUser(this.getName())!!
    } else {
        TheBasics.instance.getConsoleSender()
    }
}

fun ServerPlayer.toBasics(): User {
    return TheBasics.instance.getUser(this.name())!!
}

fun Location<*, *>.toBasics(): io.github.hiztree.thebasics.core.api.data.Location {
    return io.github.hiztree.thebasics.core.api.data.Location(
        this.x(),
        this.y(),
        this.z(),
        0.0,
        0.0
    )
}

fun World.toSponge(): ServerWorld {
    return Sponge.server().worldManager().world(Sponge.server().worldManager().worldKey(this.uniqueID).get()).get()
}

fun ServerWorld.toBasics(): World {
    return SpongeWorld(this)
}

fun io.github.hiztree.thebasics.core.api.data.Location.toSponge(world: ServerWorld): ServerLocation {
    return ServerLocation.of(world, this.x, this.y, this.z)
}

fun Component.toRaw(): String {
    return LegacyComponentSerializer.builder().build().serialize(this)
}

fun BlockType.toBasics(): ItemType {
    return ItemTypes.getByName(RegistryTypes.BLOCK_TYPE.get().findValueKey(this).get().formatted()) ?: ItemTypes.AIR
}

fun ItemStack.toBasics(): BasicItem {
    val displayOpt = this.get(Keys.DISPLAY_NAME)
    val loreOpt = this.get(Keys.LORE)

    val lore = if (loreOpt.isPresent) {
        loreOpt.get().map { it.toRaw() }
    } else {
        emptyList()
    }

    val item = if (displayOpt.isPresent) {
        BasicItem(this.type().toBasics(), this.quantity(), displayOpt.get().toRaw(), lore)
    } else {
        BasicItem(this.type().toBasics(), this.quantity(), lore = lore)
    }

    for (enchantment in this.get(Keys.STORED_ENCHANTMENTS).orElse(emptyList())) {
        val type = EnchantType.getByName(RegistryTypes.ENCHANTMENT_TYPE.get().findValueKey(enchantment.type()).get().formatted())

        if (type != null)
            item.enchantments.add(
                io.github.hiztree.thebasics.core.api.inventory.item.extra.Enchantment(
                    type,
                    enchantment.level()
                )
            )
    }

    return item
}

fun org.spongepowered.api.item.ItemType.toBasics(): ItemType {
    return ItemTypes.getByName(this.toString()) ?: ItemTypes.AIR
}

fun BasicItem.toSponge(): ItemStack {
    lateinit var item: ItemStack

    if (this.nbt.isNotEmpty()) {
        if (this.nbt.containsKey("SkullOwner")) {
            item = ItemStack.of(org.spongepowered.api.item.ItemTypes.PLAYER_HEAD, 1)
            item.offer(
                Keys.GAME_PROFILE,
                Sponge.server().gameProfileManager().profile(this.nbt["SkullOwner"]).get()
            )
        }
    } else {
        val registry = Sponge.server().registry(RegistryTypes.ITEM_TYPE)
        val type = registry.findValue<org.spongepowered.api.item.ItemType>(ResourceKey.resolve(this.itemType.name)).orElseGet {
            registry.findValue<org.spongepowered.api.item.ItemType>(ResourceKey.resolve(this.itemType.actualModID)).orElse(org.spongepowered.api.item.ItemTypes.AIR.get())
        }

        item = ItemStack.of(type, this.qty)
    }


    if (this.name.isNotEmpty())
        item.offer(Keys.DISPLAY_NAME, this.name.translateColor())

    if (this.lore.isNotEmpty()) {
        val textLore = Lists.newArrayList<Component>()

        for (s in this.lore) {
            textLore.add(s.translateColor())
        }

        item.offer(Keys.LORE, textLore)
    }


    val enchantments = item.get(Keys.STORED_ENCHANTMENTS).orElse(emptyList())

    for (enchantment in this.enchantments) {
        val spongeEnchantment = enchantment.toSponge() ?: continue

        enchantments.add(spongeEnchantment)
    }

    if (enchantments.isNotEmpty())
        item.offer(Keys.STORED_ENCHANTMENTS, enchantments)

    return item
}

fun io.github.hiztree.thebasics.core.api.inventory.item.extra.Enchantment.toSponge(): Enchantment? {
    val type = Sponge.server().registry(RegistryTypes.ENCHANTMENT_TYPE).findValue<EnchantmentType>(ResourceKey.resolve(this.type.id)).orElse(null) ?: return null

    return Enchantment.of(type, this.level)
}

fun String.translateColor(): Component = LegacyComponentSerializer.legacyAmpersand().deserialize(this)

fun <T> T.name(type: RegistryType<T>) : String {
    return Sponge.server().registry(type).findValueKey(this).get().formatted() ?: ""
}

fun <T> forceInit(klass: Class<T>): Class<T> {
    try {
        Class.forName(klass.name, true, klass.classLoader)
    } catch (e: ClassNotFoundException) {
        throw AssertionError(e)
    }
    return klass
}
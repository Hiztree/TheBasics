package io.github.hiztree.thebasics.spigot

import com.google.common.collect.Lists
import io.github.hiztree.thebasics.core.TheBasics
import io.github.hiztree.thebasics.core.api.Implementation
import io.github.hiztree.thebasics.core.api.cmd.CommandSpec
import io.github.hiztree.thebasics.core.api.cmd.UsageException
import io.github.hiztree.thebasics.core.api.cmd.sender.ConsoleSender
import io.github.hiztree.thebasics.core.api.inventory.item.BasicItem
import io.github.hiztree.thebasics.core.api.inventory.item.ItemType
import io.github.hiztree.thebasics.core.api.inventory.item.ItemTypes
import io.github.hiztree.thebasics.core.api.inventory.item.extra.EnchantType
import io.github.hiztree.thebasics.core.api.user.User
import io.github.hiztree.thebasics.spigot.impl.PlayerListener
import io.github.hiztree.thebasics.spigot.impl.SpigotConsoleSender
import io.github.hiztree.thebasics.spigot.impl.SpigotUser
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandMap
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.plugin.java.JavaPlugin
import java.lang.reflect.Field
import java.util.*

class SpigotContainer : JavaPlugin(), Listener {

    private val core = object : TheBasics() {
        val onlineUsers = Lists.newArrayList<User>()
        private val consoleSender = SpigotConsoleSender(Bukkit.getConsoleSender())

        override val users: ArrayList<User>
            get() = onlineUsers

        override fun getConsoleSender(): ConsoleSender {
            return consoleSender
        }

        override fun getImplementation(): Implementation {
            return Implementation.BUKKIT
        }
    }

    private lateinit var commandMap: CommandMap

    override fun onEnable() {
        core.init()

        val mapField: Field = Bukkit.getServer().javaClass.getDeclaredField("commandMap")
        mapField.isAccessible = true

        commandMap =  mapField.get(Bukkit.getServer()) as CommandMap

        for (command in core.commands) {
            registerCommand(command)
        }

        Bukkit.getPluginManager().registerEvents(PlayerListener(), this)
        Bukkit.getPluginManager().registerEvents(this, this)
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onJoin(event: PlayerJoinEvent) {
        core.onlineUsers.add(SpigotUser(event.player))
    }

    @EventHandler(priority = EventPriority.HIGH)
    fun onQuit(event: PlayerQuitEvent) {
        core.onlineUsers.remove(event.player.toBasics())
    }

    private fun registerCommand(spec: CommandSpec) {
        try {
            val command = object: Command(spec.label, spec.desc, spec.usage, emptyList()) {
                override fun execute(sender: CommandSender, commandLabel: String, args: Array<out String>): Boolean {
                    try {
                        if (sender is Player)
                            core.getUser(sender.name)?.let { spec.performCmd(it, args) }
                        else if (sender is ConsoleCommandSender)
                            spec.performCmd(core.getConsoleSender(), args)
                    } catch (e: Exception) {
                        when(e) {
                            !is UsageException -> {
                                sender.sendMessage("${ChatColor.RED}There was an error performing the command.")
                                println(e)
                            }
                        }
                    }

                    return true
                }

                override fun tabComplete(sender: CommandSender, alias: String, args: Array<out String>): MutableList<String> {
                    return spec.tabComplete(sender.toBasics(), args)
                }
            }

            commandMap.register(spec.label, "thebasics", command)
        } catch (e: Exception) {
            //TODO Error handling
        }
    }
}

fun CommandSender.toBasics() : io.github.hiztree.thebasics.core.api.cmd.sender.CommandSender {
    return if(this is User) {
        TheBasics.instance.getUser(this.name)!!
    } else {
        TheBasics.instance.getConsoleSender()
    }
}

fun Player.toBasics(): User {
    return TheBasics.instance.getUser(this.name)!!
}

fun Location.toBasics(): io.github.hiztree.thebasics.core.api.Location {
    return io.github.hiztree.thebasics.core.api.Location(this.x, this.y, this.z)
}

fun ItemStack.toBasics(): BasicItem {
    val meta = this.itemMeta
    val item = if(meta != null) {
        BasicItem(this.type.toBasics(), this.amount, meta.displayName, meta.lore ?: emptyList())
    } else {
        BasicItem(this.type.toBasics(), this.amount)
    }

    for (enchantment in this.enchantments) {
        val type = EnchantType.getByName(enchantment.key.name)

        if(type != null)
            item.enchantments.add(io.github.hiztree.thebasics.core.api.inventory.item.extra.Enchantment(type, enchantment.value))
    }

    return item
}

fun Material.toBasics(): ItemType {
    return ItemTypes.getByName(this.name) ?: ItemTypes.AIR
}

fun BasicItem.toBukkit() : ItemStack {
    lateinit var item: ItemStack

    if (this.nbt.isNotEmpty()) {
        if (this.nbt.containsKey("SkullOwner")) {
            item = ItemStack(Material.PLAYER_HEAD, 1, 0, 3)
            val skullMeta = item.itemMeta as SkullMeta
            skullMeta.owner = this.nbt["SkullOwner"]

            item.itemMeta = skullMeta
        }
    } else {

        val material = Material.matchMaterial(this.itemType.name)
                ?: Material.matchMaterial(this.itemType.altID)
                ?: Material.matchMaterial(this.itemType.actualModID) ?: Material.AIR

        item = ItemStack(material, this.qty)
    }

    val meta = item.itemMeta

    if (meta != null) {
        if(this.name.isNotEmpty())
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', this.name))

        if(this.lore.isNotEmpty()) {
            val textLore = Lists.newArrayList<String>()

            for (s in this.lore) {
                textLore.add(ChatColor.translateAlternateColorCodes('&', s))
            }

            meta.lore = textLore
        }

        item.itemMeta = meta
    }

    for (enchantment in this.enchantments) {
        val bukkitEnchant = enchantment.toBukkit() ?: continue

        item.addEnchantment(bukkitEnchant, enchantment.level)
    }

    return item
}

fun io.github.hiztree.thebasics.core.api.inventory.item.extra.Enchantment.toBukkit() : Enchantment? {
    for (value in Enchantment.values()) {
        if(value.key.key.equals(this.type.id, true))
            return value
    }

    return null
}
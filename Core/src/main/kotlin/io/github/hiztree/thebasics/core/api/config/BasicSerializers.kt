package io.github.hiztree.thebasics.core.api.config

import io.github.hiztree.thebasics.core.TheBasics
import io.github.hiztree.thebasics.core.api.data.Kit
import io.github.hiztree.thebasics.core.api.data.Location
import io.github.hiztree.thebasics.core.api.data.Warp
import io.github.hiztree.thebasics.core.api.inventory.item.BasicItem
import io.github.hiztree.thebasics.core.api.inventory.item.ItemTypes
import io.github.hiztree.thebasics.core.api.user.data.Home
import org.spongepowered.configurate.ConfigurationNode
import org.spongepowered.configurate.serialize.TypeSerializer
import org.spongepowered.configurate.serialize.TypeSerializerCollection
import java.lang.reflect.Type
import java.time.Instant
import java.util.*

object BasicSerializers {

    lateinit var serializers: TypeSerializerCollection

    fun register() {
        serializers = TypeSerializerCollection.builder()
            .register(Instant::class.java, object : TypeSerializer<Instant> {
                override fun deserialize(type: Type, node: ConfigurationNode): Instant {
                    return Instant.ofEpochMilli(node.long)
                }

                override fun serialize(type: Type, obj: Instant?, node: ConfigurationNode) {
                    node.set(obj?.toEpochMilli())
                }
            }).register(Home::class.java, object : TypeSerializer<Home> {
                override fun deserialize(type: Type?, node: ConfigurationNode): Home? {
                    val name = node.node("name").string ?: return null
                    val location = node.node("location").get(Location::class.java) ?: return null
                    val worldID = node.node("world").string ?: return null

                    val world = TheBasics.instance.getWorld(UUID.fromString(worldID)) ?: return null

                    return Home(name, location, world)
                }

                override fun serialize(type: Type?, obj: Home?, node: ConfigurationNode) {
                    if (obj == null) {
                        node.set(null)
                    } else {
                        node.node("name").set(obj.name)
                        node.node("location").set(obj.location)
                        node.node("world").set(obj.world.uniqueID)
                    }
                }
            }).register(Location::class.java, object : TypeSerializer<Location> {
                override fun deserialize(type: Type?, node: ConfigurationNode): Location? {
                    val x = node.node("x").double
                    val y = node.node("y").double
                    val z = node.node("z").double
                    val yaw = node.node("yaw").double
                    val pitch = node.node("pitch").double

                    return Location(x, y, z, yaw, pitch)
                }

                override fun serialize(type: Type?, obj: Location?, node: ConfigurationNode) {
                    if (obj == null) {
                        node.set(null)
                    } else {
                        node.node("x").set(obj.x)
                        node.node("y").set(obj.y)
                        node.node("z").set(obj.z)
                        node.node("yaw").set(obj.yaw)
                        node.node("pitch").set(obj.pitch)
                    }
                }
            }).register(Kit::class.java, object : TypeSerializer<Kit> {
                override fun deserialize(type: Type?, node: ConfigurationNode): Kit {
                    val name = node.node("name").string ?: return Kit("", -1, emptyList(), false)
                    val interval = node.node("interval").long
                    val items = node.node("items").getList(BasicItem::class.java) ?: emptyList()

                    return Kit(name, interval, items)
                }

                override fun serialize(type: Type?, obj: Kit?, node: ConfigurationNode) {
                    if (obj == null) {
                        node.set(null)
                    } else {
                        node.node("name").set(obj.name)
                        node.node("interval").set(obj.interval)
                        node.node("items").setList(BasicItem::class.java, obj.items)
                    }
                }
            }).register(BasicItem::class.java, object : TypeSerializer<BasicItem> {
                override fun deserialize(type: Type?, node: ConfigurationNode): BasicItem? {
                    val input = node.string ?: return null
                    val split = input.split(" ")
                    val itemType = ItemTypes.getByName(split[0]) ?: return null

                    return BasicItem.parseItem(itemType, split.drop(1).toTypedArray())
                }

                override fun serialize(type: Type?, obj: BasicItem?, node: ConfigurationNode) {
                    if (obj == null) {
                        node.set(null)
                    } else {
                        node.set(obj.toString())
                    }
                }
            }).register(Warp::class.java, object : TypeSerializer<Warp> {
                override fun deserialize(type: Type?, node: ConfigurationNode): Warp {
                    val name = node.node("name").string ?: return Warp("", null, null, false)
                    val worldID = node.node("world").get(UUID::class.java) ?: return Warp(
                        name,
                        null,
                        null,
                        false
                    )
                    val world =
                        TheBasics.instance.getWorld(worldID) ?: return Warp(name, null, null, false)
                    val location = node.node("location").get(Location::class.java) ?: return Warp(
                        name,
                        world,
                        null,
                        false
                    )


                    return Warp(name, world, location)
                }

                override fun serialize(type: Type?, obj: Warp?, node: ConfigurationNode) {
                    if (obj == null) {
                        node.set(null)
                    } else {
                        node.node("name").set(obj.name)
                        node.node("world").set(obj.world?.uniqueID)
                        node.node("location").set(obj.location)
                    }
                }
            }).build()
    }
}
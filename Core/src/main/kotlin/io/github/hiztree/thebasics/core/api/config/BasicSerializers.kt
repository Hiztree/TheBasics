package io.github.hiztree.thebasics.core.api.config

import io.github.hiztree.thebasics.core.TheBasics
import io.github.hiztree.thebasics.core.api.Location
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

                return Location(x, y, z)
            }

            override fun serialize(type: Type?, obj: Location?, node: ConfigurationNode) {
                if (obj == null) {
                    node.set(null)
                } else {
                    node.node("x").set(obj.x)
                    node.node("y").set(obj.y)
                    node.node("z").set(obj.z)
                }
            }
        }).build()
    }
}
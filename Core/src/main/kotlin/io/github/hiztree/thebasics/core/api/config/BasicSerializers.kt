package io.github.hiztree.thebasics.core.api.config

import org.spongepowered.configurate.ConfigurationNode
import org.spongepowered.configurate.serialize.TypeSerializer
import org.spongepowered.configurate.serialize.TypeSerializerCollection
import java.lang.reflect.Type
import java.time.Instant

object BasicSerializers {

    lateinit var serializers: TypeSerializerCollection

    fun register() {
        serializers = TypeSerializerCollection.builder().register(Instant::class.java, object : TypeSerializer<Instant> {
            override fun deserialize(type: Type, node: ConfigurationNode): Instant {
                return Instant.ofEpochMilli(node.long)
            }

            override fun serialize(type: Type, obj: Instant?, node: ConfigurationNode) {
                node.set(obj?.toEpochMilli())
            }
        }).build()
    }
}
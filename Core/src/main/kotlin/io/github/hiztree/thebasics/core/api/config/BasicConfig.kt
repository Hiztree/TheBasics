/*
 * Copyright (C) Fallen-Network, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Levi Pawlak <levi.pawlak17@gmail.com>, November 2020
 */

package io.github.hiztree.thebasics.core.api.config

import com.google.common.collect.Lists
import com.google.common.io.Files
import io.github.hiztree.thebasics.core.TheBasics
import org.spongepowered.configurate.CommentedConfigurationNode
import org.spongepowered.configurate.ConfigurationNode
import org.spongepowered.configurate.ConfigurationOptions
import org.spongepowered.configurate.hocon.HoconConfigurationLoader
import org.spongepowered.configurate.serialize.TypeSerializerCollection
import java.io.File
import java.net.URL
import java.net.URLConnection

/**
 * Creates a new config base off the name and directory.
 */
open class BasicConfig(
    name: String,
    dir: File = TheBasics.instance.getConfigDir(),
    classLoader: ClassLoader = TheBasics::class.java.classLoader,
    hasAsset: Boolean = false
) {

    private val file: File = File(dir, name)
    private var loader: HoconConfigurationLoader
    private var rootNode: CommentedConfigurationNode

    init {
        if (!file.exists()) {
            file.createNewFile()

            if (hasAsset) {
                val inputUrl: URL = classLoader.getResource(name)!!
                val connection: URLConnection = inputUrl.openConnection()
                connection.useCaches = false
                Files.write(connection.getInputStream().readBytes(), file)
            }
        }

        loader = HoconConfigurationLoader.builder()
            .file(file)
            .defaultOptions { opts: ConfigurationOptions ->
                opts.shouldCopyDefaults(true)
                    .serializers { build: TypeSerializerCollection.Builder ->
                        build.registerAll(BasicSerializers.serializers)
                    }
            }.prettyPrinting(true)
            .build()

        rootNode = loader.load()
    }


    /**
     * Gets the root node of the config.
     */
    open fun getRootNode(): CommentedConfigurationNode {
        return rootNode
    }

    operator fun get(vararg path: String): CommentedConfigurationNode {
        return rootNode.node(*path)
    }

    /**
     * Saves the config.
     */
    open fun save() {
        loader.save(rootNode)
    }
}

/**
 * Gets a list of strings.
 */
fun ConfigurationNode.getStringList(): List<String> {
    return this.getList(String::class.java, Lists.newArrayList())
}
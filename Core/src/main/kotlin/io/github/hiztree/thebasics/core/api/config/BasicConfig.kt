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
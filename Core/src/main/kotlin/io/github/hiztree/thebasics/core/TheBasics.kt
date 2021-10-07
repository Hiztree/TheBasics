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

package io.github.hiztree.thebasics.core

import com.google.common.collect.Lists
import com.google.common.eventbus.EventBus
import com.google.common.reflect.ClassPath
import com.google.common.reflect.TypeToken
import io.github.hiztree.thebasics.core.api.Loader
import io.github.hiztree.thebasics.core.api.PluginContainer
import io.github.hiztree.thebasics.core.api.cmd.CommandContext
import io.github.hiztree.thebasics.core.api.cmd.CommandContexts
import io.github.hiztree.thebasics.core.api.cmd.CommandSpec
import io.github.hiztree.thebasics.core.api.config.BasicConfig
import io.github.hiztree.thebasics.core.api.config.BasicSerializers
import io.github.hiztree.thebasics.core.api.lang.LangKey

abstract class TheBasics : PluginContainer {

    val commands: ArrayList<CommandSpec> = Lists.newArrayList()

    lateinit var generalConfig: BasicConfig
    lateinit var langConfig: BasicConfig
    lateinit var kitConfig: BasicConfig
    lateinit var dataConfig: BasicConfig

    companion object {
        lateinit var instance: TheBasics
        private val commandContexts = Lists.newArrayList<CommandContext<*>>()
        val eventBus = EventBus("thebasics")

        fun registerCommandContext(context: CommandContext<*>) {
            commandContexts.add(context)
        }

        fun <T> getCommandContext(type: TypeToken<T>): CommandContext<T>? {
            return commandContexts.firstOrNull { it.type == type } as CommandContext<T>?
        }
    }

    override fun init() {
        instance = this

        CommandContexts.registerJVMContexts()
        CommandContexts.registerKotlinContexts()
        CommandContexts.registerBasicContexts()

        BasicSerializers.register()

        if (!getConfigDir().exists())
            getConfigDir().mkdirs()

        if (!getPlayerDir().exists())
            getPlayerDir().mkdirs()

        generalConfig = BasicConfig("general.conf")
        langConfig = BasicConfig("lang.conf")
        kitConfig = BasicConfig("kit.conf")
        dataConfig = BasicConfig("data.conf")

        val registeredClasses = ClassPath.from(TheBasics::class.java.classLoader)
            .getTopLevelClassesRecursive("io.github.hiztree.thebasics.core")

        //Apply the loaders to all of the modules.
        for (loadClasses in ClassPath.from(TheBasics::class.java.classLoader)
            .getTopLevelClasses("io.github.hiztree.thebasics.core.loader")) {
            val loadedClass = loadClasses.load().newInstance()

            if (loadedClass is Loader) {
                loadedClass.load(registeredClasses)
            }
        }

        LangKey.load()
    }
}
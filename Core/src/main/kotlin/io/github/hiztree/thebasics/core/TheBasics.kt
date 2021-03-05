package io.github.hiztree.thebasics.core

import com.google.common.collect.Lists
import com.google.common.eventbus.EventBus
import com.google.common.reflect.ClassPath
import com.google.common.reflect.TypeToken
import io.github.hiztree.thebasics.core.api.PluginContainer
import io.github.hiztree.thebasics.core.api.Loader
import io.github.hiztree.thebasics.core.api.cmd.CommandContext
import io.github.hiztree.thebasics.core.api.cmd.CommandContexts
import io.github.hiztree.thebasics.core.api.cmd.CommandSpec
import io.github.hiztree.thebasics.core.api.config.BasicConfig
import io.github.hiztree.thebasics.core.api.lang.LangKey
import java.util.*

abstract class TheBasics : PluginContainer {

    val commands: ArrayList<CommandSpec> = Lists.newArrayList()

    lateinit var generalConfig: BasicConfig
    lateinit var langConfig: BasicConfig

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

        if(!getConfigDir().exists())
            getConfigDir().mkdirs()

        generalConfig = BasicConfig("general.conf")
        langConfig = BasicConfig("lang.conf")

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
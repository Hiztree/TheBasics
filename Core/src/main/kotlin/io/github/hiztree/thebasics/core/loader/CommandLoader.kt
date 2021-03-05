/*
 * Copyright (C) Fallen-Network, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Levi Pawlak <levi.pawlak17@gmail.com>, November 2020
 */

package io.github.hiztree.thebasics.core.loader

import com.google.common.collect.ImmutableSet
import com.google.common.reflect.ClassPath
import io.github.hiztree.thebasics.core.TheBasics
import io.github.hiztree.thebasics.core.api.Loader
import io.github.hiztree.thebasics.core.api.cmd.CommandSpec
import io.github.hiztree.thebasics.core.api.cmd.annotation.BasicCmd
import io.github.hiztree.thebasics.core.api.cmd.annotation.DefaultCmd
import io.github.hiztree.thebasics.core.api.cmd.annotation.SubCmd
import io.github.hiztree.thebasics.core.api.cmd.sender.CommandSender
import java.lang.reflect.Method
import java.lang.reflect.Parameter

class CommandLoader : Loader() {

    override fun load(set: ImmutableSet<ClassPath.ClassInfo>): Boolean {
        for (info in set.stream().filter { info: ClassPath.ClassInfo ->
            info.load().isAnnotationPresent(BasicCmd::class.java)
        }) {
            try {
                val loaded = info.load()
                val instance = loaded.newInstance()

                val basicCmd = loaded.getAnnotation(BasicCmd::class.java)

                val defaultMethod = loaded.declaredMethods.firstOrNull { it.isAnnotationPresent(DefaultCmd::class.java) }
                        ?: continue

                val defaultCmd = loadCmdSpec(basicCmd.label, basicCmd.desc, "", instance, defaultMethod) ?: continue

                for (declaredMethod in loaded.declaredMethods) {
                    if (declaredMethod.isAnnotationPresent(SubCmd::class.java)) {
                        val subCmd = declaredMethod.getAnnotation(SubCmd::class.java)

                        loadCmdSpec(subCmd.label,basicCmd.desc, defaultCmd.label, instance, declaredMethod)?.let { defaultCmd.subCommands.add(it) }
                    }
                }

                TheBasics.instance.commands.add(defaultCmd)
            } catch (ex: ReflectiveOperationException) {
                ex.printStackTrace()
                continue
            }
        }

        return true
    }

    private fun loadCmdSpec(label: String, desc: String, parentLabel: String, instance: Any, method: Method): CommandSpec? {
        val sender: Parameter = method.parameters.firstOrNull {
            CommandSender::class.java.isAssignableFrom(it.type)
        } ?: return null

        return CommandSpec(label, desc, parentLabel, sender.type as Class<out CommandSender>, instance, method)
    }

}
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

                val defaultMethod =
                    loaded.declaredMethods.firstOrNull { it.isAnnotationPresent(DefaultCmd::class.java) }
                        ?: continue

                val defaultCmd =
                    loadCmdSpec(basicCmd.label, basicCmd.desc, "", instance, defaultMethod)
                        ?: continue

                for (declaredMethod in loaded.declaredMethods) {
                    if (declaredMethod.isAnnotationPresent(SubCmd::class.java)) {
                        val subCmd = declaredMethod.getAnnotation(SubCmd::class.java)

                        loadCmdSpec(
                            subCmd.label,
                            basicCmd.desc,
                            defaultCmd.label,
                            instance,
                            declaredMethod
                        )?.let { defaultCmd.subCommands.add(it) }
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

    private fun loadCmdSpec(
        label: String,
        desc: String,
        parentLabel: String,
        instance: Any,
        method: Method
    ): CommandSpec? {
        val sender: Parameter = method.parameters.firstOrNull {
            CommandSender::class.java.isAssignableFrom(it.type)
        } ?: return null

        var actualLabel = label

        val aliases = if (label.contains("|")) {
            val split = label.split("|")
            actualLabel = split[0]
            label.split("|").drop(1)
        } else {
            emptyList()
        }

        return CommandSpec(
            actualLabel,
            aliases,
            desc,
            parentLabel,
            sender.type as Class<out CommandSender>,
            instance,
            method
        )
    }

}
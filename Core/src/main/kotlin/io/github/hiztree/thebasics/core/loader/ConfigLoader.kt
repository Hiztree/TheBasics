/*
 * Copyright (C) Fallen-Network, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Levi Pawlak <levi.pawlak17@gmail.com>, November 2020
 */

package io.github.hiztree.thebasics.core.loader

import com.google.common.collect.ImmutableSet
import com.google.common.reflect.ClassPath
import io.github.hiztree.thebasics.core.api.Loader
import io.github.hiztree.thebasics.core.api.config.annotation.Section
import io.github.hiztree.thebasics.core.api.config.annotation.Setting

class ConfigLoader : Loader() {

    override fun load(set: ImmutableSet<ClassPath.ClassInfo>): Boolean {
        for (info in set.stream().filter { info: ClassPath.ClassInfo ->
            val loaded = info.load()

            loaded.isAnnotationPresent(Section::class.java)
        }) {
            try {
                val loaded: Class<*> = info.load()
                val section = loaded.getAnnotation(Section::class.java)
                val parentNode = section.type.getConfig().getRootNode()
                val instance = loaded.newInstance()
                var change = false

                for (declaredField in loaded.declaredFields) {
                    if (declaredField.isAnnotationPresent(Setting::class.java)) {
                        val setting = declaredField.getAnnotation(Setting::class.java)
                        val path = if (setting.path.isEmpty()) declaredField.name.toLowerCase() else setting.path
                        val node = parentNode.node(path)

                        declaredField.isAccessible = true

                        if (node.virtual()) {
                            node.set(declaredField.get(instance))
                            change = true
                        } else {
                            declaredField.set(instance, node.get(declaredField.genericType))
                        }

                        if (node.comment() == null && setting.comment.isNotEmpty()) {
                            node.comment(setting.comment)
                            change = true
                        }
                    }
                }

                if (change)
                    section.type.getConfig().save()
            } catch (ignore: ReflectiveOperationException) {
                ignore.printStackTrace()
                continue
            }
        }

        return true
    }
}
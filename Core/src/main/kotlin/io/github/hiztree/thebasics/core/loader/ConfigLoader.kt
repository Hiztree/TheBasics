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
import ninja.leaping.configurate.objectmapping.ObjectMapper
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable

class ConfigLoader : Loader() {

    override fun load(set: ImmutableSet<ClassPath.ClassInfo>): Boolean {
        for (info in set.stream().filter { info: ClassPath.ClassInfo ->
            val loaded = info.load()

            loaded.isAnnotationPresent(ConfigSerializable::class.java) && loaded.isAnnotationPresent(Section::class.java)
        }) {
            try {
                val loaded = info.load()
                val section = loaded.getAnnotation(Section::class.java)

                ObjectMapper.forClass(loaded).bindToNew().populate(section.type.getConfig().getRootNode())
                section.type.getConfig().save()
            } catch (ignore: ReflectiveOperationException) {
                continue
            }
        }

        return true
    }
}
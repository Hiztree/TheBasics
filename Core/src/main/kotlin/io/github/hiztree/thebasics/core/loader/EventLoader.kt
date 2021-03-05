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
import io.github.hiztree.thebasics.core.api.event.annotation.Listener

class EventLoader : Loader() {

    override fun load(set: ImmutableSet<ClassPath.ClassInfo>): Boolean {
        for (info in set.stream().filter { info: ClassPath.ClassInfo ->
            info.load().isAnnotationPresent(Listener::class.java)
        }) {
            try {
                val loaded = info.load()

                TheBasics.eventBus.register(loaded.newInstance())
            } catch (ex: Exception) {
                //TODO
            }
        }

        return false
    }
}
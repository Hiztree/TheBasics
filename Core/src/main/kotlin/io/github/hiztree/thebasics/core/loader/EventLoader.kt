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
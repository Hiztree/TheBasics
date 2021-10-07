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

package io.github.hiztree.thebasics.core.listeners

import com.google.common.eventbus.Subscribe
import io.github.hiztree.thebasics.core.api.event.UserChatEvent
import io.github.hiztree.thebasics.core.api.event.UserJoinEvent
import io.github.hiztree.thebasics.core.api.event.UserQuitEvent
import io.github.hiztree.thebasics.core.api.event.annotation.Listener
import io.github.hiztree.thebasics.core.api.lang.LangKey

@Listener
class UserListener {

    @Subscribe
    fun onJoin(event: UserJoinEvent) {
        event.user.serialize()
    }

    @Subscribe
    fun onQuit(event: UserQuitEvent) {
        event.user.deserialize()
    }

    @Subscribe
    fun onChat(event: UserChatEvent) {
        if (event.user.isMuted()) {
            event.msg = null
            event.user.sendMsg(LangKey.MUTE_ATTEMPT)
        }
    }

    @Subscribe
    fun onCommand(event: UserChatEvent.Command) {
        if (event.user.isMuted()) {
            event.msg = null
            event.user.sendMsg(LangKey.MUTE_ATTEMPT)
        }
    }
}
package io.github.hiztree.thebasics.core.listeners

import com.google.common.eventbus.Subscribe
import io.github.hiztree.thebasics.core.TheBasics
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
        if(event.user.isMuted()) {
            event.msg = null
            event.user.sendMsg(LangKey.MUTE_ATTEMPT)
        }
    }
}
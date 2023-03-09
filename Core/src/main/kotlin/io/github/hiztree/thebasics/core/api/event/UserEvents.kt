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

package io.github.hiztree.thebasics.core.api.event

import io.github.hiztree.thebasics.core.api.inventory.item.BasicItem
import io.github.hiztree.thebasics.core.api.inventory.item.extra.EnchantType
import io.github.hiztree.thebasics.core.api.inventory.item.extra.PotionType
import io.github.hiztree.thebasics.core.api.user.User
import java.util.*

abstract class UserEvent(val user: User)

abstract class UserCancellableEvent(user: User, val cancel: Boolean = false) : UserEvent(user)

open class UserPreJoinEvent(val uuid: UUID)

open class UserJoinEvent(user: User) : UserEvent(user)

open class UserQuitEvent(user: User) : UserEvent(user)

open class UserChatEvent(user: User, var msg: String?) : UserCancellableEvent(user) {
    open class Command(user: User, msg: String?) : UserChatEvent(user, msg)
}

open class UserSleepEvent(user: User) : UserEvent(user)

open class UserBreedEvent(user: User, val entityID: String) : UserEvent(user)

open class UserDieEvent(user: User) : UserEvent(user)

open class UserPickupItemEvent(user: User, val item: BasicItem) : UserEvent(user)

open class UserKillMobEvent(user: User, val entityID: String) : UserEvent(user)

open class UserMoveEvent(user: User) : UserEvent(user)

open class UserTameAnimalEvent(user: User, val entityID: String) : UserEvent(user)

open class ItemUseEvent(user: User, val item: BasicItem) : UserEvent(user)

open class AnvilRepairEvent(user: User) : UserEvent(user)

open class BrewEvent(user: User, val type: PotionType) : UserEvent(user)

open class CraftEvent(user: User, val item: BasicItem) : UserEvent(user)

open class EnchantEvent(user: User, val item: BasicItem, val type: EnchantType) : UserEvent(user)

open class ForgeEvent(user: User, val item: BasicItem) : UserEvent(user)
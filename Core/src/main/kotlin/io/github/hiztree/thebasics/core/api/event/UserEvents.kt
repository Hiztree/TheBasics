/*
 * Copyright (C) Fallen-Network, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Levi Pawlak <levi.pawlak17@gmail.com>, November 2020
 */

package io.github.hiztree.thebasics.core.api.event

import io.github.hiztree.thebasics.core.api.inventory.item.BasicItem
import io.github.hiztree.thebasics.core.api.inventory.item.extra.EnchantType
import io.github.hiztree.thebasics.core.api.inventory.item.extra.PotionType
import io.github.hiztree.thebasics.core.api.user.User
import java.util.*

abstract class UserEvent(val user: User)

open class UserPreJoinEvent(val uuid: UUID)

open class UserJoinEvent(user: User) : UserEvent(user)

open class UserQuitEvent(user: User) : UserEvent(user)

open class UserChatEvent(user: User, var msg: String?): UserEvent(user)

open class UserSleepEvent(user: User) : UserEvent(user)

open class UserBreedEvent(user: User, val entityID: Int) : UserEvent(user)

open class UserDieEvent(user: User) : UserEvent(user)

open class UserPickupItemEvent(user: User, val item: BasicItem) : UserEvent(user)

open class UserKillMobEvent(user: User, val entityID: Int) : UserEvent(user)

open class UserMoveEvent(user: User) : UserEvent(user)

open class UserTameAnimalEvent(user: User, val entityID: Int) : UserEvent(user)

open class ItemUseEvent(user: User, val item: BasicItem) : UserEvent(user)

open class AnvilRepairEvent(user: User) : UserEvent(user)

open class BrewEvent(user: User, val type: PotionType) : UserEvent(user)

open class CraftEvent(user: User, val item: BasicItem) : UserEvent(user)

open class EnchantEvent(user: User, val item: BasicItem, val type: EnchantType) : UserEvent(user)

open class ForgeEvent(user: User, val item: BasicItem) : UserEvent(user)
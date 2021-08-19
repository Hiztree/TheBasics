/*
 * Copyright (C) Fallen-Network, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Levi Pawlak <levi.pawlak17@gmail.com>, November 2020
 */

package io.github.hiztree.thebasics.core.api.event

import io.github.hiztree.thebasics.core.api.data.Location
import io.github.hiztree.thebasics.core.api.inventory.item.BasicItem
import io.github.hiztree.thebasics.core.api.user.User

abstract class BlockEvent(val user: User, val block: BasicItem, val loc: Location) : Cancellable()

open class BlockBreakEvent(user: User, block: BasicItem, loc: Location) :
    BlockEvent(user, block, loc)

open class BlockPlaceEvent(user: User, block: BasicItem, loc: Location) :
    BlockEvent(user, block, loc)
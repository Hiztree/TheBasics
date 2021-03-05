/*
 * Copyright (C) Fallen-Network, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Levi Pawlak <levi.pawlak17@gmail.com>, November 2020
 */

package io.github.hiztree.thebasics.core.api.inventory.item

class ItemType(val name: String, val altID: String = "", var actualModID: String = "minecraft:$name") {
    init {
        if (!ItemTypes.VALUES.contains(this))
            ItemTypes.VALUES.add(this)
    }
}

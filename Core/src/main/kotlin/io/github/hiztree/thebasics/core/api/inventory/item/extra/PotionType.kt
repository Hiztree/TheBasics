/*
 * Copyright (C) Fallen-Network, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Levi Pawlak <levi.pawlak17@gmail.com>, November 2020
 */

package io.github.hiztree.thebasics.core.api.inventory.item.extra


@Suppress("unused")
enum class PotionType(id: String) {

    AWKWARD("AWKWARD"),
    EMPTY("EMPTY"),
    FIRE_RESISTANCE("FIRE_RESISTANCE"),
    HARMING("HARMING"),
    HEALING("HEALING"),
    INVISIBILITY("INVISIBILITY"),
    LEAPING("LEAPING"),
    LONG_FIRE_RESISTANCE("LONG_FIRE_RESISTANCE"),
    LONG_INVISIBILITY("LONG_INVISIBILITY"),
    LONG_LEAPING("LONG_LEAPING"),
    LONG_NIGHT_VISION("LONG_NIGHT_VISION"),
    LONG_POISON("LONG_POISON"),
    LONG_REGENERATION("LONG_REGENERATION"),
    LONG_SLOWNESS("LONG_SLOWNESS"),
    LONG_STRENGTH("LONG_STRENGTH"),
    LONG_SWIFTNESS("LONG_SWIFTNESS"),
    LONG_WATER_BREATHING("LONG_WATER_BREATHING"),
    LONG_WEAKNESS("LONG_WEAKNESS"),
    MUNDANE("MUNDANE"),
    NIGHT_VISION("NIGHT_VISION"),
    POISON("POISON"),
    REGENERATION("REGENERATION"),
    SLOWNESS("SLOWNESS"),
    STRENGTH("STRENGTH"),
    STRONG_HARMING("STRONG_HARMING"),
    STRONG_HEALING("STRONG_HEALING"),
    STRONG_LEAPING("STRONG_LEAPING"),
    STRONG_POISON("STRONG_POISON"),
    STRONG_REGENERATION("STRONG_REGENERATION"),
    STRONG_STRENGTH("STRONG_STRENGTH"),
    STRONG_SWIFTNESS("STRONG_SWIFTNESS"),
    SWIFTNESS("SWIFTNESS"),
    THICK("THICK"),
    WATER("WATER"),
    WATER_BREATHING("WATER_BREATHING"),
    WEAKNESS("WEAKNESS");

    companion object {
        fun getByName(id: String): PotionType? {
            for (value in values()) {
                if (value.name.equals(id, true) || value.name.contains(id, true))
                    return value
            }

            return null
        }
    }
}
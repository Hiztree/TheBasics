/*
 * Copyright (C) Fallen-Network, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Levi Pawlak <levi.pawlak17@gmail.com>, November 2020
 */

package io.github.hiztree.thebasics.core.api.inventory.item.extra

enum class EnchantType(val id: String) {
    AQUA_AFFINITY("AQUA_AFFINITY"),
    BANE_OF_ARTHROPODS("BANE_OF_ARTHROPODS"),
    BINDING_CURSE("BINDING_CURSE"),
    BLAST_PROTECTION("BLAST_PROTECTION"),
    DEPTH_STRIDER("DEPTH_STRIDER"),
    EFFICIENCY("EFFICIENCY"),
    FEATHER_FALLING("FEATHER_FALLING"),
    FIRE_ASPECT("FIRE_ASPECT"),
    FIRE_PROTECTION("FIRE_PROTECTION"),
    FLAME("FLAME"),
    FORTUNE("FORTUNE"),
    FROST_WALKER("FROST_WALKER"),
    INFINITY("INFINITY"),
    KNOCKBACK("KNOCKBACK"),
    LOOTING("LOOTING"),
    LUCK_OF_THE_SEA("LUCK_OF_THE_SEA"),
    LURE("LURE"),
    MENDING("MENDING"),
    POWER("POWER"),
    PROJECTILE_PROTECTION("PROJECTILE_PROTECTION"),
    PROTECTION("PROTECTION"),
    PUNCH("PUNCH"),
    RESPIRATION("RESPIRATION"),
    SHARPNESS("SHARPNESS"),
    SILK_TOUCH("SILK_TOUCH"),
    SMITE("SMITE"),
    SWEEPING("SWEEPING"),
    THORNS("THORNS"),
    UNBREAKING("UNBREAKING"),
    VANISHING_CURSE("VANISHING_CURSE");

    companion object {
        fun getByName(id: String): EnchantType? {
            for (value in values()) {
                if (value.name.equals(id, true) || value.name.contains(id, true))
                    return value
            }

            return null
        }
    }
}
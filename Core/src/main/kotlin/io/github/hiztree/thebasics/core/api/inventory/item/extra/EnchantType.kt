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
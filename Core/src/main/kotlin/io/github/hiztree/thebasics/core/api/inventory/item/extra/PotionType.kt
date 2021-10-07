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
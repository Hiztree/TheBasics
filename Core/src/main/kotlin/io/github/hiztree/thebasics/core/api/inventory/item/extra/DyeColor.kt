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

enum class DyeColor(val id: String, val data: Byte) {
    BLACK("BLACK", 15),
    BLUE("BLUE", 11),
    BROWN("BROWN", 12),
    CYAN("CYAN", 9),
    GRAY("GRAY", 7),
    GREEN("GREEN", 13),
    LIGHT_BLUE("LIGHT_BLUE", 3),
    LIME("LIME", 5),
    MAGENTA("MAGENTA", 2),
    ORANGE("ORANGE", 1),
    PINK("PINK", 6),
    PURPLE("PURPLE", 10),
    RED("RED", 14),
    SILVER("SILVER", 8),
    WHITE("WHITE", 0),
    YELLOW("YELLOW", 4)
}
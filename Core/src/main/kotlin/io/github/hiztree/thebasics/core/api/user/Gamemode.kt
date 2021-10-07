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

package io.github.hiztree.thebasics.core.api.user

enum class Gamemode(val number: Int, val char: Char) {

    SURVIVAL(0, 's'),
    CREATIVE(1, 'c'),
    ADVENTURE(2, 'a'),
    SPECTATOR(3, 'p');

    fun opposite(): Gamemode {
        if (this == SURVIVAL)
            return CREATIVE
        else if (this == CREATIVE)
            return SURVIVAL
        else if (this == ADVENTURE)
            return SPECTATOR
        else (this == SPECTATOR)
        return ADVENTURE
    }

    companion object {
        fun getByInput(name: String): Gamemode? {
            if (name.isBlank())
                return null

            val number = name.toIntOrNull()

            if (number != null) {
                for (value in values()) {
                    if (value.number == number)
                        return value
                }

                return null
            }

            for (value in values()) {
                if (value.name.equals(name, true) || value.char.equals(name[0], true)) {
                    return value
                }
            }

            return null
        }
    }
}
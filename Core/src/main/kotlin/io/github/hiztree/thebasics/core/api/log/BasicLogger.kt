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

package io.github.hiztree.thebasics.core.api.log

interface BasicLogger {

    /**
     * Prints a message to console according to the level.
     *
     * @param level The level of log
     * @param msg The message to log
     */
    fun log(level: BasicLogLevel, msg: String)

    /**
     * Print an information message to console.
     *
     * @param msg The message to log
     */
    fun info(msg: String) = log(BasicLogLevel.INFO, msg)

    /**
     * Print an error message to console.
     *
     * @param msg The message to log
     */
    fun error(msg: String) = log(BasicLogLevel.ERROR, msg)

    /**
     * Print an warning message to console.
     *
     * @param msg The message to log
     */
    fun warn(msg: String) = log(BasicLogLevel.WARN, msg)

}
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

package io.github.hiztree.thebasics.core.api

import java.time.Duration
import java.time.Instant
import java.time.temporal.ChronoField
import java.time.temporal.ChronoUnit

class BasicTime {

    var days = 0L
        set(value) {
            if (value < 0)
                throw IllegalArgumentException("Days must be greater then 0.")
            field = value
        }

    var hours = 0L
        set(value) {
            if (value < 0)
                throw IllegalArgumentException("Hours must be greater then 0.")
            field = value
        }

    var minutes = 0L
        set(value) {
            if (value < 0)
                throw IllegalArgumentException("Minutes must be greater then 0.")
            field = value
        }

    var seconds = 0L
        set(value) {
            if (value < 0)
                throw IllegalArgumentException("Seconds must be greater then 0.")
            field = value
        }

    fun toInstant(): Instant = Instant.now()
        .plus(days, ChronoUnit.DAYS)
        .plus(hours, ChronoUnit.HOURS)
        .plus(minutes, ChronoUnit.MINUTES)
        .plus(seconds, ChronoUnit.SECONDS)


    fun isZero() = days <= 0 && hours <= 0 && minutes <= 0 && seconds <= 0

    override fun toString(): String {
        val sb = StringBuilder()

        when {
            days > 0 -> sb.append("${days}d")
            hours > 0 -> sb.append("${hours}h")
            minutes > 0 -> sb.append("${minutes}m")
            seconds > 0 -> sb.append("${seconds}s")
        }

        return sb.toString()
    }

    companion object {
        fun parseTime(input: String): BasicTime {
            var sb = StringBuilder()
            val bs = BasicTime()

            for (c in input.toCharArray()) {
                when (c) {
                    'd' -> {
                        bs.days = sb.toString().toLong()
                        sb = StringBuilder()
                    }
                    'h' -> {
                        bs.hours = sb.toString().toLong()
                        sb = StringBuilder()
                    }
                    'm' -> {
                        bs.minutes = sb.toString().toLong()
                        sb = StringBuilder()
                    }
                    's' -> {
                        bs.seconds = sb.toString().toLong()
                        sb = StringBuilder()
                    }
                    else -> {
                        sb.append(c)
                    }
                }
            }

            return bs
        }

        fun fromInstant(instant: Instant) : BasicTime {
            val bs = BasicTime()
            val duration = Duration.between(Instant.now(), instant)

            bs.days = duration.get(ChronoUnit.DAYS)
            bs.hours = duration.get(ChronoUnit.HOURS)
            bs.minutes = duration.get(ChronoUnit.MINUTES)
            bs.seconds = duration.get(ChronoUnit.SECONDS)

            return bs
        }
    }
}
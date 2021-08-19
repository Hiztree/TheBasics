/*
 * Copyright (C) Fallen-Network, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Levi Pawlak <levi.pawlak17@gmail.com>, November 2020
 */

package io.github.hiztree.thebasics.core.api.data

open class Location(
    val x: Double,
    val y: Double,
    val z: Double,
    val yaw: Double = 0.0,
    val pitch: Double = 0.0
) {

    fun getBlockX(): Int {
        return x.toInt()
    }

    fun getBlockY(): Int {
        return y.toInt()
    }

    fun getBlockZ(): Int {
        return z.toInt()
    }
}
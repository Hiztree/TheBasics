/*
 * Copyright (C) Fallen-Network, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Levi Pawlak <levi.pawlak17@gmail.com>, November 2020
 */

package io.github.hiztree.thebasics.core.api

import com.google.common.collect.ImmutableSet
import com.google.common.reflect.ClassPath

abstract class Loader {
    abstract fun load(set: ImmutableSet<ClassPath.ClassInfo>): Boolean
}
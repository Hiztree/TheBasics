package io.github.hiztree.thebasics.core.api.data

data class Warp(
    val name: String,
    var world: World?,
    var location: Location?,
    val valid: Boolean = true
)
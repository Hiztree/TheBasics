package io.github.hiztree.thebasics.core.api.user.data

import io.github.hiztree.thebasics.core.api.Location
import io.github.hiztree.thebasics.core.api.World

data class Home(val name: String, val location: Location, val world: World)
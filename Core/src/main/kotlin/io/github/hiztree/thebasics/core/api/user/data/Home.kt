package io.github.hiztree.thebasics.core.api.user.data

import io.github.hiztree.thebasics.core.api.data.Location
import io.github.hiztree.thebasics.core.api.data.World

data class Home(val name: String, val location: Location, val world: World)
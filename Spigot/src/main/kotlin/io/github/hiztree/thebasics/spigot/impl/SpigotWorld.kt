package io.github.hiztree.thebasics.spigot.impl

import org.bukkit.World

class SpigotWorld(base: World) : io.github.hiztree.thebasics.core.api.World(base.name, base.uid)
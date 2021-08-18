package io.github.hiztree.thebasics.core.api

import io.github.hiztree.thebasics.core.api.inventory.item.BasicItem

data class Kit(val name: String, val interval: Long, val items: List<BasicItem?>)
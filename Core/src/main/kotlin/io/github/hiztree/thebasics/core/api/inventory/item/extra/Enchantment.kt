package io.github.hiztree.thebasics.core.api.inventory.item.extra

class Enchantment(var type: EnchantType, var level: Int) {

    override fun toString(): String {
        return "{$type, $level}"
    }
}
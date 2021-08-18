package io.github.hiztree.thebasics.core.configs

import io.github.hiztree.thebasics.core.api.Kit
import io.github.hiztree.thebasics.core.api.config.ConfigType
import io.github.hiztree.thebasics.core.api.config.annotation.Section
import io.github.hiztree.thebasics.core.api.config.annotation.Setting

@Section(ConfigType.KIT)
class KitConfig {

    companion object {
        @Setting(comment = "Enter your kits here.")
        var kits = arrayListOf<Kit>()
    }
}
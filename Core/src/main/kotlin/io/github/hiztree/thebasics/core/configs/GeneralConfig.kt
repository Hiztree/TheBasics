package io.github.hiztree.thebasics.core.configs

import io.github.hiztree.thebasics.core.api.config.ConfigType
import io.github.hiztree.thebasics.core.api.config.annotation.Section
import io.github.hiztree.thebasics.core.api.config.annotation.Setting

@Section(ConfigType.GENERAL)
class GeneralConfig {

    companion object {
        @Setting(comment = "Max current amount. -1 to disable a max.")
        var maxCurrency = 100000000000.0

        @Setting(comment = "The starting balance for all new players.")
        var startingBalance = 500.0
    }
}
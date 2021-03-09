package io.github.hiztree.thebasics.core.configs

import io.github.hiztree.thebasics.core.api.config.ConfigType
import io.github.hiztree.thebasics.core.api.config.annotation.Section
import io.github.hiztree.thebasics.core.api.config.annotation.Setting

@Section(ConfigType.GENERAL)
class GeneralConfig {

    companion object {
        @Setting(comment = "This is a derp field")
        var testValue = "Derp"

        @Setting(comment = "This is a great field.")
        var thing = "Thing"
    }
}
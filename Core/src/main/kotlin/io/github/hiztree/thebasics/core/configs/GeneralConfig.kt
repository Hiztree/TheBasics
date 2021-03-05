package io.github.hiztree.thebasics.core.configs

import io.github.hiztree.thebasics.core.api.config.ConfigType
import io.github.hiztree.thebasics.core.api.config.annotation.Section
import ninja.leaping.configurate.objectmapping.Setting
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable

@ConfigSerializable
@Section(ConfigType.GENERAL)
object GeneralConfig {

    @Setting("Test", comment = "This is a test field.")
    var testValue = "Derp"
}
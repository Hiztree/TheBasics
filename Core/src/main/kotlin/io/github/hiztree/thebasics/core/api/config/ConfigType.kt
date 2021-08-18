package io.github.hiztree.thebasics.core.api.config

import io.github.hiztree.thebasics.core.TheBasics

enum class ConfigType {

    GENERAL {
        override fun getConfig(): BasicConfig {
            return TheBasics.instance.generalConfig
        }
    },
    KIT {
        override fun getConfig(): BasicConfig {
            return TheBasics.instance.kitConfig
        }
    };

    abstract fun getConfig(): BasicConfig

}
package io.github.hiztree.thebasics.core.api.config.annotation

import io.github.hiztree.thebasics.core.api.config.ConfigType

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class Section(val type: ConfigType)


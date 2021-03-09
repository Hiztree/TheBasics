package io.github.hiztree.thebasics.core.api.config.annotation

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class Setting(val path: String = "", val comment: String = "")


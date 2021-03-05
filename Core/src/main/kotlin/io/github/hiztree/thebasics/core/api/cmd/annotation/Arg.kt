package io.github.hiztree.thebasics.core.api.cmd.annotation

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class Arg(val label: String, val optional: Boolean = false)

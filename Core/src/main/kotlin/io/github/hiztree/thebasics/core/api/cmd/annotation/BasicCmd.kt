package io.github.hiztree.thebasics.core.api.cmd.annotation

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class BasicCmd(val label: String, val desc: String = "")

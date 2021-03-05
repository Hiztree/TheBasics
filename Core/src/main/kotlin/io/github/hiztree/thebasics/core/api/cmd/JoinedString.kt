package io.github.hiztree.thebasics.core.api.cmd

class JoinedString(val args: Array<out String>) {

    companion object {
        val empty = JoinedString(emptyArray())
    }

    override fun toString(): String {
        return args.joinToString(" ")
    }
}
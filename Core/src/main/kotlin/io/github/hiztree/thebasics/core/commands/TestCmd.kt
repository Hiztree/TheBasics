package io.github.hiztree.thebasics.core.commands

import io.github.hiztree.thebasics.core.api.cmd.annotation.Arg
import io.github.hiztree.thebasics.core.api.cmd.annotation.BasicCmd
import io.github.hiztree.thebasics.core.api.cmd.annotation.DefaultCmd
import io.github.hiztree.thebasics.core.api.cmd.annotation.SubCmd
import io.github.hiztree.thebasics.core.api.cmd.sender.CommandSender
import io.github.hiztree.thebasics.core.api.user.User

@BasicCmd("test", "This is a test command.")
class TestCmd {

    @DefaultCmd
    fun testFun(sender: CommandSender) {
        sender.sendMsg("Hi")
    }

    @SubCmd("give")
    fun testSub(sender: User, @Arg("target") target: User, @Arg("price", optional = true) price: Int?) {
        sender.sendMsg("Hi sender, you gave $price.")
        target.sendMsg("Hi target, you received $price.")
    }
}
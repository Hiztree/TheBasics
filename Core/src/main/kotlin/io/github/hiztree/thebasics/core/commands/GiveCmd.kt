package io.github.hiztree.thebasics.core.commands

import io.github.hiztree.thebasics.core.api.cmd.JoinedString
import io.github.hiztree.thebasics.core.api.cmd.UsageException
import io.github.hiztree.thebasics.core.api.cmd.annotation.Arg
import io.github.hiztree.thebasics.core.api.cmd.annotation.BasicCmd
import io.github.hiztree.thebasics.core.api.cmd.annotation.DefaultCmd
import io.github.hiztree.thebasics.core.api.cmd.sender.CommandSender
import io.github.hiztree.thebasics.core.api.inventory.item.BasicItem
import io.github.hiztree.thebasics.core.api.inventory.item.ItemType
import io.github.hiztree.thebasics.core.api.lang.LangKey
import io.github.hiztree.thebasics.core.api.user.User

@BasicCmd("give", "Give yourself or another user an item.")
class GiveCmd {

    @DefaultCmd
    fun giveCmd(sender: CommandSender, @Arg("target") target: User, @Arg("type") type: ItemType, @Arg("spec", true) input: JoinedString) {
        val item = BasicItem.parseItem(type, input.args) ?: throw UsageException(sender, LangKey.INVALID_ITEM)

        target.giveItem(item)
    }
}
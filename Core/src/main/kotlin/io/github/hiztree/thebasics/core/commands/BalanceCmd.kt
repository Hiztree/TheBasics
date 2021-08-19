package io.github.hiztree.thebasics.core.commands

import io.github.hiztree.thebasics.core.api.cmd.UsageException
import io.github.hiztree.thebasics.core.api.cmd.annotation.Arg
import io.github.hiztree.thebasics.core.api.cmd.annotation.BasicCmd
import io.github.hiztree.thebasics.core.api.cmd.annotation.DefaultCmd
import io.github.hiztree.thebasics.core.api.cmd.annotation.SubCmd
import io.github.hiztree.thebasics.core.api.cmd.sender.CommandSender
import io.github.hiztree.thebasics.core.api.econ.ResultType
import io.github.hiztree.thebasics.core.api.lang.LangKey
import io.github.hiztree.thebasics.core.api.lang.pretty
import io.github.hiztree.thebasics.core.api.user.User

@BasicCmd("balance", "Check your balance or another players.")
class BalanceCmd {

    @DefaultCmd
    fun balanceCmd(sender: CommandSender, @Arg("target", true) user: User?) {
        if (user != null) {
            if (!sender.hasPermission("thebasics.balance.others"))
                throw UsageException(sender, LangKey.NO_PERMISSION)

            sender.sendMsg(LangKey.BALANCE_CHECK_OTHER, user.getName(), user.balance().pretty())
        } else {
            if (sender !is User)
                throw UsageException(sender, LangKey.USER_ERROR)

            sender.sendMsg(LangKey.BALANCE_CHECK_SELF, sender.balance().pretty())
        }
    }

    @SubCmd("set")
    fun setCmd(sender: CommandSender, @Arg("target") target: User, @Arg("amount") amount: Double) {
        if (amount < 0)
            throw UsageException(sender, LangKey.INVALID_AMOUNT)

        target.setBalance(amount)

        sender.sendMsg(LangKey.BALANCE_SET_SENDER, target.getName(), amount.pretty())
        target.sendMsg(LangKey.BALANCE_SET_TARGET, amount.pretty())
    }

    @SubCmd("add")
    fun addCmd(sender: CommandSender, @Arg("target") target: User, @Arg("amount") amount: Double) {
        if (amount < 0)
            throw UsageException(sender, LangKey.INVALID_AMOUNT)

        if (target.deposit(amount) == ResultType.SUCCESS) {
            sender.sendMsg(LangKey.BALANCE_DEPOSIT_SENDER, amount.pretty(), target.getName())
            target.sendMsg(LangKey.BALANCE_DEPOSIT_TARGET, amount.pretty())
        } else {
            target.sendMsg(LangKey.BALANCE_MAX)
        }
    }

    @SubCmd("take")
    fun takeCmd(sender: CommandSender, @Arg("target") target: User, @Arg("amount") amount: Double) {
        if (amount < 0)
            throw UsageException(sender, LangKey.INVALID_AMOUNT)

        if (target.withdraw(amount) == ResultType.SUCCESS) {
            sender.sendMsg(LangKey.BALANCE_WITHDRAW_SENDER, amount.pretty(), target.getName())
            target.sendMsg(LangKey.BALANCE_WITHDRAW_TARGET, amount.pretty())
        } else {
            sender.sendMsg(LangKey.INSUFFICIENT_FUNDS_SENDER, target.getName())
        }
    }
}
package io.github.hiztree.thebasics.core.api.user

import io.github.hiztree.thebasics.core.api.config.BasicConfig
import io.github.hiztree.thebasics.core.api.config.Serializable
import io.github.hiztree.thebasics.core.api.econ.AccountHolder
import io.github.hiztree.thebasics.core.api.econ.ResultType
import io.github.hiztree.thebasics.core.configs.GeneralConfig
import java.util.*

abstract class OfflineUser(uniqueId: UUID) : AccountHolder, Serializable, BasicConfig(
    "${uniqueId}.conf",
    io.github.hiztree.thebasics.core.TheBasics.instance.getPlayerDir()
) {

    abstract fun getName(): String

    override fun balance(): Double {
        return this["balance"].getDouble(GeneralConfig.startingBalance)
    }

    override fun setBalance(balance: Double) {
        this["balance"].set(balance)
        save()
    }

    override fun withdraw(balance: Double): ResultType {
        val currentBalance = balance()

        if (currentBalance >= balance) {
            setBalance(currentBalance - balance)
            return ResultType.SUCCESS
        }

        return ResultType.INSUFFICIENT_FUNDS
    }

    override fun deposit(balance: Double): ResultType {
        val currentBalance = balance()

        if ((currentBalance + balance) > GeneralConfig.maxCurrency)
            return ResultType.MAX_CURRENCY

        setBalance(currentBalance + balance)

        return ResultType.SUCCESS
    }
}
/*
 * MIT License
 *
 * Copyright (c) 2021 Levi Pawlak
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
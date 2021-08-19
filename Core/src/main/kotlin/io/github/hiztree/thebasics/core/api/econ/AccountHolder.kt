package io.github.hiztree.thebasics.core.api.econ

interface AccountHolder {

    fun balance(): Double

    fun setBalance(balance: Double)

    fun withdraw(balance: Double): ResultType

    fun deposit(balance: Double): ResultType

}
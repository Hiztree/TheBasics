package io.github.hiztree.thebasics.core.api.user

import java.util.*

interface OfflineUser {

    fun getName(): String
    fun getUniqueID(): UUID

}
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

package io.github.hiztree.thebasics.core.api.cmd

import com.google.common.reflect.TypeToken
import io.github.hiztree.thebasics.core.api.BasicTime
import io.github.hiztree.thebasics.core.api.data.Kit
import io.github.hiztree.thebasics.core.api.data.Warp
import io.github.hiztree.thebasics.core.api.inventory.item.ItemType
import io.github.hiztree.thebasics.core.api.inventory.item.extra.EnchantType
import io.github.hiztree.thebasics.core.api.inventory.item.extra.PotionType
import io.github.hiztree.thebasics.core.api.user.Gamemode
import io.github.hiztree.thebasics.core.api.user.User
import io.github.hiztree.thebasics.core.api.user.data.Home
import java.time.Instant

object BasicTokens {

    val USER_TOKEN: TypeToken<User> = TypeToken.of(User::class.java)
    val TIME_TOKEN: TypeToken<BasicTime> = TypeToken.of(BasicTime::class.java)
    val ITEM_TYPE_TOKEN: TypeToken<ItemType> = TypeToken.of(ItemType::class.java)
    val ENCHANT_TYPE_TOKEN: TypeToken<EnchantType> = TypeToken.of(EnchantType::class.java)
    val POTION_TYPE_TOKEN: TypeToken<PotionType> = TypeToken.of(PotionType::class.java)
    val JOINED_STRING_TOKEN: TypeToken<JoinedString> = TypeToken.of(JoinedString::class.java)
    val INSTANT_TOKEN: TypeToken<Instant> = TypeToken.of(Instant::class.java)
    val HOME_TOKEN: TypeToken<Home> = TypeToken.of(Home::class.java)
    val GAMEMODE_TOKEN: TypeToken<Gamemode> = TypeToken.of(Gamemode::class.java)
    val KIT_TOKEN: TypeToken<Kit> = TypeToken.of(Kit::class.java)
    val WARP_TOKEN: TypeToken<Warp> = TypeToken.of(Warp::class.java)
}
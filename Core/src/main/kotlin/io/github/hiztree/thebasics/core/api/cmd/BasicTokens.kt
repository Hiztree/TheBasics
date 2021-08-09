package io.github.hiztree.thebasics.core.api.cmd

import com.google.common.reflect.TypeToken
import io.github.hiztree.thebasics.core.api.BasicTime
import io.github.hiztree.thebasics.core.api.inventory.item.ItemType
import io.github.hiztree.thebasics.core.api.inventory.item.extra.EnchantType
import io.github.hiztree.thebasics.core.api.inventory.item.extra.PotionType
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
}
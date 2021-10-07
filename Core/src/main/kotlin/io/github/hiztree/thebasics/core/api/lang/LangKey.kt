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

package io.github.hiztree.thebasics.core.api.lang

import io.github.hiztree.thebasics.core.TheBasics
import java.text.DecimalFormat
import java.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.toKotlinDuration

enum class LangKey(private val default: String) {

    NO_PERMISSION("&cYou do not have permission to perform this command!"),
    INVALID_USAGE("&cYou must specify a valid: &7{0}&c!"),
    DECIMAL_FORMAT("#.##"),
    HEALED_TARGET("&7You have healed &6{0}&7."),
    HEALED_SENDER("&7You have been healed by &6{0}&7."),
    HEAL_SELF("&7You have healed yourself."),
    FED_TARGET("&7You have fed &6{0}&7."),
    FED_SENDER("&7Your hunger has been restored by &6{0}&7."),
    FED_SELF("&7You have fed yourself."),
    USER_ERROR("&cYou must be a user to perform this command!"),
    INVALID_ITEM("&cYou have specified an invalid item!"),
    KICK_NOTIFY("&cThe user &7{0} &chas been kicked from the server."),
    MUTE_NOTIFY("&cThe user &7{0} &chas been muted for &7{1}&c."),
    MUTE_ERROR("&cThat user is already muted!."),
    MUTED("&cYou have been muted for &7{0} &cfor &7{1}&c."),
    MUTE_ATTEMPT("&cYou are still muted and cannot talk or use commands."),
    XP("&7The player &6{0} &7xp is &6{1} &7and level is &6{2}&7."),
    XP_GIVE_SENDER("&7You gave &6{0} &7levels to &6{1}&7."),
    XP_GIVE_TARGET("&7You received &6{0} &7levels."),
    XP_RMV_SENDER("&7You removed &6{0} &7levels to &6{1}&7."),
    XP_RMV_TARGET("&7You lost &6{0} &7levels."),
    INVALID_LVL("&cThe player does not have enough levels to remove!"),
    HOME_LIST("&6Homes: &7{0}"),
    REQ_HOME("&cYou must set a home before using this command!"),
    SET_HOME("&7You have set a home called &6{0} &7at your current location."),
    UN_SET_HOME("&7You have removed a home called &6{0}&7."),
    HOME_IS_EXIST_ERROR("&cThe home called &7{0} &calready exist!"),
    GAMEMODE_CHANGE_SENDER("&7You have changed the user &6{0} &7game mode to &6{1}&7."),
    GAMEMODE_CHANGE_TARGET("&7Your game mode has been changed to &6{0}&7."),
    KIT_GIVE_SENDER("&7You gave the &6{0} &7kit to &6{1}&7."),
    KIT_GIVE_TARGET("&7You have received the kit &6{0}&7."),
    KIT_INTERVAL("&cYou must wait &7{0} &cuntil you can use the &7{1} &ckit again!"),
    KIT_PERMISSION("&cYou do not have access to this kit!"),
    KIT_LIST("&7Kits: &6{0}&7."),
    TELEPORT_HERE_TARGET("&7You have been teleported to &6{0}'s &7location."),
    TELEPORT_SENDER("&7You have teleported to &6{0}'s &7location."),
    NO_SPAWN("&cThe server does not have a set spawn!"),
    SET_SPAWN("&7You have set the servers spawn at your current location."),
    REPLACE_WARP("&7You have updated the warp &6{0} &7to your current location."),
    SET_WARP("&7You have set a warp called &6{0} &7at your current location."),
    WARP_PERMISSION("&cYou do not have permission to use this warp!"),
    WARP_USE("&7You have teleported to the warp &6{0}&7."),
    WARP_LIST("&7Warps: &6{0}&7."),
    CURRENCY_SYMBOL("$"),
    BALANCE_CHECK_SELF("&7Balance: &6{CURRENCY_SYMBOL}{0}&7."),
    BALANCE_CHECK_OTHER("&6{0}'s &7Balance: &6{CURRENCY_SYMBOL}{1}&7."),
    BALANCE_SET_SENDER("&7You set the user &6{0} &7balance to &6{CURRENCY_SYMBOL}{1}&7."),
    BALANCE_SET_TARGET("&7Your balance was set to &6{CURRENCY_SYMBOL}{0}&7."),
    BALANCE_DEPOSIT_SENDER("&7You deposited &6{CURRENCY_SYMBOL}{0} &7into &6{1}'s &7account."),
    BALANCE_DEPOSIT_TARGET("&7You have received &6{CURRENCY_SYMBOL}{0}&7."),
    BALANCE_WITHDRAW_SENDER("&7You withdrawn &6{CURRENCY_SYMBOL}{0} &7from &6{1}'s &7account."),
    BALANCE_WITHDRAW_TARGET("&7You have spent &6{CURRENCY_SYMBOL}{0}&7."),
    BALANCE_MAX("&cYou have already reached the maximum balance!"),
    INVALID_AMOUNT("&cYou must specify a positive amount!"),
    INSUFFICIENT_FUNDS_SENDER("&cThe player &7{0} &cdoes not have enough funds!");

    companion object {
        val decimalFormat = DecimalFormat(DECIMAL_FORMAT.msg)

        fun load() {
            var update = false

            for (value in values()) {
                val name = value.name.toCamelCase()
                val node = TheBasics.instance.langConfig.getRootNode().node(name)

                if (node.virtual()) {
                    node.set(value.default)
                    update = true
                }

                value.msg = node.getString(value.default)
            }

            if (update)
                TheBasics.instance.langConfig.save()
        }
    }

    var msg: String = default

    fun parse(vararg args: Any): String {
        var parsedMsg = msg

        for (index in args.indices) {
            parsedMsg = parsedMsg.replace("{$index}", args[index].toString())
        }

        for (value in values()) {
            if (parsedMsg.contains("{${value.name}}")) {
                parsedMsg = parsedMsg.replace("{${value.name}}", value.msg)
            }
        }

        return parsedMsg
    }

    override fun toString(): String {
        return msg
    }
}

@OptIn(ExperimentalTime::class)
fun Duration.pretty(): String =
    this.toKotlinDuration().toIsoString().replace("PT", "").split(".")[0] + "S"

fun Number.pretty(): String = LangKey.decimalFormat.format(this)
fun String.toCamelCase() =
    toLowerCase().split('_').joinToString("", transform = String::capitalize).decapitalize()
package io.github.hiztree.thebasics.core.api.lang

import io.github.hiztree.thebasics.core.TheBasics

enum class LangKey(private val default: String) {

    NO_PERMISSION("&cYou do not have permission to perform this command!"),
    INVALID_USAGE("&cYou must specify a valid: &7{0}&c!"),
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
    MUTE_ATTEMPT("&cYou are still muted and cannot talk."),
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
    TELEPORT_HERE_TARGET("&7You have been teleported to &6{0}'s &7location."),
    TELEPORT_SENDER("&7You have teleported to &6{0}'s &7location."),
    NO_SPAWN("&cThe server does not have a set spawn!"),
    SET_SPAWN("&7You have set the servers spawn at your current location.");

    companion object {

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

            if(update)
                TheBasics.instance.langConfig.save()
        }

    }

    var msg: String = default

    override fun toString(): String {
        return msg
    }
}

fun String.toCamelCase() = toLowerCase().split('_').joinToString("", transform = String::capitalize).decapitalize()
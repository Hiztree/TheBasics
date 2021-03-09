package io.github.hiztree.thebasics.core.api.lang

import io.github.hiztree.thebasics.core.TheBasics

enum class LangKey(private val default: String) {

    NO_PERMISSION("&cYou do not have permission to perform this command!"),
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
    MUTE_ATTEMPT("&cYou are still muted and cannot talk.");

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
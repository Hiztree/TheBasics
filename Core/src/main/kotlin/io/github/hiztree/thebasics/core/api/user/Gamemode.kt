package io.github.hiztree.thebasics.core.api.user

enum class Gamemode(val number: Int, val char: Char) {

    SURVIVAL(0, 's'),
    CREATIVE(1, 'c'),
    ADVENTURE(2, 'a'),
    SPECTATOR(3, 'p');

    fun opposite(): Gamemode {
        if (this == SURVIVAL)
            return CREATIVE
        else if (this == CREATIVE)
            return SURVIVAL
        else if (this == ADVENTURE)
            return SPECTATOR
        else (this == SPECTATOR)
        return ADVENTURE
    }

    companion object {
        fun getByInput(name: String): Gamemode? {
            if (name.isBlank())
                return null

            val number = name.toIntOrNull()

            if (number != null) {
                for (value in values()) {
                    if (value.number == number)
                        return value
                }

                return null
            }

            for (value in values()) {
                if (value.name.equals(name, true) || value.char.equals(name[0], true)) {
                    return value
                }
            }

            return null
        }
    }
}
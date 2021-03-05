package io.github.hiztree.thebasics.core.api.cmd

import com.google.common.collect.ImmutableList
import com.google.common.collect.Lists
import com.google.common.reflect.TypeToken
import io.github.hiztree.thebasics.core.TheBasics
import io.github.hiztree.thebasics.core.api.cmd.annotation.Arg
import io.github.hiztree.thebasics.core.api.cmd.sender.CommandSender
import io.github.hiztree.thebasics.core.api.cmd.sender.ConsoleSender
import io.github.hiztree.thebasics.core.api.user.User
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

class CommandSpec(
    val label: String,
    val desc: String,
    parentLabel: String,
    private val senderClass: Class<out CommandSender>,
    private val instance: Any,
    private val method: Method
) {

    val subCommands: ArrayList<CommandSpec> = Lists.newArrayList()

    val usage: String
    private val parameters = method.parameters.drop(1)
    private val reqParams: Int

    init {
        val sb = StringBuilder("/${if (parentLabel.isEmpty()) label else "$parentLabel $label"}")

        for (parameter in method.parameters.drop(1)) {
            sb.append(" ")

            if (parameter.isAnnotationPresent(Arg::class.java)) {
                val arg = parameter.getAnnotation(Arg::class.java)!!

                sb.append(if (arg.optional) "[" else "<").append(arg.label).append(if (arg.optional) "]" else ">")
            } else {
                sb.append("<").append(parameter.name).append(">")
            }
        }

        usage = sb.toString()

        reqParams = parameters.count { if(it.isAnnotationPresent(Arg::class.java)) !it.getAnnotation(Arg::class.java)!!.optional else true }
    }

    fun performCmd(sender: CommandSender, rawArgs: Array<out String>) {
        if (rawArgs.isNotEmpty()) {
            val subCmd = subCommands.firstOrNull { it.label.equals(rawArgs[0], true) }

            if (subCmd != null) {
                subCmd.performCmd(sender, rawArgs.drop(1).toTypedArray())
            } else {
                if (!senderClass.isAssignableFrom(sender.javaClass)) {
                    sender.sendMsg("&cYou must be a ${if (sender is ConsoleSender) "user" else "console"}!")
                    return
                }

                val args = Lists.newArrayList<Any>()

                args.add(sender)

                if (reqParams > rawArgs.size) {
                    sender.sendMsg("&cUsage: &7$usage")
                    return
                }

                for (x in parameters.indices) {
                    val param = parameters[x]

                    val arg = TheBasics.getCommandContext(TypeToken.of(param.type))

                    if (arg == null) {
                        sender.sendMsg("&cThere was an error performing the command.")
                        return
                    }

                    try {
                        if(arg.type == TypeToken.of(JoinedString::class.java)) {
                            args.add(JoinedString(rawArgs.copyOfRange(x, rawArgs.size)))
                        } else {
                            args.add(arg.complete(rawArgs[x]))
                        }
                    } catch (e: Exception) {
                        when(e) {
                            is CommandException, is ArrayIndexOutOfBoundsException -> {
                                val argAnnotation = param.getAnnotation(Arg::class.java)

                                if (argAnnotation != null && !argAnnotation.optional) {
                                    sender.sendMsg("&cYou must specify a valid: &7${e.message}&c.")
                                    return
                                }

                                args.add(null)
                            }
                        }
                    }
                }

                try {
                    method.invoke(instance, *args.toTypedArray())
                } catch(ignore: InvocationTargetException) {}
            }
        } else {
            if (reqParams > 1) {
                sender.sendMsg("&cUsage: &7$usage")
                return
            }

            val nullArray = arrayOfNulls<Any>(parameters.size) // Work around for when there is only optional args.

            method.invoke(instance, sender, *nullArray)
        }
    }

    fun tabComplete(sender: CommandSender, args: Array<out String>): MutableList<String> {
        if (args.isEmpty()) {
            return ImmutableList.of()
        }

        val lastWord = args[args.size - 1]

        val matchedCompletions = Lists.newArrayList<String>()

        for (subCommand in subCommands) {
            if(subCommand.label.startsWith(lastWord, true))
                matchedCompletions.add(subCommand.label)
        }

        if(parameters.isNotEmpty()) {
            val param = parameters[args.size - 1]
            val type = TheBasics.getCommandContext(TypeToken.of(param.type))

            if (type != null)
                matchedCompletions.addAll(type.tab(sender, lastWord))
        }

        matchedCompletions.sortWith(String.CASE_INSENSITIVE_ORDER)

        return matchedCompletions
    }
}
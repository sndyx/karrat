/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.command

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.elementDescriptors
import kotlinx.serialization.serializer
import org.karrat.command.generic.*
import org.karrat.command.generic.installCommand
import org.karrat.command.generic.killCommand
import org.karrat.command.generic.stopCommand
import org.karrat.entity.Player
import org.karrat.play.colored
import org.karrat.serialization.command.CommandDecoder
import org.karrat.server.Loadable

public inline fun command(
    literals: List<String>,
    structure: Command.() -> Unit = { }
): Command {
    val command = CommandNodeLiteral(literals.toMutableList())
    command.structure()
    return command
}

public inline fun command(
    literal: String,
    structure: Command.() -> Unit = { }
): Command = command(listOf(literal), structure)

public fun Command.route(
    literals: List<String>,
    structure: Command.() -> Unit = { }
): Command = command(literals, structure).also {
    nodes.add(it)
}

public fun Command.route(
    literal: String,
    structure: Command.() -> Unit = { }
): Command = route(listOf(literal), structure)

public inline fun <reified T> Command.argument(
    label: String? = null,
    structure: Command.() -> Unit = { }
): Command {
    val command = CommandNodeArgument<T>(label)
    command.structure()
    nodes.add(command)
    return command
}

public inline fun <reified T> Command.vararg(
    label: String? = null
): Command {
    val command = CommandNodeArgument<T>(label)
    nodes.add(command)
    command.nodes.add(command)
    return command
}

public interface Command {

    public val nodes: MutableSet<Command>
    public val executor: CommandExecutor

    public fun matchesCommand(tokens: List<String>): Pair<Boolean, Int>

    public companion object : Loadable<Command> {

        override val list: MutableSet<Command> = mutableSetOf()

        override fun unregister(value: Command) {
            list.remove(value)
        }

        override fun register(value: Command) {
            check(value is CommandNodeLiteral) { "Root node must be a literal node." }
            list.add(value)
        }

        override fun load() {
            register(killCommand())
            register(installCommand())
            register(stopCommand())
            register(echoCommand())
            register(complexCommand())
        }

        public fun run(command: String, sender: Player? = null) {
            val tokens = command.split(" ")
            list.map { it as CommandNodeLiteral }
                .firstOrNull { node -> node.literals.any { it.equals(tokens[0], true) } }
                ?.run(tokens.drop(1), sender)
                ?: respond(sender, "&cUnknown command.".colored())
        }

    }

    public fun run(
        tokens: List<String>,
        sender: Player? = null,
        args: List<Any> = emptyList()
    ) {
        if (tokens.isEmpty()) executor.execute(CommandScope(sender, args))
        else {
            val next = resolveNextNode(tokens)
            when (next?.first) {
                null -> {
                    respond(sender, "&cInvalid syntax".colored())
                    return
                }
                is CommandNodeArgument<*> -> {
                    val argumentNode = next.first as CommandNodeArgument<*> // Smart cast impossible
                    val decoder = CommandDecoder(ArrayDeque(tokens.take(next.second)))
                    val result = argumentNode.serializer.deserialize(decoder)
                    val newArgs = args.toMutableList().apply { add(result!!) }.toList()
                    argumentNode.run(tokens.drop(next.second), sender, newArgs)
                }
                else -> {
                    val literalNode = next.first as CommandNodeLiteral
                    literalNode.run(tokens.drop(1), sender, args)
                }
            }
        }
    }

    public fun resolveNextNode(tokens: List<String>): Pair<Command, Int>? {
        var bestFit: Pair<Command, Int>? = null
        nodes.forEach {
            val result = it.matchesCommand(tokens)
            if (result.first) {
                if (bestFit == null || bestFit!!.second < result.second) {
                    bestFit = Pair(it, result.second)
                }
            }
        }
        return bestFit
    }

    public fun onRun(block: CommandScope.() -> Unit): Command {
        executor.globalExecutor = block
        return this
    }

    public fun onRunByConsole(block: CommandScope.() -> Unit): Command {
        executor.consoleExecutor = block
        return this
    }

    public fun onRunByPlayer(block: PlayerCommandScope.() -> Unit): Command {
        executor.playerExecutor = block
        return this
    }

    public fun aliases(aliases: List<String>): Command {
        check(this is CommandNodeLiteral) { "Cannot define aliases for an argument node." }
        this.literals.addAll(aliases)
        return this
    }

}

@PublishedApi
internal class CommandNodeLiteral @PublishedApi internal constructor(
    val literals: MutableList<String>,
) : Command {

    override val nodes: MutableSet<Command> = mutableSetOf()
    override val executor: CommandExecutor = CommandExecutor()

    override fun matchesCommand(tokens: List<String>): Pair<Boolean, Int> {
        val matches = literals.firstOrNull { it == tokens.first() } != null
        return Pair(matches, 1)
    }

}

@PublishedApi
@OptIn(ExperimentalSerializationApi::class)
internal class CommandNodeArgument<T> @PublishedApi internal constructor(
    val serializer: KSerializer<T>,
    val label: String? = null
) : Command {

    companion object {
        inline operator fun <reified T> invoke(label: String? = null) =
            CommandNodeArgument<T>(serializer(), label)
    }

    override val nodes: MutableSet<Command> = mutableSetOf()
    override val executor: CommandExecutor = CommandExecutor()

    override fun matchesCommand(tokens: List<String>): Pair<Boolean, Int> {
        val descriptor = serializer.descriptor
        val size = decompiledElementsCount(descriptor)
        if (size > tokens.size) return Pair(false, 0)
        return Pair(checkMatch(descriptor, ArrayDeque(tokens)), size)
    }

    private fun checkMatch(descriptor: SerialDescriptor, tokens: ArrayDeque<String>): Boolean {
        if (descriptor.kind is PrimitiveKind) {
            return ensurePrimitiveMatch(
                tokens.removeFirst(),
                descriptor.kind as PrimitiveKind
            )
        }
        descriptor.elementDescriptors.forEach {
            if (!checkMatch(it, tokens)) {
                return false
            }
        }
        return true
    }

    private fun ensurePrimitiveMatch(token: String, kind: PrimitiveKind): Boolean {
        return runCatching {
            when (kind) {
                PrimitiveKind.BOOLEAN -> token.lowercase().toBooleanStrict()
                PrimitiveKind.BYTE -> token.toByte()
                PrimitiveKind.SHORT -> token.toShort()
                PrimitiveKind.INT -> token.toInt()
                PrimitiveKind.LONG -> token.toLong()
                PrimitiveKind.FLOAT -> token.toFloat()
                PrimitiveKind.DOUBLE -> token.toDouble()
                PrimitiveKind.STRING -> { /* Ignore */ }
                else -> {
                    return false // Unexpected argument type.
                }
            }
        }.isSuccess
    }

    private fun decompiledElementsCount(descriptor: SerialDescriptor): Int {
        return if (descriptor.kind is PrimitiveKind) {
            1
        } else {
            var total = 0
            descriptor.elementDescriptors.forEach {
                total += decompiledElementsCount(it)
            }
            total
        }
    }

}

private fun respond(sender: Player?, response: String) {
    if (sender != null) {
        sender.sendMessage(response)
    } else {
        println(response)
    }
}
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
import org.karrat.play.colored
import org.karrat.serialization.command.CommandDecoder
import org.karrat.server.Loadable
import kotlin.collections.ArrayDeque

public inline fun command(
    literal: String,
    structure: Command.() -> Unit = { }
): Command {
    val command = CommandNodeLiteral(literal)
    command.structure()
    return command
}

public fun Command.route(
    literal: String,
    structure: Command.() -> Unit = { }
): Command = command(literal, structure).also {
    nodes.add(it)
}

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

    public fun matches(scope: CommandScope<CommandSender>): Pair<Boolean, Int>

    public fun consume(scope: CommandScope<CommandSender>, size: Int)

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

        public fun run(command: String, sender: CommandSender) {
            val tokens = command.split(" ")
            val cmd = list.firstOrNull { (it as CommandNodeLiteral).literal == tokens[0] }
            if (cmd == null) {
                sender.respond("&cUnknown command.".colored())
                return
            }
            cmd.run(CommandScope(ArrayDeque(tokens.drop(1)), sender))
        }

    }

    public fun run(command: String, sender: CommandSender): Unit = run(CommandScope(ArrayDeque(), sender))

    public fun run(scope: CommandScope<CommandSender>) {
        if (scope.tokens.isEmpty()) executor.execute(scope)
        else {
            val next = resolveNextNode(scope)
            next?.first?.let {
                it.consume(scope, next.second)
                it.run(scope)
            } ?: let {
                scope.respond("&cInvalid syntax".colored())
                return
            }

        }
    }

    public fun resolveNextNode(scope: CommandScope<CommandSender>): Pair<Command, Int>? {
        var bestFit: Pair<Command, Int>? = null
        nodes.forEach {
            val result = it.matches(scope)
            if (result.first) {
                if (bestFit == null || bestFit!!.second < result.second) {
                    bestFit = Pair(it, result.second)
                }
            }
        }
        return bestFit
    }

    public fun onRun(block: CommandScope<CommandSender>.() -> Unit): Command {
        executor.globalExecutor = block
        return this
    }

    public fun onRunByPlayer(block: CommandScope<PlayerCommandSender>.() -> Unit): Command {
        executor.playerExecutor = block
        return this
    }

    public fun onRunByConsole(block: CommandScope<ConsoleCommandSender>.() -> Unit): Command {
        executor.consoleExecutor = block
        return this
    }
}

public class CommandScope<out T: CommandSender>(
    public val tokens: ArrayDeque<String> = ArrayDeque(),
    public val sender: T,
    public val args: MutableList<Any> = mutableListOf()
) {
    public fun respond(response: String) {
        sender.respond(response)
    }

    public fun drop(num: Int) {
        tokens.drop(num)
    }

    public fun drop() {
        tokens.drop(1)
    }

    public fun arg(arg: Any) {
        args.add(arg)
    }
}

@PublishedApi
internal class CommandNodeLiteral @PublishedApi internal constructor(
    val literal: String,
) : Command {

    override val nodes: MutableSet<Command> = mutableSetOf()
    override val executor: CommandExecutor = CommandExecutor()

    override fun matches(scope: CommandScope<CommandSender>): Pair<Boolean, Int> {
        return Pair(scope.tokens.first() == literal, 1)
    }

    override fun consume(scope: CommandScope<CommandSender>, size: Int) {
        scope.drop(size)
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

    override fun matches(scope: CommandScope<CommandSender>): Pair<Boolean, Int> {
        val descriptor = serializer.descriptor
        val size = decompiledElementsCount(descriptor)
        if (size > scope.tokens.size) return Pair(false, 0)
        return Pair(checkMatch(descriptor, ArrayDeque(scope.tokens)), size)
    }

    override fun consume(scope: CommandScope<CommandSender>, size: Int) {
        val decoder = CommandDecoder(ArrayDeque(scope.tokens.take(size)))
        val result = serializer.deserialize(decoder)
        scope.arg(result!!)
        scope.drop(size)
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
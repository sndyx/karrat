/*
 * Copyright © Karrat - 2023.
 */

package org.karrat.command

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.descriptors.elementDescriptors
import kotlinx.serialization.serializer
import org.karrat.command.generic.*
import org.karrat.entity.Player
import org.karrat.plugin.Plugin
import org.karrat.plugin.karrat
import org.karrat.plugin.minecraft
//import org.karrat.plugin.minecraft
import org.karrat.serialization.command.CommandDecoder
import org.karrat.server.Registry

public inline fun command(
    literal: String,
    vararg aliases: String,
    structure: Command.() -> Unit = { }
): Command = CommandNodeLiteral(listOf(literal, *aliases))
    .apply(structure)

public fun Command.route(
    literal: String,
    vararg aliases: String,
    structure: Command.() -> Unit = { }
): Command = command(literal, *aliases, structure = structure)
    .also { nodes.add(it) }

public inline fun <reified T : Any> Command.argument(
    mapTo: String? = null,
    label: String? = null,
    structure: Command.() -> Unit = { }
): Command = CommandNodeArgument<T>(mapTo, label)
    .apply(structure)
    .also { nodes.add(it) }

public fun Command.redirect(
    to: Command
): Unit = CommandNodeRedirect(to)
    .let { nodes.add(it) }

public inline fun <reified T : Any> Command.vararg(
    mapTo: String? = null,
    label: String? = null
): Command = CommandNodeArgument<List<T>>(mapTo, label)
    .also { nodes.add(it) }

public fun Command.eval(
    tokens: List<String>,
    args: MutableList<Any> = mutableListOf(),
    mappings: MutableList<String?> = mutableListOf()
): Result<EvaluatedCommand> {
    if (tokens.isEmpty()) return Result.success(EvaluatedCommand(this, CommandArguments(args, mappings)))
    return resolveNextNode(tokens)?.let {
        it.first.consume(tokens.take(it.second), args, mappings)
        it.first.eval(tokens.drop(it.second), args, mappings)
    } ?: Result.failure(IllegalArgumentException("Unknown command."))
}

public fun Command.run(
    tokens: List<String>,
    sender: Player? = null
): Result<Unit> = eval(tokens).map { it.run(sender) }.also {
    it.exceptionOrNull()?.message?.let { message ->
        sender?.sendMessage(message) ?: println(message)
    }
}

@CommandDsl
public interface Command {

    public val nodes: MutableList<Command>
    public val executor: CommandExecutor

    public fun matches(tokens: List<String>): Int

    public fun consume(
        consumedTokens: List<String>,
        args: MutableList<Any>,
        mappings: MutableList<String?>
    )

    public fun canUse(sender: Player?): Boolean
    
    public object Root : Command by command("root")
    
    public companion object CommandRegistry : Registry<Command> {
        
        override val list: MutableList<Command> get() = Root.nodes

        context(Plugin)
        override fun register(value: Command) {
            require(value is CommandNodeLiteral) { "Root node must be a literal node." }
            val root = CommandNodeRoot(this@Plugin, value)
            Root.nodes.add(root)
        }

        context(Plugin)
        override fun unregister(value: Command) {
            require(value is CommandNodeLiteral) { "Root node must be a literal node." }
            val root = CommandNodeRoot(this@Plugin, value)
            Root.nodes.remove(root)
        }

        override fun load() {
            with(Plugin.minecraft) {
                register(killCommand())
                register(stopCommand())
                register(echoCommand())
                register(complexCommand())
                register(sudoCommand())
                register(testCommand())
                register(executeCommand())
                register(listCommand())
                register(syntaxCommand())
            }
            with(Plugin.karrat) {
                register(installCommand())
            }
        }
        
        public fun eval(command: String): Result<EvaluatedCommand> =
            Root.eval(command.split(' '))

        public fun run(command: String, sender: Player? = null): Result<Unit> =
            Root.run(command.split(' '), sender)

    }
    
    public fun resolveNextNode(tokens: List<String>): Pair<Command, Int>? =
        nodes.fold<Command, Pair<Command, Int>?>(null) { best, it ->
            val result = it.matches(tokens)
            if (result != -1 && (best == null || best.second < result)) Pair(it, result) else best
        }
    
    public fun onRun(block: CommandScope.() -> Unit): Command =
        also { executor.globalExecutor = block }
    
    public fun onRunByConsole(block: CommandScope.() -> Unit): Command =
        also { executor.consoleExecutor = block }
    
    public fun onRunByPlayer(block: PlayerCommandScope.() -> Unit): Command =
        also { executor.playerExecutor = block }
    
}

@CommandDsl
internal class CommandNodeRoot(
    val plugin: Plugin,
    val command: CommandNodeLiteral
) : Command by command {

    override fun matches(tokens: List<String>): Int =
        if (command.literals.any {  // pretty sure this works
                it == tokens.first() || "${plugin.id}:$it" == tokens.first()
        }) 1 else -1

}

@CommandDsl
@PublishedApi
internal class CommandNodeLiteral @PublishedApi internal constructor(
    val literals: List<String>,
) : Command {

    override val nodes: MutableList<Command> = mutableListOf()
    override val executor: CommandExecutor = CommandExecutor()

    override fun matches(tokens: List<String>): Int =
        if (literals.any { it == tokens.first() }) 1 else -1

    override fun consume(
        consumedTokens: List<String>,
        args: MutableList<Any>,
        mappings: MutableList<String?>
    ) { /* Ignore */ }

    override fun canUse(sender: Player?): Boolean {
        return true
    }
}

@CommandDsl
@PublishedApi
internal class CommandNodeRedirect @PublishedApi internal constructor(
    val redirectNode: Command,
) : Command by redirectNode {
    
    override fun matches(tokens: List<String>): Int = 0
    
}


@CommandDsl
@PublishedApi
@OptIn(ExperimentalSerializationApi::class)
internal class CommandNodeArgument<T : Any> @PublishedApi internal constructor(
    val serializer: KSerializer<T>,
    val mapTo: String? = null,
    val label: String? = null
) : Command {

    companion object {
        inline operator fun <reified T : Any> invoke(mapTo: String?, label: String?) =
            CommandNodeArgument<T>(serializer(), mapTo, label)
    }

    override val nodes: MutableList<Command> = mutableListOf()
    override val executor: CommandExecutor = CommandExecutor()

    override fun matches(tokens: List<String>): Int {
        if (serializer.descriptor.kind is StructureKind.LIST) {
            val descriptor = serializer.descriptor.elementDescriptors.first()
            var matches = 0
            tokens.forEach {
                if (checkMatch(descriptor, ArrayDeque(listOf(it)))) { matches++ }
                else return matches
            }
            return matches
        }
        val descriptor = serializer.descriptor
        val size = decompiledElementsCount(descriptor)
        if (size > tokens.size) return -1
        return if (checkMatch(descriptor, ArrayDeque(tokens))) size else -1
    }

    override fun consume(
        consumedTokens: List<String>,
        args: MutableList<Any>,
        mappings: MutableList<String?>
    ) {
        val decoder = CommandDecoder(ArrayDeque(consumedTokens))
        args.add(serializer.deserialize(decoder))
        mappings.add(mapTo)
    }

    override fun canUse(sender: Player?): Boolean {
        return true
    }

    private fun checkMatch(descriptor: SerialDescriptor, tokens: ArrayDeque<String>): Boolean {
        return if (descriptor.kind is PrimitiveKind) {
            ensurePrimitiveMatch(tokens.removeFirst(), descriptor.kind as PrimitiveKind)
        }
        else descriptor.elementDescriptors.all { checkMatch(it, tokens) }
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
                else -> { return false /* Unexpected argument type. */ }
            }
        }.isSuccess
    }

    private fun decompiledElementsCount(descriptor: SerialDescriptor): Int =
        if (descriptor.kind is PrimitiveKind) 1
        else descriptor.elementDescriptors.sumOf { decompiledElementsCount(it) }

}
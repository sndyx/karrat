/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.struct

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.karrat.Server
import org.karrat.command.Command
import org.karrat.entity.Player
import org.karrat.plugin.Plugin
import org.karrat.plugin.karrat
import org.karrat.server.schedule
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

public typealias Message = CharSequence

public fun Message.unformatted(): String = toString()

public fun Message.formatted(): String {
    // TODO: json
    TODO()
}

@Serializable
public data class StyledText(
    val content: String,
    val bold: Boolean? = null,
    val italic: Boolean? = null,
    val underlined: Boolean? = null,
    val strikethrough: Boolean? = null,
    val obfuscated: Boolean? = null,
    val color: Color? = null,
    val insertion: String? = null,
    val clickEvent: ClickEvent? = null,
    val hoverEvent: HoverEvent? = null
)

@Serializable
public data class ClickEvent(val action: ClickAction, val value: String)

@Serializable
public enum class ClickAction {

    @SerialName("open_url")
    OpenUrl,
    @SerialName("run_command")
    RunCommand,
    @SerialName("suggest_command")
    SuggestCommand,
    @SerialName("change_page")
    ChangePage,
    @SerialName("copy_to_clipboard")
    CopyToClipboard

}

@Serializable
public data class HoverEvent(val action: HoverAction, val value: String)

@Serializable
public enum class HoverAction {

    @SerialName("show_text")
    ShowText,
    @SerialName("show_item")
    ShowItem,
    @SerialName("show_entity")
    ShowEntity

}

@Serializable
public class StyledMessage(
    internal val text: StyledText,
    internal val extra: List<StyledText>
) : Message by text.content + extra.joinToString(separator = "", transform = { it.content }) {

    @Transient
    public val segments: List<StyledText> = listOf(text) + extra

}

public class MessageBuilder {
    public val builders: MutableList<TextBuilder> = mutableListOf()
    public fun text(content: String): TextBuilder {
        val builder = TextBuilder(content)
        builders.add(builder)
        return builder
    }
    internal fun build(): StyledMessage {
        check(builders.isNotEmpty())
        val texts = builders.map { it.build() }
        return StyledMessage(texts.first(), texts.drop(1))
    }
}

public class TextBuilder(
    public val content: String,
) {
    public var bold: Boolean? = null
    public var italic: Boolean? = null
    public var underlined: Boolean? = null
    public var strikethrough: Boolean? = null
    public var obfuscated: Boolean? = null
    public var color: Color? = null
    public var insertion: String? = null
    public var clickEvent: ClickEvent? = null
    public var hoverEvent: HoverEvent? = null

    public fun bold(): TextBuilder = also { bold = true }
    public fun italic(): TextBuilder = also { italic = true }
    public fun underlined(): TextBuilder = also { underlined = true }
    public fun strikethrough(): TextBuilder = also { strikethrough = true }
    public fun obfuscated(): TextBuilder = also { obfuscated = true }
    public fun color(value: Color): TextBuilder = also { color = value }
    public fun insert(text: String): TextBuilder = also { insertion = text }

    public fun onClick(
        url: String? = null,
        command: String? = null,
        suggestCommand: String? = null,
        page: Int? = null,
        clipboard: String? = null,
        expireAfter: Duration? = null,
        action: ((Player) -> Unit)? = null
    ): TextBuilder {
        clickEvent = when {
            url != null -> ClickEvent(ClickAction.OpenUrl, url)
            command != null -> ClickEvent(ClickAction.RunCommand, command)
            suggestCommand != null -> ClickEvent(ClickAction.SuggestCommand, suggestCommand)
            page != null -> ClickEvent(ClickAction.ChangePage, page.toString())
            clipboard != null -> ClickEvent(ClickAction.CopyToClipboard, clipboard)
            action != null -> {
                val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
                val name = (1..32)
                    .map { allowedChars.random() }
                    .joinToString("")
                val cmd = org.karrat.command.command(name).onRunByPlayer {
                    action(sender)
                }
                with(Plugin.karrat) {
                    Command.register(cmd)
                    (expireAfter ?: 1.minutes).let {
                        Server.schedule(it) {
                            Command.unregister(cmd)
                        }
                    }
                }
                ClickEvent(ClickAction.RunCommand, "/$name")
            }
            else -> null
        }
        return this
    }

    public fun onHover(): TextBuilder {
        TODO()
    }

    internal fun build(): StyledText = StyledText(
        content,
        bold,
        italic,
        underlined,
        strikethrough,
        obfuscated,
        color,
        insertion,
        clickEvent,
        hoverEvent
    )

}

public fun Message(
    text: String
): Message {
    return text
}

public fun Message(
    builder: MessageBuilder.() -> Unit
): Message {
    return MessageBuilder().apply(builder).build()
}
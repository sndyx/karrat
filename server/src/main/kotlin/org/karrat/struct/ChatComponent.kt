/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.struct

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

//TODO custom optimized serialization
@Serializable
public sealed class ChatComponent: Style() {
    public var clickEvent: ClickEvent? = null
    public var hoverEvent: HoverEvent? = null

    public val attributes: TextAttributes = TextAttributes()

    public fun clickEvent(clickEvent: ClickEvent): ChatComponent {
        this.clickEvent = clickEvent
        return this
    }

    public fun hoverEvent(hoverEvent: HoverEvent): ChatComponent {
        this.hoverEvent = hoverEvent
        return this
    }

    public fun attributes(modifier: TextAttributes.() -> Unit): ChatComponent {
        modifier(attributes)
        return this
    }

    public open fun append(component: ChatComponent): ChatComponent {
        return ChatComponentBase().append(this).append(component)
    }
}

@Serializable
public class ChatComponentBase : ChatComponent() {
    public val extra: MutableList<ChatComponent> = mutableListOf()

    override fun append(component: ChatComponent): ChatComponentBase {
        extra.add(component)
        return this
    }
}

@Serializable
public class TextComponent : ChatComponent {
    public var text: String

    public constructor(text: String) : super() {
        this.text = text
    }
}

//TODO enforce valid translation key
@Serializable
public class TranslatableComponent : ChatComponent {
    public var translate: String
    public val with: List<ChatComponent> = mutableListOf()

    public constructor(translate: String) : super() {
        this.translate = translate
    }

    public fun with(modifier: List<ChatComponent>.() -> Unit): TranslatableComponent {
        modifier(with)
        return this
    }

}

//TODO enforce valid keybinds
@Serializable
public class KeybindComponent : ChatComponent {
    public var keybind: String

    public constructor(keybind: String) : super() {
        this.keybind = keybind
    }
}

//TODO enforce valid name, objective
//TODO allow or force dynamically loading of value
@Serializable
public class ScoreComponent : ChatComponent {
    public val name: String
    public val objective: String
    public var value: String? = null

    public constructor (
        name: String,
        objective: String,
    ) : super() {
        this.name = name
        this.objective = objective
    }

    public fun value(value: String): ScoreComponent {
        this.value = value
        return this
    }

}

//TODO parse target selector
@Serializable
public class SelectorComponent : ChatComponent {
    public val targetSelector: String

    public constructor (
        targetSelector: String,
    ) : super() {
        this.targetSelector = targetSelector
    }
}

@Serializable
public class TextAttributes {
    public var insertion: String? = null
    public var hoverEvent: HoverEvent? = null
    public var clickEvent: ClickEvent? = null

    public fun onShiftClick(insertion: String): TextAttributes {
        this.insertion = insertion
        return this
    }

    public fun onHover(hoverEvent: HoverEvent): TextAttributes {
        this.hoverEvent = hoverEvent
        return this
    }

    public fun onClick(clickEvent: ClickEvent): TextAttributes {
        this.clickEvent = clickEvent
        return this
    }
}

@Serializable
public class ClickEvent(public val action: ClickAction, public val value: String)

@Serializable
public enum class ClickAction {
    @SerialName("open_url")
    OpenURL,
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
public class HoverEvent(public val action: HoverAction, public val value: String)

@Serializable
public enum class HoverAction {
    @SerialName("show_text")
    DisplayText,

    @SerialName("show_item")
    DisplayItem,

    @SerialName("show_entity")
    DisplayEntity
}

//TODO color enforcement

@Serializable
public open class Style {
    private companion object Empty {
        val Empty: Style = Style()
    }

    public var bold: Boolean? = null
    public var italic: Boolean? = null
    public var underlined: Boolean? = null
    public var strikethrough: Boolean? = null
    public var obfuscated: Boolean? = null
    public var color: String? = null

    public fun bold(bold: Boolean): Style {
        this.bold = bold
        return this
    }

    public fun italic(italic: Boolean): Style {
        this.italic = italic
        return this
    }

    public fun underlined(underlined: Boolean): Style {
        this.underlined = underlined
        return this
    }

    public fun strikethrough(strikethrough: Boolean): Style {
        this.strikethrough = strikethrough
        return this
    }

    public fun obfuscated(obfuscated: Boolean): Style {
        this.obfuscated = obfuscated
        return this
    }

    public fun copy(other: Style) {
        this.bold = other.bold
        this.italic = other.italic
        this.underlined = other.underlined
        this.strikethrough = other.strikethrough
        this.obfuscated = other.obfuscated
        this.color = other.color
    }
}
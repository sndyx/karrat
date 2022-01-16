/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.play

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

//TODO custom optimized serialization
@Serializable
public sealed class ChatComponentBase {
    public val style: Style
    protected val clickEvent: ClickEvent
    protected val hoverEvent: HoverEvent

    protected val attributes: TextAttributes = TextAttributes()

    protected constructor(
        style: Style,
        clickEvent: ClickEvent,
        hoverEvent: HoverEvent,
    ) {
        this.style = style
        this.clickEvent = clickEvent
        this.hoverEvent = hoverEvent
    }
}

@Serializable
public class MainChatComponent : ChatComponentBase {
    public val extra: List<ChatComponentBase> = mutableListOf()

    public constructor(style: Style,
                       clickEvent: ClickEvent,
                       hoverEvent: HoverEvent) : super(style, clickEvent, hoverEvent)
}

@Serializable
public class TextComponent : ChatComponentBase {
    public var text: String

    public constructor(text: String,
                    style: Style,
                    clickEvent: ClickEvent,
                    hoverEvent: HoverEvent) : super(style, clickEvent, hoverEvent) {
        this.text = text
    }
}

@Serializable
public class Style(public val bold: Boolean,
                   public val italic: Boolean,
                   public val underlined: Boolean,
                   public val strikethrough: Boolean,
                   public val obfuscated: Boolean,
                   public val color: String)

//TODO enforce valid translation key
@Serializable
public class TranslatableComponent : ChatComponentBase {
    public var translate: String
    public val with: List<ChatComponentBase> = mutableListOf()

    public constructor(translate: String,
                       style: Style,
                       clickEvent: ClickEvent,
                       hoverEvent: HoverEvent) : super(style, clickEvent, hoverEvent) {
        this.translate = translate
    }
}

//TODO enforce valid keybinds
@Serializable
public class KeybindComponent : ChatComponentBase {
    public var keybind: String

    public constructor(keybind: String,
                       style: Style,
                       clickEvent: ClickEvent,
                       hoverEvent: HoverEvent) : super(style, clickEvent, hoverEvent) {
        this.keybind = keybind
    }
}

//TODO enforce valid name, objective
//TODO allow dynamically loading of value
@Serializable
public class ScoreComponent : ChatComponentBase {
    public val name: String
    public val objective: String
    public val value: String

    public constructor (
        name: String,
        objective: String,
        value: String,
        style: Style,
        clickEvent: ClickEvent,
        hoverEvent: HoverEvent
        ) : super(style, clickEvent, hoverEvent) {
        this.name = name
        this.objective = objective
        this.value = value
    }

    public constructor (
        name: String,
        objective: String,
        style: Style,
        clickEvent: ClickEvent,
        hoverEvent: HoverEvent
    ) : this(name, objective, "", style, clickEvent, hoverEvent)
}

//TODO parse target selector
@Serializable
public class SelectorComponent : ChatComponentBase {
    public val targetSelector: String

    public constructor (
        targetSelector: String,
        style: Style,
        clickEvent: ClickEvent,
        hoverEvent: HoverEvent
    ) : super(style, clickEvent, hoverEvent) {
        this.targetSelector = targetSelector
    }
}

@Serializable
public class TextAttributes {
    public fun onShiftClick(insertion: String) {

    }

    public fun onHover(hoverEvent: HoverEvent) {

    }

    public fun onClick(clickEvent: ClickEvent) {

    }
}

@Serializable
public class ClickEvent(public val action: ClickAction, public val value: String)

@Serializable
public enum class ClickAction {
    @SerialName("open_url") OpenURL,
    @SerialName("run_command") RunCommand,
    @SerialName("suggest_command") SuggestCommand,
    @SerialName("change_page") ChangePage,
    @SerialName("copy_to_clipboard") CopyToClipboard
}

@Serializable
public class HoverEvent(public val action: HoverAction, public val value: String)

@Serializable
public enum class HoverAction {
    @SerialName("show_text") displayText,
    @SerialName("show_item") displayItem,
    @SerialName("show_entity") displayEntity,
}
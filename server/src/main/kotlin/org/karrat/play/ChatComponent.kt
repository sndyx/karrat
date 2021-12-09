/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.play

import kotlinx.serialization.Serializable
import org.karrat.entity.Entity

@Serializable
public sealed class ChatComponentBase {
    public val extra: List<ChatComponent> = arrayListOf()
}


@Serializable
public class ChatComponent: ChatComponentBase {
    public lateinit var text : String
    
    /*private val bold: Boolean
    private val italic: Boolean
    private val underlined: Boolean
    private val strikethrough: Boolean
    private val obfuscated: Boolean
    private val color: String
    private val insertion: String
    private val clickEvent: ClickEvent
    private val hoverEvent: HoverEvent
    */
    
    public constructor(text: String) {
        this.text = text
    }
    
    public constructor(builder: Builder.() -> Unit)
    
    @Serializable
    public class ClickEvent(public val action: String, public val value: String)
    
    @Serializable
    public class HoverEvent(public val action: String, public val value: String)
    
    public class Builder {
        
        public fun text(text: String, attributes: TextAttributes.() -> Unit = { }) {
        
        }
        
    }
    
    public class TextAttributes {
    
        public fun onHoverDisplayText(text: String) {
        
        }
        
        public fun onHoverDisplayItem(item: ItemStack) {
        
        }
        
        public fun onHoverDisplayEntity(entity: Entity) {
        
        }
        
        public fun onClickOpenUrl(url: String) {
        
        }
        
        public fun onClickSendMessage(message: String) {
        
        }
        
        public fun onClickSuggestCommand(suggestion: String) {
        
        }
        
        public fun onClickRun(block: () -> Unit) {
        
        }
        
    }

}
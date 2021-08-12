/*
 * This file is part of Karrat.
 *
 * Karrat is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Karrat is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Karrat.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.karrat.util

import kotlinx.serialization.Serializable
import org.karrat.entity.Entity
import org.karrat.item.ItemStack

@Serializable
class ChatComponent {
    
    /*private val bold: Boolean
    private val italic: Boolean
    private val underlined: Boolean
    private val strikethrough: Boolean
    private val obfuscated: Boolean
    private val color: String
    private val insertion: String
    private val clickEvent: ClickEvent
    private val hoverEvent: HoverEvent
    private val extra: List<ChatComponent>*/
    
    constructor(text: String)
    
    constructor(builder: Builder.() -> Unit) {
    }
    
    @Serializable
    class ClickEvent(val action: String, val value: String)
    
    @Serializable
    class HoverEvent(val action: String, val value: String)
    
    class Builder {
        
        val black = "§0"
        val blue = "§1"
        val green = "§2"
        val darkAqua = "§3"
        val red = "§4"
        val purple = "§5"
        val gold = "§6"
        val lightGray = "§7"
        val gray = "§8"
        val lightBlue = "§9"
        val lime = "§a"
        val aqua = "§b"
        val lightRed = "§c"
        val magenta = "§d"
        val yellow = "§e"
        val white = "§f"
        
        fun text(text: String, attributes: TextAttributes.() -> Unit = { }) {
        
        }
        
    }
    
    class TextAttributes {
    
        fun onHoverDisplayText(text: String) {
        
        }
        
        fun onHoverDisplayItem(item: ItemStack) {
        
        }
        
        fun onHoverDisplayEntity(entity: Entity) {
        
        }
        
        fun onClickOpenUrl(url: String) {
        
        }
        
        fun onClickSendMessage(message: String) {
        
        }
        
        fun onClickSuggestCommand(suggestion: String) {
        
        }
        
        fun onClickRun(block: () -> Unit) {
        
        }
        
    }

}
package org.karrat.test

import org.karrat.item.nbt.NbtCompound
import org.karrat.util.ChatComponent

class Test {
    
    fun test() {
        val component = ChatComponent {
            text("&eEpic &lGaming") {
                onClickRun {
                    println("Player clicked message!")
                }
                onHoverDisplayText("Text")
            }
            text("Quite epic.")
        }
        
        val compound = NbtCompound()
        val y = compound.get<NbtCompound>("gamers").get<String>("rise")
        
    }
    
}
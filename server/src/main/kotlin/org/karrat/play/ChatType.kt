/*
 * Copyright © Karrat - 2022.
 */

package org.karrat.play

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.karrat.struct.NbtCompound
import org.karrat.struct.toNbtCompound

public class ChatType {

    // TODO: THIS BULLSHIT!!!!!! i hate NEW CHAT!!!

    public companion object {

        public val codec: NbtCompound = """
  {
    "value": [
      {
        "name": "minecraft:chat",
        "id": 0,
        "element": {
          "chat": {
            "decoration": {
              "parameters": [
                "sender",
                "content"
              ],
              "translation_key": "chat.type.text",
              "style": {}
            }
          },
          "narration": {
            "decoration": {
              "parameters": [
                "sender",
                "content"
              ],
              "translation_key": "chat.type.text.narrate",
              "style": {}
            },
            "priority": "chat"
          }
        }
      },
      {
        "element": {
          "narration": {
            "priority": "system"
          },
          "chat": {}
        },
        "name": "minecraft:system",
        "id": 1
      },
      {
        "id": 2,
        "name": "minecraft:game_info",
        "element": {
          "overlay": {}
        }
      },
      {
        "element": {
          "narration": {
            "priority": "chat",
            "decoration": {
              "style": {},
              "parameters": [
                "sender",
                "content"
              ],
              "translation_key": "chat.type.text.narrate"
            }
          },
          "chat": {
            "decoration": {
              "style": {},
              "parameters": [
                "sender",
                "content"
              ],
              "translation_key": "chat.type.announcement"
            }
          }
        },
        "id": 3,
        "name": "minecraft:say_command"
      },
      {
        "id": 4,
        "element": {
          "chat": {
            "decoration": {
              "translation_key": "commands.message.display.incoming",
              "style": {
                "italic": 1,
                "color": "gray"
              },
              "parameters": [
                "sender",
                "content"
              ]
            }
          },
          "narration": {
            "priority": "chat",
            "decoration": {
              "translation_key": "chat.type.text.narrate",
              "style": {},
              "parameters": [
                "sender",
                "content"
              ]
            }
          }
        },
        "name": "minecraft:msg_command"
      },
      {
        "name": "minecraft:team_msg_command",
        "id": 5,
        "element": {
          "narration": {
            "priority": "chat",
            "decoration": {
              "style": {},
              "parameters": [
                "sender",
                "content"
              ],
              "translation_key": "chat.type.text.narrate"
            }
          },
          "chat": {
            "decoration": {
              "translation_key": "chat.type.team.text",
              "style": {},
              "parameters": [
                "team_name",
                "sender",
                "content"
              ]
            }
          }
        }
      },
      {
        "id": 6,
        "element": {
          "chat": {
            "decoration": {
              "translation_key": "chat.type.emote",
              "style": {},
              "parameters": [
                "sender",
                "content"
              ]
            }
          },
          "narration": {
            "priority": "chat",
            "decoration": {
              "style": {},
              "translation_key": "chat.type.emote",
              "parameters": [
                "sender",
                "content"
              ]
            }
          }
        },
        "name": "minecraft:emote_command"
      },
      {
        "element": {
          "chat": {},
          "narration": {
            "priority": "chat"
          }
        },
        "id": 7,
        "name": "minecraft:tellraw_command"
      }
    ],
    "type": "minecraft:chat_type"
  }
            """.trimIndent().let {
            Json.decodeFromString<Map<String, Any>>(it).toNbtCompound()
        }
    }

}
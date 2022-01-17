/*
 * Copyright © Karrat - 2021.
 */

package org.karrat.world.dimensions

import org.karrat.struct.ResourceLocation

public class DimensionCodec {
    public val dimensionRegistry: DimensionRegistry = DimensionRegistry()
    public val biomeRegistry: BiomeRegistry = BiomeRegistry()

    public class DimensionRegistry {
        public val dimensions: List<Entry> = mutableListOf()

        public class Entry(public val name: String, public val id: Int, public val element: EntryType)

        public class EntryType {
            public var piglinSafe: Byte = 0
            public var natural: Byte = 0
            public var ambientLight: Float = 0f
            public var fixedTime: Long? = null
            public var infiniburn: ResourceLocation = ResourceLocation.Empty
            public var respawnAnchorsWork: Byte = 0
            public var effects: String = "" //???
            public var hasRaids: Byte = 0
            public var minY: Int = 0
            public var height: Int = 0
            public var logicalHeight: Int = 0
            public var coordinateScale: Float = 0f
            public var ultrawarm: Byte = 0
            public var hasCeiling: Byte = 0
        }
    }

    public class BiomeRegistry {

    }
}

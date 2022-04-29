import org.karrat.World
import org.karrat.play.Vec2i
import org.karrat.world.Block
import org.karrat.world.Chunk

public interface WorldGenerator {
    
    public fun generateChunk(world: World, pos: Vec2i): Chunk
    
    public object Default : WorldGenerator {
    
        override fun generateChunk(world: World, pos: Vec2i): Chunk {
            val chunk = Chunk(world.height)
            repeat(16) { x ->
                repeat(16) { z ->
                    chunk[x, 0, z] = Block.Stone
                }
            }
            return chunk
        }
    
    }
    
}

public interface WorldGenerator {
    
    val world: World
    
    fun generateChunk(x: Int, y: Int): Chunk
    
}

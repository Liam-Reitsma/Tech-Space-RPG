package game.world.model.generation;

public class WorldGenConfig
{
    private WorldGenConfig() {}
    
    public static final int MIN_RANGE = 8;
    public static final int MAX_RANGE = 15;
    public static final int TERMINATION_CHANCE = 30;
    public static final int MIN_ROOM_COUNT = MIN_RANGE * MIN_RANGE;
    public static final int MAX_CLUMPING = 3;
    public static final int DOUBLE_ROOM_THRESHOLD = 4;
    public static final int TRIPLE_ROOM_THRESHOLD = 8;
    public static final int BASE_CHANCE = 6;
    public static final int GAIN_CHANCE = 6;
}
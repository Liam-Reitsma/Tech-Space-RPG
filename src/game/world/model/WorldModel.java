package game.world.model;

import static game.world.model.generation.WorldGenConfig.MIN_RANGE;
import static game.world.model.room.RoomTileState.*;
import static game.world.model.room.RoomType.*;
import static game.world.model.generation.WorldGenConfig.MAX_RANGE;

import game.world.model.generation.GenerationRoom;
import game.world.model.generation.WorldGenerator;
import game.world.model.room.RoomState;
import game.world.model.room.RoomType;
import game.world.model.room.RuntimeRoom;

import java.util.*;

public class WorldModel
{
    public static final int VIEWABLE_RANGE = 5;
    
    private final Random random;
    private final WorldGenerator worldGenerator;
    
    public WorldModel()
    {
        random = new Random();
        worldGenerator = new WorldGenerator();
    }

    /**
     * Generates a world with an empty starting room, randomly assigned room types,
     * a boss room towards the outer boundaries and walls surrounding accessible rooms.
     *
     * @return A new map containing the generated world's rooms and their coordinates.
     */
    public Map<Point, RuntimeRoom> generateWorld()
    {
        Map<Point, GenerationRoom> generatedMap = worldGenerator.generate();
        Map<Point, RuntimeRoom> world = new HashMap<>();
        if(generatedMap.isEmpty())
            return world;
        
        for(Point point : generatedMap.keySet())
        {
            int chance = random.nextInt(100);
            if(point.getX() == 0 && point.getY() == 0)
                world.put(point, new RuntimeRoom(point, new RoomState(EMPTY)));
            else if(chance < 5  && checkRoomToBuild(world, point, MINIBOSS))
                world.put(point, new RuntimeRoom(point, new RoomState(MINIBOSS)));
            else if(chance < 25 && checkRoomToBuild(world, point, CHEST))
                world.put(point, new RuntimeRoom(point, new RoomState(CHEST)));
            else if(chance < 40 && checkRoomToBuild(world, point, EMPTY))
                world.put(point, new RuntimeRoom(point, new RoomState(EMPTY)));
            else
                world.put(point, new RuntimeRoom(point, new RoomState(COMBAT)));
        }
        
        do
        {
            int minRange = -MAX_RANGE + MIN_RANGE;
            int maxRange = MAX_RANGE - MIN_RANGE;
            Point randomPoint = new Point(random.nextInt(-MAX_RANGE, MAX_RANGE + 1), random.nextInt(-MAX_RANGE, MAX_RANGE + 1));
            if(generatedMap.containsKey(randomPoint) && (randomPoint.getX() < minRange || randomPoint.getX() > maxRange || randomPoint.getY() < minRange || randomPoint.getY() > maxRange))
            {
                world.put(randomPoint, new RuntimeRoom(randomPoint, new RoomState(BOSS)));
                break;
            }
        } while(true);
        
        for(int y = 0; y < MAX_RANGE * 2 + 1; y++)
            for(int x = 0; x < MAX_RANGE * 2 + 1; x++)
            {
                Point point = new Point(x - MAX_RANGE, (-y) + MAX_RANGE);
                world.put(point, world.getOrDefault(point, new RuntimeRoom(point, new RoomState(WALL))));
            }
        
        return world;
    }

    /**
     * Checks the four orthogonally adjacent coordinates.
     *
     * @param world The map of rooms already created.
     * @param point The proposed room position.
     * @param roomType The room type being considered.
     * @return True if no existing adjacent room shares the requested type; otherwise false.
     */
    private boolean checkRoomToBuild(Map<Point, RuntimeRoom> world, Point point, RoomType roomType)
    {
        RuntimeRoom testRoom;
        
        for(int i = -1; i < 2; i += 2)
        {
            testRoom = world.get(new Point(point.getX() + i, point.getY()));
            if(testRoom != null)
                if(testRoom.getRoomState().getRoomType() == roomType)
                    return false;
            
            testRoom = world.get(new Point(point.getX(), point.getY() + i));
            if(testRoom != null)
                if(testRoom.getRoomState().getRoomType() == roomType)
                    return false;
        }
        
        return true;
    }

    /**
     * Finds directions leading to adjacent, non-wall rooms, regardless of visitation state.
     *
     * @param world The world containing the rooms.
     * @param playerLocation The reference position for movement.
     * @return An array of directions leading to accessible adjacent rooms.
     */
    public Direction[] getPossibleDirections(Map<Point, RuntimeRoom> world, Point playerLocation)
    {
        List<Direction> directions = new ArrayList<>();
        
        for(Point orthogonalPoint : Point.getOrthogonalPoints(playerLocation))
            if(world.containsKey(orthogonalPoint) && world.get(orthogonalPoint).getRoomState().getRoomType() != WALL)
                directions.add(orthogonalPoint.returnRelativeDirection(playerLocation));
        
        return directions.toArray(new Direction[0]);
    }

    /**
     * Converts the world into a 2D array and marks previously unseen rooms within the player's viewing range as seen.
     *
     * @param world The map containing the world's rooms.
     * @param playerPoint The player's current position.
     * @return A two-dimensional array of rooms representing the world.
     */
    public RuntimeRoom[][] updateWorldInfo(Map<Point, RuntimeRoom> world, Point playerPoint)
    {
        RuntimeRoom[][] roomMap = new RuntimeRoom[MAX_RANGE * 2 + 1][MAX_RANGE * 2 + 1];
        
        for(int y = 0; y < MAX_RANGE * 2 + 1; y++)
            for(int x = 0; x < MAX_RANGE * 2 + 1; x++)
                roomMap[y][x] = world.get(new Point(x - MAX_RANGE, (-y) + MAX_RANGE));
        
        int viewingYLowRange = (-playerPoint.getY()) + MAX_RANGE - (VIEWABLE_RANGE / 2);
        int viewingYTopRange = (-playerPoint.getY()) + MAX_RANGE + 1 + (VIEWABLE_RANGE / 2);
        
        int viewingXLowRange = playerPoint.getX() + MAX_RANGE - (VIEWABLE_RANGE / 2);
        int viewingXTopRange = playerPoint.getX() + MAX_RANGE + 1 + (VIEWABLE_RANGE / 2);
        
        for(int y = viewingYLowRange; y < viewingYTopRange; y++)
            for(int x = viewingXLowRange; x < viewingXTopRange; x++)
            {
                if(y < 0 || y >= MAX_RANGE * 2 + 1)
                    continue;
                if(x < 0 || x >= MAX_RANGE * 2 + 1)
                    continue;
                if(roomMap[y][x].getRoomState().getRoomTileState() == UNSEEN)
                    roomMap[y][x].setSeen();
            }
        
        return roomMap;
    }
}
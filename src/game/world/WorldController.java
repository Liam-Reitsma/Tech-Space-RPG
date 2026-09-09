package game.world;

import static game.world.RoomType.*;
import game.Loopable;
import game.Startable;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class WorldController implements Startable<WorldResult>, Loopable<WorldResult>
{
    private final Random random;
    private final WorldGenerator worldGenerator;
    private final Map<Point, RuntimeRoom> world;
    
    public WorldController()
    {
        random = new Random();
        worldGenerator = new WorldGenerator();
        world = new HashMap<>();
    }
    
    public WorldResult loadMapFromSave(Map<Point, RuntimeRoom> loadedWorld)
    {
        for(Map.Entry<Point, RuntimeRoom> room : loadedWorld.entrySet())
        {
            if(room.getKey() == null) return new WorldResult(WorldErrors.LOADING_WORLD_ERROR, null);
            world.put(room.getKey(), room.getValue());
        }
        
        return new WorldResult(WorldErrors.SUCCESS, world);
    }
    
    @Override
    public WorldResult start()
    {
        Map<Point, GenerationRoom> generatedMap = worldGenerator.generate();
        
        if(generatedMap.isEmpty()) return new WorldResult(WorldErrors.GENERATING_ERROR, null);
        
        for(Point point : generatedMap.keySet())
        {
            int chance = random.nextInt(100);
            if(chance < 5  && checkRoomToBuild(point, MINIBOSS)) world.put(point, new RuntimeRoom(point, MINIBOSS));
            else if(chance < 25 && checkRoomToBuild(point, CHEST)) world.put(point, new RuntimeRoom(point, CHEST));
            else if(chance < 40 && checkRoomToBuild(point, EMPTY)) world.put(point, new RuntimeRoom(point, EMPTY));
            else world.put(point, new RuntimeRoom(point, COMBAT));
        }
        
        return new WorldResult(WorldErrors.SUCCESS, world);
    }
    
    private boolean checkRoomToBuild(Point point, RoomType roomType)
    {
        RuntimeRoom testRoom;
        
        for(int i = -1; i < 2; i += 2)
        {
            testRoom = world.get(new Point(point.getX() + i, point.getY()));
            if(testRoom != null)
            {
                if(testRoom.getRoomType() == roomType) return false;
            }
            
            testRoom = world.get(new Point(point.getX(), point.getY() + i));
            if(testRoom != null)
            {
                if(testRoom.getRoomType() == roomType) return false;
            }
        }
        
        return true;
    }
    
    @Override
    public WorldResult loop()
    {
        return null;
    }
}

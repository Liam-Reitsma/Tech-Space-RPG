package game.world;

import static game.world.WorldErrors.*;

import game.ConsoleHandler;
import game.Loopable;

import java.util.Optional;

public class RoomController implements Loopable<RuntimeRoom, RuntimeRoom[], WorldResult>
{
    private final ConsoleHandler consoleH;
    
    public RoomController(ConsoleHandler consoleHandler)
    {
        consoleH = consoleHandler;
    }
    
    @Override
    public WorldResult loop(RuntimeRoom room, RuntimeRoom[] adjacentRooms)
    {
        if(room.isCompleted()) return determineNextRoom(room, adjacentRooms);
        
        return new WorldResult(SUCCESS, null);
    }
    
    private WorldResult determineNextRoom(RuntimeRoom room, RuntimeRoom[] adjacentRooms)
    {
        Direction[] directions = new Direction[adjacentRooms.length];
        int index = 0;
        for(RuntimeRoom adjRoom : adjacentRooms)
        {
            directions[index++] = room.getPoint().returnRelativeDirection(adjRoom.getPoint());
        }
        
        consoleH.printLine("You have " + directions.length + " options to travel to. Where would you like to go?");
        for(int i = 0; i < directions.length; i++)
        {
            if(adjacentRooms[i].isCompleted()) consoleH.printLine((i + 1) + ". " + directions[i] + " (Empty).");
            else consoleH.printLine((i + 1) + ". " + directions[i] + ".");
        }
        
        Optional<Integer> result = consoleH.nextInt().getInput();
        if(result.isEmpty()) return new WorldResult(USER_WENT_BACK, null);
        else return new WorldResult(SUCCESS, adjacentRooms[result.get()]);
    }
}

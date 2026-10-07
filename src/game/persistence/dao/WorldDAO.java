package game.persistence.dao;

import static game.persistence.PersistenceTable.ROOMS;
import static game.persistence.SQLCommandFactory.*;

import game.persistence.DBManager;
import game.world.model.Point;
import game.world.model.room.RuntimeRoom;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class WorldDAO
{
    private static final String SAVE_NAME_COLUMN = "SAVE_NAME";
    private static final String TABLE_NAME = ROOMS.name();
    private static final String[] COLUMNS = ROOMS.getColumnStrings();
    
    private final DBManager dbManager;
    
    public WorldDAO(DBManager db)
    {
        dbManager = db;
    }
    
    public void insertWorld(String saveName, Map<Point, RuntimeRoom> world)
    {
        for(Entry<Point, RuntimeRoom> worldEntry : world.entrySet())
        {
            Point pointEntry = worldEntry.getKey();
            RuntimeRoom roomEntry = worldEntry.getValue();
            Object[] args = new Object[]
                    {
                            saveName, pointEntry.getX(), pointEntry.getY(),
                            roomEntry.getRoomState().getRoomTileState().name(), roomEntry.getRoomState().getRoomType().name()
                    };
            
            dbManager.addEntryDB(buildInsertIntoSQL(TABLE_NAME, COLUMNS), args);
        }
    }
    
    public void updateMap(String saveName, Map<Point, RuntimeRoom> world)
    {
        dbManager.deleteEntryDB(buildDeleteAllRowSQL(TABLE_NAME, SAVE_NAME_COLUMN), saveName);
        
        insertWorld(saveName, world);
    }
    
    public void updateRoomStatus(String saveName, RuntimeRoom room)
    {
        Object[] args = new Object[]
                {
                        saveName, room.getPoint().getX(), room.getPoint().getY(),
                };
        String[] primaryKeys = ROOMS.getPrimaryKeys();
        String columnName = "ROOM_TILE_STATE";
        String newCellValue = room.getRoomState().getRoomTileState().name();
        
        dbManager.updateCellEntryDB(buildUpdateRowSQL(TABLE_NAME, columnName, primaryKeys), newCellValue, args);
    }
    
    public Map<Point, RuntimeRoom> loadSavedWorld(String saveName)
    {
        Object[][] loadedRoomsList = dbManager.queryRoomDB(buildSelectSQL(TABLE_NAME, SAVE_NAME_COLUMN), saveName);
        Map<Point, RuntimeRoom> world = new HashMap<>();
        
        for(Object[] loadedRoom : loadedRoomsList)
        {
            RuntimeRoom room = new RuntimeRoom(loadedRoom);
            world.put(room.getPoint(), room);
        }
        
        return world;
    }
}
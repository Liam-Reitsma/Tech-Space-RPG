package game.persistence.dao;

import static game.persistence.PersistenceTable.ROOMS;
import static game.persistence.SQLCommandFactory.*;

import game.persistence.DBManager;
import game.world.model.Point;
import game.world.model.room.RoomState;
import game.world.model.room.RoomTileState;
import game.world.model.room.RoomType;
import game.world.model.room.RuntimeRoom;

import java.util.HashMap;
import java.util.List;
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
    
    public boolean insertWorld(String saveName, Map<Point, RuntimeRoom> world)
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
            
            if(!dbManager.updateDB(buildInsertIntoSQL(TABLE_NAME, COLUMNS), args))
                return false;
        }
        
        return true;
    }
    
    public boolean updateMap(String saveName, Map<Point, RuntimeRoom> world)
    {
        if(!dbManager.updateDB(buildDeleteAllRowsSQL(TABLE_NAME, SAVE_NAME_COLUMN), saveName))
            return false;
        
        return insertWorld(saveName, world);
    }
    
    public boolean updateRoomStatus(String saveName, RuntimeRoom room)
    {
        Object[] args = new Object[]
                {
                        room.getRoomState().getRoomTileState().name(), saveName, room.getPoint().getX(), room.getPoint().getY(),
                };
        String[] primaryKeys = ROOMS.getPrimaryKeys();
        String columnName = "ROOM_TILE_STATE";
        
        return dbManager.updateDB(buildUpdateCellEntrySQL(TABLE_NAME, columnName, primaryKeys), args);
    }
    
    public Map<Point, RuntimeRoom> loadSavedWorld(String saveName)
    {
        List<Map<String, Object>> roomDataList = dbManager.queryDB(buildSelectSQL(TABLE_NAME, SAVE_NAME_COLUMN), saveName);
        Map<Point, RuntimeRoom> world = new HashMap<>();
        if(roomDataList == null)
            return null;
        if(roomDataList.isEmpty())
            return new HashMap<>();
        
        for(Map<String, Object> roomData : roomDataList)
        {
            Point point = new Point((int)roomData.get("X_POINT"), (int)roomData.get("Y_POINT"));
            RoomState roomState = new RoomState(RoomTileState.valueOf(roomData.get("ROOM_TILE_STATE").toString()), RoomType.valueOf(roomData.get("ROOM_TYPE").toString()));
            RuntimeRoom room = new RuntimeRoom(point, roomState);
            world.put(room.getPoint(), room);
        }
        
        return world;
    }
    
    public boolean deleteWorld(String saveName)
    {
        return dbManager.updateDB(buildDeleteRowSQL(TABLE_NAME, new String[]{SAVE_NAME_COLUMN}), saveName);
    }
}
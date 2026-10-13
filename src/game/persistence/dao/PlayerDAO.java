package game.persistence.dao;

import static game.persistence.PersistenceTable.*;
import static game.persistence.SQLCommandFactory.*;

import game.entities.player.ClassType;
import game.entities.player.Player;
import game.persistence.DBManager;
import game.world.model.Point;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class PlayerDAO
{
    private static final String SAVE_NAME_COLUMN = "SAVE_NAME";
    private static final String PLAYER_TABLE_NAME = PLAYER.name();
    private static final String STATS_TABLE_NAME = PLAYER_STATS.name();
    private static final String[] PLAYER_COLUMNS = PLAYER.getColumnStrings();
    private static final String[] STATS_COLUMNS = PLAYER_STATS.getColumnStrings();
    
    private final DBManager dbManager;
    
    public PlayerDAO(DBManager db)
    {
        dbManager = db;
    }
    
    public boolean insertPlayer(String saveName, Player player)
    {
        Object[] playerArgs = new Object[]
                {
                        saveName, player.getName(), player.getLevel(),
                        player.getHealth(), player.getXP(), player.getGold(),
                        player.getClassType().name(), player.getPoint().getX(), player.getPoint().getY()
                };
        if(!dbManager.updateDB(buildInsertIntoSQL(PLAYER_TABLE_NAME, PLAYER_COLUMNS), playerArgs))
            return false;
        
        Object[] statArgs = new Object[]
                {
                        saveName, player.getSTR(), player.getDEX(), player.getINT()
                };
        return dbManager.updateDB(buildInsertIntoSQL(STATS_TABLE_NAME, STATS_COLUMNS), statArgs);
    }
    
    public boolean updatePlayer(String saveName, Player player)
    {
        Object[] args = new Object[]
                {
                        player.getName(), player.getLevel(), player.getHealth(), player.getXP(), player.getGold(),
                        player.getClassType().name(), player.getPoint().getX(), player.getPoint().getY(), saveName
                };
        if(!dbManager.updateDB(buildUpdateTableSQL(PLAYER_TABLE_NAME, SAVE_NAME_COLUMN, PLAYER_COLUMNS), args))
            return false;
        
        args = new Object[]
                {
                        player.getSTR(), player.getDEX(), player.getINT(), saveName
                };
        
        return dbManager.updateDB(buildUpdateTableSQL(STATS_TABLE_NAME, SAVE_NAME_COLUMN, STATS_COLUMNS), args);
    }
    
    public Player loadSavedPlayer(String saveName)
    {
        List<Map<String, Object>> playerDataList = dbManager.queryDB(buildSelectSQL(PLAYER_TABLE_NAME, SAVE_NAME_COLUMN), saveName);
        List<Map<String, Object>> statsDataList = dbManager.queryDB(buildSelectSQL(STATS_TABLE_NAME, SAVE_NAME_COLUMN), saveName);
        if(playerDataList == null || statsDataList == null)
            return null;
        if(playerDataList.isEmpty() || statsDataList.isEmpty())
            return null;
        
        Map<String, Object> playerData = playerDataList.getFirst();
        
        return loadPlayer(playerData, statsDataList);
    }
    
    public Map<String, Player> getAllSaves()
    {
        Map<String, Player> saveNameCharacterMap = new HashMap<>();
        List<Map<String, Object>> playerDataList = dbManager.queryDB(buildSelectTableSQL(PLAYER_TABLE_NAME));
        List<Map<String, Object>> statsDataList = dbManager.queryDB(buildSelectTableSQL(STATS_TABLE_NAME));
        if(playerDataList == null || statsDataList == null)
            return null;
        if(playerDataList.isEmpty() || statsDataList.isEmpty() || playerDataList.size() != statsDataList.size())
            return saveNameCharacterMap;
        
        
        for(Map<String, Object> playerData : playerDataList)
        {
            Player result = loadPlayer(playerData, statsDataList);
            if(result == null)
                return null;
            
            saveNameCharacterMap.put(playerData.get("SAVE_NAME").toString(), result);
        }
        
        return saveNameCharacterMap;
    }
    
    private Player loadPlayer(Map<String, Object> playerData, List<Map<String, Object>> statsDataList)
    {
        String name = playerData.get("NAME").toString();
        int level = (int)playerData.get("LEVEL");
        float health = (float)playerData.get("HEALTH");
        int xp = (int)playerData.get("XP");
        int gold = (int)playerData.get("GOLD");
        ClassType classType = ClassType.valueOf(playerData.get("CLASS_TYPE").toString());
        Point point = new Point((int)playerData.get("CURRENT_X"), (int)playerData.get("CURRENT_Y"));
        int strength = 0;
        int dexterity = 0;
        int intelligence = 0;
        
        boolean statsFound = false;
        
        for(Map<String, Object> statsData : statsDataList)
        {
            if(!playerData.get("SAVE_NAME").toString().equalsIgnoreCase(statsData.get("SAVE_NAME").toString()))
                continue;
            strength = (int)statsData.get("STRENGTH");
            dexterity = (int)statsData.get("DEXTERITY");
            intelligence = (int)statsData.get("INTELLIGENCE");
            statsFound = true;
            break;
        }
        
        if(!statsFound)
            return null;
        
        return new Player(name, level, health, xp, gold, classType, point, strength, dexterity, intelligence);
    }
    
    public boolean deletePlayer(String saveName)
    {
        return  dbManager.updateDB(buildDeleteRowSQL(STATS_TABLE_NAME, new String[]{SAVE_NAME_COLUMN}), saveName) &&
                dbManager.updateDB(buildDeleteRowSQL(PLAYER_TABLE_NAME, new String[]{SAVE_NAME_COLUMN}), saveName);
    }
    
    public boolean doesSaveExist(String saveName)
    {
        List<Map<String, Object>> result = dbManager.queryDB(buildSelectSQL(PLAYER_TABLE_NAME, SAVE_NAME_COLUMN), saveName);
        return !result.isEmpty();
    }
}
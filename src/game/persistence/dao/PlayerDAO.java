package game.persistence.dao;

import static game.persistence.PersistenceTable.*;
import static game.persistence.SQLCommandFactory.*;

import game.entities.player.Player;
import game.persistence.DBManager;

import java.util.Arrays;

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
    
    public void insertPlayer(String saveName, Player player)
    {
        Object[] playerArgs = new Object[]
                {
                        saveName, player.getName(), player.getLevel(),
                        player.getHealth(), player.getXP(), player.getGold(),
                        player.getClassType().name(), player.getPoint().getX(), player.getPoint().getY()
                };
        dbManager.addEntryDB(buildInsertIntoSQL(PLAYER_TABLE_NAME, PLAYER_COLUMNS), playerArgs);
        
        Object[] statArgs = new Object[]
                {
                        saveName, player.getSTR(), player.getDEX(), player.getINT()
                };
        dbManager.addEntryDB(buildInsertIntoSQL(STATS_TABLE_NAME, STATS_COLUMNS), statArgs);
    }
    
    public void updatePlayer(String saveName, Player player)
    {
        Object[] args = new Object[]
                {
                        player.getName(), player.getLevel(),
                        player.getHealth(), player.getXP(), player.getGold(),
                        player.getClassType().name(), player.getPoint().getX(), player.getPoint().getY()
                };
        dbManager.updateEntryDB(buildUpdateTableSQL(PLAYER_TABLE_NAME, SAVE_NAME_COLUMN,
                Arrays.copyOfRange(PLAYER_COLUMNS, 1, PLAYER_COLUMNS.length)), saveName, args);
        
        args = new Object[]
                {
                        player.getSTR(), player.getDEX(), player.getINT()
                };
        
        dbManager.updateEntryDB(buildUpdateTableSQL(STATS_TABLE_NAME, SAVE_NAME_COLUMN,
                Arrays.copyOfRange(STATS_COLUMNS, 1, STATS_COLUMNS.length)), saveName, args);
    }
    
    public Player loadSavedPlayer(String saveName)
    {
        //TODO: Replace Player Object[] loading with column-name-based mapping (e.g. Map<String, Object>) so loading does not depend on database column order.
        Object[] playerData = dbManager.queryDB(buildSelectSQL(PLAYER_TABLE_NAME, SAVE_NAME_COLUMN), saveName);
        Object[] statsData = dbManager.queryDB(buildSelectSQL(STATS_TABLE_NAME, SAVE_NAME_COLUMN), saveName);
        return new Player(playerData, statsData);
    }
}
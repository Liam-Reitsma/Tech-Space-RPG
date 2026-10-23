package game.persistence;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.LinkedHashMap;

public class SQLCommandFactoryTest
{
    public SQLCommandFactoryTest()
    {
    
    }

    @Test
    public void testBuildCreateTableSQL_3args()
    {
        String tableName = "PLAYER";
        LinkedHashMap<String, String> columns = new LinkedHashMap<>();
        columns.put("SAVE_NAME", "VARCHAR(50)");
        columns.put("NAME", "VARCHAR(50)");
        columns.put("LEVEL", "INT");
        columns.put("HEALTH", "FLOAT");
        columns.put("XP", "INT");
        columns.put("GOLD", "INT");
        columns.put("CLASS_TYPE", "VARCHAR(25)");
        columns.put("CURRENT_X", "INT");
        columns.put("CURRENT_Y", "INT");
        String[] pKeys = {"SAVE_NAME"};
        
        String expected = "CREATE TABLE PLAYER" +
                "(SAVE_NAME VARCHAR(50), NAME VARCHAR(50), LEVEL INT, HEALTH FLOAT, XP INT, GOLD INT, " +
                "CLASS_TYPE VARCHAR(25), CURRENT_X INT, CURRENT_Y INT, " +
                "PRIMARY KEY(SAVE_NAME))";
        
        String actual = SQLCommandFactory.buildCreateTableSQL(tableName, columns, pKeys);
        
        assertEquals(expected, actual);
    }

    @Test
    public void testBuildCreateTableSQL_4args()
    {
        String tableName = "STORED_POTIONS";
        LinkedHashMap<String, String> columns = new LinkedHashMap<>();
        columns.put("SAVE_NAME", "VARCHAR(50)");
        columns.put("POTION_TYPE", "VARCHAR(25)");
        columns.put("COUNT", "INT");
        String[] pKeys = {"SAVE_NAME", "POTION_TYPE"};
        String[] notNullables = {"SAVE_NAME", "POTION_TYPE", "COUNT"};
        
        String expected = "CREATE TABLE STORED_POTIONS" +
                "(SAVE_NAME VARCHAR(50) NOT NULL, POTION_TYPE VARCHAR(25) NOT NULL, COUNT INT NOT NULL, " +
                "PRIMARY KEY(SAVE_NAME, POTION_TYPE))";
        
        String actual = SQLCommandFactory.buildCreateTableSQL(tableName, columns, pKeys, notNullables);
        
        assertEquals(expected, actual);
    }

    @Test
    public void testBuildCreateTableSQL_6args()
    {
        String tableName = "PLAYER_STATS";
        LinkedHashMap<String, String> columns = new LinkedHashMap<>();
        columns.put("SAVE_NAME", "VARCHAR(50)");
        columns.put("STRENGTH", "INT");
        columns.put("DEXTERITY", "INT");
        columns.put("INTELLIGENCE", "INT");
        String[] pKeys = {"SAVE_NAME"};
        String[] notNullables = {"SAVE_NAME", "STRENGTH", "DEXTERITY", "INTELLIGENCE"};
        String[] parentTableNames = {"PLAYER"};
        String[] fKeys = {"SAVE_NAME"};
        
        String expected = "CREATE TABLE PLAYER_STATS" +
                "(SAVE_NAME VARCHAR(50) NOT NULL, STRENGTH INT NOT NULL, DEXTERITY INT NOT NULL, INTELLIGENCE INT NOT NULL, " +
                "PRIMARY KEY(SAVE_NAME), FOREIGN KEY(SAVE_NAME) REFERENCES PLAYER(SAVE_NAME))";
        
        String actual = SQLCommandFactory.buildCreateTableSQL(tableName, columns, pKeys, notNullables, parentTableNames, fKeys);
        
        assertEquals(expected, actual);
    }

    @Test
    public void testBuildInsertIntoSQL()
    {
        String tableName = "ROOMS";
        String[] columns = {"SAVE_NAME", "X_POINT", "Y_POINT", "ROOM_TILE_STATE", "ROOM_TYPE"};
        
        String expected = "INSERT INTO ROOMS" +
                "(SAVE_NAME, X_POINT, Y_POINT, ROOM_TILE_STATE, ROOM_TYPE) " +
                "VALUES(?, ?, ?, ?, ?)";
        
        String actual = SQLCommandFactory.buildInsertIntoSQL(tableName, columns);
        
        assertEquals(expected, actual);
    }

    @Test
    public void testBuildSelectTableSQL()
    {
        String tableName = "STORED_ARMOUR";
        
        String expected = "SELECT * FROM STORED_ARMOUR";
        
        String actual = SQLCommandFactory.buildSelectTableSQL(tableName);
        
        assertEquals(expected, actual);
    }

    @Test
    public void testBuildSelectSQL()
    {
        String tableName = "STORED_WEAPONS";
        String columnName = "WEAPON_SLOT";
        
        String expected = "SELECT * FROM STORED_WEAPONS WHERE WEAPON_SLOT = ?";
        
        String actual = SQLCommandFactory.buildSelectSQL(tableName, columnName);
        
        assertEquals(expected, actual);
    }

    @Test
    public void testBuildUpdateTableSQL()
    {
        String tableName = "EQUIPPED_ITEMS";
        String columnName = "SAVE_NAME";
        String[] columns = {"WEAPON_TYPE", "WEAPON_LEVEL", "HEAD", "CHEST", "ARMS", "THIGHS", "SHINS", "BOOTS"};
        
        String expected = "UPDATE EQUIPPED_ITEMS " +
                "SET WEAPON_TYPE = ?, WEAPON_LEVEL = ?, " +
                "HEAD = ?, CHEST = ?, ARMS = ?, THIGHS = ?, SHINS = ?, BOOTS = ? " +
                "WHERE SAVE_NAME = ?";
        
        String actual = SQLCommandFactory.buildUpdateTableSQL(tableName, columnName, columns);
        
        assertEquals(expected, actual);
    }

    @Test
    public void testBuildUpdateCellEntrySQL()
    {
        String tableName = "STORED_WEAPONS";
        String columnName = "COUNT";
        String[] columns = {"SAVE_NAME", "WEAPON_SLOT", "WEAPON_TYPE"};
        
        String expected = "UPDATE STORED_WEAPONS " +
                "SET COUNT = ? " +
                "WHERE SAVE_NAME = ? AND WEAPON_SLOT = ? AND WEAPON_TYPE = ?";
        
        String actual = SQLCommandFactory.buildUpdateCellEntrySQL(tableName, columnName, columns);
        
        assertEquals(expected, actual);
    }

    @Test
    public void testBuildDeleteRowSQL()
    {
        String tableName = "STORED_ARMOUR";
        String[] columns = {"SAVE_NAME", "ARMOUR_SLOT", "ARMOUR_MATERIAL", "COUNT"};
        
        String expected = "DELETE FROM STORED_ARMOUR " +
                "WHERE SAVE_NAME = ? AND ARMOUR_SLOT = ? AND ARMOUR_MATERIAL = ? AND COUNT = ?";
        
        String actual = SQLCommandFactory.buildDeleteRowSQL(tableName, columns);
        
        assertEquals(expected, actual);
    }

    @Test
    public void testBuildDeleteAllRowSQL()
    {
        String tableName = "ROOMS";
        String columnName = "SAVE_NAME";
        
        String expected = "DELETE FROM ROOMS " +
                "WHERE SAVE_NAME = ?";
        
        String actual = SQLCommandFactory.buildDeleteAllRowsSQL(tableName, columnName);
        
        assertEquals(expected, actual);
    }
}
package game.persistence;

import static game.persistence.PersistenceTable.PLAYER;
import static game.persistence.SQLCommandFactory.buildCreateTableSQL;

public class DatabaseSchemaManager
{
    private static final String COLUMN_NAME = "SAVE_NAME";
    
    private final DBManager dbManager;
    
    public DatabaseSchemaManager(DBManager db)
    {
        dbManager = db;
    }
    
    public boolean createTables()
    {
        if(!dbManager.createTable(PLAYER.name(),
                buildCreateTableSQL(PLAYER.name(), PLAYER.getColumns(), PLAYER.getPrimaryKeys(),
                        PLAYER.getColumnStrings())))
            return false;
        
        for(PersistenceTable table : PersistenceTable.values())
        {
            if(table == PLAYER)
                continue;
            if(!dbManager.createTable(table.name(), buildCreateTableSQL(table.name(), table.getColumns(), table.getPrimaryKeys(),
                    table.getColumnStrings(), new String[]{PLAYER.name()}, new String[]{COLUMN_NAME})))
                return false;
        }
        
        return true;
    }
}
package game.persistence;

import java.util.LinkedHashMap;

public enum PersistenceTable
{
    PLAYER
            (new String[]{"SAVE_NAME"},
            "SAVE_NAME", "VARCHAR(50)",
            "NAME", "VARCHAR(50)",
            "LEVEL", "INT",
            "HEALTH", "REAL",
            "XP", "INT",
            "GOLD", "INT",
            "CLASS_TYPE", "VARCHAR(10)",
            "CURRENT_X", "INT",
            "CURRENT_Y", "INT"
            ),
    PLAYER_STATS
            (new String[]{"SAVE_NAME"},
            "SAVE_NAME", "VARCHAR(50)",
            "STRENGTH", "INT",
            "DEXTERITY", "INT",
            "INTELLIGENCE", "INT"
            ),
    EQUIPPED_ITEMS
            (new String[]{"SAVE_NAME"},
            "SAVE_NAME", "VARCHAR(50)",
            "WEAPON_TYPE", "VARCHAR(25)",
            "WEAPON_LEVEL", "INT",
            "HEAD", "VARCHAR(50)",
            "CHEST", "VARCHAR(50)",
            "ARMS", "VARCHAR(50)",
            "THIGHS", "VARCHAR(50)",
            "SHINS", "VARCHAR(50)",
            "BOOTS", "VARCHAR(50)"
            ),
    STORED_WEAPONS
            (new String[]{"SAVE_NAME", "WEAPON_SLOT", "WEAPON_TYPE", "LEVEL"},
            "SAVE_NAME", "VARCHAR(50)",
            "WEAPON_SLOT", "VARCHAR(25)",
            "WEAPON_TYPE", "VARCHAR(25)",
            "LEVEL", "INT",
            "COUNT", "INT",
            "SLOT_INDEX", "INT"
            ),
    STORED_ARMOUR
            (new String[]{"SAVE_NAME", "ARMOUR_SLOT", "ARMOUR_MATERIAL"},
            "SAVE_NAME", "VARCHAR(50)",
            "ARMOUR_SLOT", "VARCHAR(25)",
            "ARMOUR_MATERIAL", "VARCHAR(25)",
            "COUNT", "INT",
            "SLOT_INDEX", "INT"
            ),
    STORED_POTIONS
            (new String[]{"SAVE_NAME", "POTION_TYPE"},
            "SAVE_NAME", "VARCHAR(50)",
            "POTION_TYPE", "VARCHAR(25)",
            "COUNT", "INT",
            "SLOT_INDEX", "INT"
            ),
    ROOMS
            (new String[]{"SAVE_NAME", "X_POINT", "Y_POINT"},
            "SAVE_NAME", "VARCHAR(50)",
            "X_POINT", "INT",
            "Y_POINT", "INT",
            "ROOM_TILE_STATE", "VARCHAR(25)",
            "ROOM_TYPE", "VARCHAR(25)"
            );
    
    private final String[] primaryKeys;
    private final LinkedHashMap<String, String> columns;
    
    PersistenceTable(String[] PKs, String... args)
    {
        primaryKeys = PKs;
        columns = new LinkedHashMap<>();
        
        for(int i = 0; i < args.length; i += 2)
        {
            columns.put(args[i], args[i+1]);
        }
    }
    
    public LinkedHashMap<String, String> getColumns() { return new LinkedHashMap<>(columns); }
    
    public String[] getColumnStrings() { return columns.keySet().toArray(new String[0]); }
    
    public String[] getPrimaryKeys() { return primaryKeys.clone(); }
}
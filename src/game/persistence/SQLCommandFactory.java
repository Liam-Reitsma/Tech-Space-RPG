package game.persistence;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class SQLCommandFactory
{
    private static final String separator = ", ";
    
    private SQLCommandFactory()
    {
    
    }
    
    public static String buildCreateTableSQL(String tableName, LinkedHashMap<String, String> columns, String[] pKeys)
    {
        return buildCreateTableSQL(tableName, columns, pKeys, null);
    }
    
    public static String buildCreateTableSQL(String tableName, LinkedHashMap<String, String> columns, String[] pKeys, String[] notNullables)
    {
        return buildCreateTableSQL(tableName, columns, pKeys, notNullables,null, null);
    }
    
    public static String buildCreateTableSQL(String tableName, LinkedHashMap<String, String> columns, String[] pKeys, String[] parentTableNames, String[] fKeys)
    {
        return buildCreateTableSQL(tableName, columns, pKeys, null, parentTableNames, fKeys);
    }
    
    public static String buildCreateTableSQL(String tableName, LinkedHashMap<String, String> columns, String[] pKeys, String[] notNullables, String[] parentTableNames, String[] fKeys)
    {
        StringBuilder sql = new StringBuilder();
        
        sql.append("CREATE TABLE ").append(tableName).append("(");
        
        for(Entry<String, String> column : columns.entrySet())
        {
            sql.append(column.getKey()).append(" ").append(column.getValue());
            
            if(notNullables != null)
            {
                for(String notNullable : notNullables)
                {
                    if(column.getKey().equalsIgnoreCase(notNullable))
                    {
                        sql.append(" NOT NULL");
                        break;
                    }
                }
            }
            
            sql.append(separator);
        }
        
        sql.append("PRIMARY KEY(");
        
        for(String pKey : pKeys)
        {
            sql.append(pKey).append(separator);
        }
        
        sql.delete((sql.length()) - separator.length(), sql.length()).append(")");
        
        if(parentTableNames == null) { sql.append(")"); return sql.toString(); }
        else sql.append(separator);
        
        for(int i = 0; i < parentTableNames.length; i++)
        {
            sql.append("FOREIGN KEY(").append(fKeys[i]).append(") REFERENCES ").append(parentTableNames[i]).append("(").append(fKeys[i]).append(")").append(separator);
        }
        
        sql.delete(sql.length() - separator.length(), sql.length()).append(")");
        
        return sql.toString();
    }
    
    public static String buildInsertIntoSQL(String tableName, String[] columns)
    {
        StringBuilder sql = new StringBuilder();
        
        sql.append("INSERT INTO ").append(tableName).append("(");
        
        for(String column : columns)
        {
            sql.append(column).append(separator);
        }
        
        sql.delete((sql.length()) - separator.length(), sql.length()).append(")");
        sql.append(" VALUES (").repeat("?, ", columns.length);
        sql.delete((sql.length()) - separator.length(), sql.length()).append(")");
        
        return sql.toString();
    }
    
    public static String buildSelectSQL(String tableName, String columnName)
    {
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT * FROM ").append(tableName).append(" WHERE ").append(columnName).append(" = ?");
        
        return sql.toString();
    }
    
    public static String buildUpdateTableSQL(String tableName, String columnName, String[] columns)
    {
        StringBuilder sql = new StringBuilder();
        
        sql.append("UPDATE ").append(tableName).append(" SET ");
        for(String column : columns)
        {
            sql.append(column).append(" = ?").append(separator);
        }
        
        sql.delete((sql.length()) - separator.length(), sql.length());
        sql.append(" WHERE ").append(columnName).append(" = ?");
        
        return sql.toString();
    }
    
    public static String buildUpdateRowSQL(String tableName, String columnName, String[] columns)
    {
        StringBuilder sql = new StringBuilder();
        
        sql.append("UPDATE ").append(tableName).append(" SET ").append(columnName).append(" = ? ").append(" WHERE ");
        for(String column : columns)
        {
            if(columnName.equalsIgnoreCase(column)) continue;
            sql.append(column).append(" = ?").append(" AND ");
        }
        
        sql.delete(sql.length() - " AND ".length(), sql.length());
        
        return sql.toString();
    }
    
    public static String buildDeleteRowSQL(String tableName, String[] columns)
    {
        StringBuilder sql = new StringBuilder();
        
        sql.append("DELETE FROM ").append(tableName).append(" WHERE ");
        for(String column : columns)
        {
            sql.append(column).append(" = ?").append(" AND ");
        }
        
        sql.delete(sql.length() - " AND ".length(), sql.length());
        
        return sql.toString();
    }
    
    public static String buildDeleteAllRowSQL(String tableName, String columnName)
    {
        StringBuilder sql = new StringBuilder();
        
        sql.append("DELETE FROM ").append(tableName).append(" WHERE ").append(columnName).append(" = ?");
        
        return sql.toString();
    }
}
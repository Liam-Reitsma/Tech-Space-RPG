package game.persistence;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBManager
{
    private static final Logger LOGGER = Logger.getLogger(DBManager.class.getName());
    
    private Connection connection;
    
    public DBManager(String url, String username, String password)
    {
        establishConnection(url, username, password);
    }
    
    private void establishConnection(String url, String username, String password)
    {
        try
        {
            connection = DriverManager.getConnection(url, username, password);
        } catch(SQLException e)
        {
            LOGGER.log(Level.SEVERE, "DBManager failed to connect to the Database.", e);
        }
    }
    
    public void closeConnection()
    {
        if(connection == null)
            return;
        
        try
        {
            connection.close();
        } catch(SQLException e)
        {
            LOGGER.log(Level.SEVERE, "DBManager failed to close the connection to the Database.", e);
        }
    }
    
    public boolean createTable(String tableName, String sql)
    {
        if(tableExists(tableName))
            return true;
        
        if(connection == null)
            return false;
        
        try(Statement statement = connection.createStatement())
        {
            statement.executeUpdate(sql);
            return true;
        } catch(SQLException e)
        {
            return false;
        }
    }
    
    private boolean tableExists(String tableName)
    {
        try
        {
            try(ResultSet tables = connection.getMetaData().getTables(null, null, tableName.toUpperCase(), null))
            {
                return tables.next();
            }
        }
        catch(SQLException e)
        {
            return false;
        }
    }
    
    public boolean updateDB(String templateSQL, Object... args)
    {
        if(connection == null)
            return false;
        
        try(PreparedStatement ps = connection.prepareStatement(templateSQL))
        {
            for(int i = 1; i <= args.length; i++)
                ps.setObject(i, args[i - 1]);
            
            ps.executeUpdate();
            return true;
        } catch(SQLException e)
        {
            LOGGER.log(Level.SEVERE, "Database update failed: " + templateSQL, e);
            return false;
        }
    }
    
    public List<Map<String, Object>> queryDB(String templateSQL, Object... args)
    {
        if(connection == null)
            return null;
        
        List<Map<String, Object>> objectList = new ArrayList<>();
        Map<String, Object> row;
        int i = 0;
        
        try(PreparedStatement ps = connection.prepareStatement(templateSQL))
        {
            for(Object object : args)
            {
                ps.setObject(++i, object);
            }
            
            try(ResultSet rs = ps.executeQuery())
            {
                ResultSetMetaData metadata = rs.getMetaData();
                
                while(rs.next())
                {
                    row = new HashMap<>();
                    for(int col = 1; col <= metadata.getColumnCount(); col++)
                    {
                        row.put(metadata.getColumnName(col) , rs.getObject(col));
                    }
                    objectList.add(row);
                }
            }
            
            return objectList;
        } catch(SQLException e)
        {
            return null;
        }
    }
    
    public boolean dropTableIfExists(String tableName)
    {
        if(connection == null)
            return false;
        
        try
        {
            try(Statement statement = connection.createStatement())
            {
                try(ResultSet tables = connection.getMetaData().getTables(null, null, tableName.toUpperCase(), null))
                {
                    if(tables.next())
                        statement.execute("DROP TABLE " + tableName);
                }
            }
            return true;
        } catch(SQLException ex)
        {
            return false;
        }
    }
}
package game.persistence;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBManager
{
    private final static String URL = "jdbc:derby:database/TechSpaceRPG_DB;create=true";
    private final static String username = "TSDB";
    private final static String password = "tsdb";
    
    private Connection connection;
    
    public DBManager()
    {
        establishConnection();
    }
    
    private void establishConnection()
    {
        try
        {
            connection = DriverManager.getConnection(URL, username, password);
        } catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }
    
    public void closeSQLConnection()
    {
        closeConnection();
    }
    
    private void closeConnection()
    {
        if(connection != null)
        {
            try
            {
                connection.close();
            } catch(SQLException e)
            {
                System.out.println(e.getMessage());
            }
        }
    }
    
    public void createTable(String tableName, String sql)
    {
        if(tableExists(tableName)) return;
        
        Connection conn = connection;
        
        try(Statement statement = conn.createStatement())
        {
            statement.executeUpdate(sql);
        } catch(SQLException e)
        {
            //change for an actual error.
            System.out.println(e.getMessage());
        }
    }
    
    private boolean tableExists(String tableName)
    {
        boolean result = false;
        
        try
        {
            DatabaseMetaData metadata = connection.getMetaData();
            ResultSet tables = metadata.getTables(null, null, tableName.toUpperCase(), null);
            
            result = tables.next();
            
            tables.close();
        }
        catch(SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
        
        return result;
    }
    
    public void addEntryDB(String templateSQL, Object... args)
    {
        //TODO Add return value & meaning to inform caller result.
        int i = 0;
        
        try(PreparedStatement ps = connection.prepareStatement(templateSQL))
        {
            for(Object object : args)
            {
                ps.setObject(++i, object);
            }
            
            ps.executeUpdate();
        } catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }
    
    public void updateEntryDB(String templateSQL, String saveName, Object... args)
    {
        //TODO Add return value & meaning to inform caller result.
        
        int i = 0;
        
        try(PreparedStatement ps = connection.prepareStatement(templateSQL))
        {
            for(Object object : args)
            {
                ps.setObject(++i, object);
            }
            ps.setObject(++i, saveName);
            
            ps.executeUpdate();
        } catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }
    
    public void updateCellEntryDB(String templateSQL, Object newCellValue, Object... args)
    {
        //TODO Add return value & meaning to inform caller result.
        
        int i = 0;
        
        try(PreparedStatement ps = connection.prepareStatement(templateSQL))
        {
            ps.setObject(++i, newCellValue);
            
            for(Object object : args)
            {
                ps.setObject(++i, object);
            }
            
            ps.executeUpdate();
        } catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }
    
    public void deleteEntryDB(String templateSQL, String saveName)
    {
        //TODO Add return value & meaning to inform caller result.
        
        int i = 0;
        
        try(PreparedStatement ps = connection.prepareStatement(templateSQL))
        {
            ps.setObject(++i, saveName);
            
            ps.executeUpdate();
        } catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }
    
    public void deleteSingleEntryDB(String templateSQL, Object... args)
    {
        //TODO Add return value & meaning to inform caller result.
        int i = 0;
        
        try(PreparedStatement ps = connection.prepareStatement(templateSQL))
        {
            for(Object object : args)
            {
                ps.setObject(++i, object);
            }
            
            ps.executeUpdate();
        } catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }
    
    public List<Map<String, Object>> queryDB(String templateSQL, Object... args)
    {
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
    
    private void dropTableIfExists(String tableName)
    {
        try
        {
            Connection conn = connection;
            Statement statement = conn.createStatement();
            
            DatabaseMetaData metadata = connection.getMetaData();
            ResultSet tables = metadata.getTables(null, null, tableName.toUpperCase(), null);
            if(tables.next())
            {
                statement.execute("DROP TABLE " + tableName);
            }
            
            tables.close();
        } catch(SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
    }
}
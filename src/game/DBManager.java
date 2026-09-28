package game;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
    
    public Object[] queryDB(String templateSQL, String searchName)
    {
        List<Object> objectList = new ArrayList<>();
        
        try(PreparedStatement ps = connection.prepareStatement(templateSQL))
        {
            ps.setString( 1, searchName);
            
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()) objectList.add(rs.getObject(searchName));
            
            objectList.removeFirst();
            
            return objectList.toArray(new Object[0]);
        } catch(SQLException e)
        {
            return null;
        }
    }
    
    public String[] getSavesName()
    {
        Connection conn = connection;
        List<String> savesStringName = new ArrayList<>();
        
        try
        {
            ResultSet rs = conn.createStatement().executeQuery("SELECT SAVE_NAME FROM PLAYER");
            
            while(rs.next()) savesStringName.add(rs.getString("SAVE_NAME"));
            
            return savesStringName.toArray(new String[0]);
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
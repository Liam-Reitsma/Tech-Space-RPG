package game.persistence;

import game.entities.Persistable;

import static game.persistence.SaveErrors.*;

import java.io.*;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

public class SaveFile
{
    private File saveFile;
    private final File saveFileLocation;
    private File[] saveFiles;
    
    public SaveFile()
    {
        saveFileLocation = new File("resources/saves");
    }
    
    public SaveErrors write(List<Persistable> saveables)
    {
        for(int i = 0; i < 2; i++)
        {
            try(BufferedWriter writer = new BufferedWriter(new FileWriter(saveFile)))
            {
                for(Persistable saveable : saveables)
                {
                    writer.write(saveable.getSaveInfo());
                    writer.newLine();
                }
            } catch(FileNotFoundException e)
            {
                if(saveFileLocation.mkdir()) { return write(saveables); }
                return WRITE_ERROR;
            } catch(IOException e)
            {
                return WRITE_ERROR;
            } catch(IllegalAccessException e)
            {
                throw new RuntimeException(e);
            }
        }
        
        return WRITE_ERROR;
    }
    
    public LoadResult read()
    {
        LoadResult lResult= new LoadResult();
        try(BufferedReader reader = new BufferedReader(new FileReader(saveFile)))
        {
            List<String> result = reader.readAllLines();
            for(String line : result)
            {
                lResult.addData(line);
            }
            
            return lResult;
        } catch(FileNotFoundException e)
        {
            lResult.setError(SAVE_FILE_MISSING);
            return lResult;
        } catch(IOException e)
        {
            lResult.setError(LOAD_ERROR);
            return lResult;
        }
    }
    
    public SaveErrors createNewSave(String name)
    {
        if(!checkFileNameValidity(name)) return INVALID_FILE_NAME;
        
        for(String saveName : getSaveNames())
        {
            if(Objects.equals(saveName, name + ".save")) return FILE_ALREADY_EXISTS;
        }
        
        saveFile = new File(saveFileLocation + "/" + name + ".save");
        return SUCCESS;
    }
    
    private boolean checkFileNameValidity(String name)
    {
        if(name == null || name.isEmpty() || name.length() >= 255) return false;
        
        try
        {
            Paths.get(name);
        } catch (Exception e)
        {
            return false;
        }
        
        String windowsReserved = name.split("\\.")[0].toUpperCase();
        if(windowsReserved.equals("CON") || windowsReserved.equals("PRN")) return false;
        if(windowsReserved.equals("AUX") || windowsReserved.equals("NUL")) return false;
        
        return true;
    }
    
    public SaveErrors selectSave(int number)
    {
        if(number - 1 < 0 || number - 1 >= saveFiles.length) return NUMBER_OUT_OF_BOUNDS;
        saveFile = saveFiles[number - 1];
        return SUCCESS;
    }
    
    @Override
    public String toString()
    {
        StringBuilder saves = new StringBuilder();
        int i = 0;
        for(String saveLine : getSaveNames())
        {
            i++;
            saves.append("\n").append(i).append(". ").append(saveLine);
        }
        
        return saves.toString();
    }
    
    private String[] getSaveNames()
    {
        saveFiles = Objects.requireNonNull(saveFileLocation.listFiles());
        String[] saveNames = new String[saveFiles.length];
        int i = 0;
        for(File save : saveFiles)
        {
            saveNames[i++] = save.getName();
        }
        
        return saveNames;
    }
    
    public File getSaveFile()
    {
        return saveFile;
    }
}

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
    
    public SaveErrors write(List<Persistable> persistables)
    {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(saveFile)))
        {
            for(Persistable persistable : persistables)
            {
                writer.write(persistable.getSaveInfo());
                writer.newLine();
            }
            
            return SUCCESS;
        } catch(FileNotFoundException e)
        {
            if(saveFileLocation.mkdir()) { return write(persistables); }
            return WRITE_ERROR;
        } catch(IOException | IllegalAccessException e)
        {
            return WRITE_ERROR;
        }
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
    
    public SaveErrors selectSave(int number)
    {
        if(number - 1 < 0 || number - 1 >= saveFiles.length) return NUMBER_OUT_OF_BOUNDS;
        saveFile = saveFiles[number - 1];
        return SUCCESS;
    }
    
    public SaveErrors createNewSave(String name)
    {
        if(!checkFileNameValidity(name)) return INVALID_FILE_NAME;
        
        SaveFileResult sfResult = getSaveFileNames();
        
        for(String saveName : sfResult.getSaveFiles())
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
    
    @Override
    public String toString()
    {
        StringBuilder saves = new StringBuilder();
        int i = 0;
        
        SaveFileResult SaveFiles = getSaveFileNames();
        
        for(String saveLine : SaveFiles.getSaveFiles())
        {
            i++;
            saves.append("\n").append(i).append(". ").append(saveLine);
        }
        
        return saves.toString();
    }
    
    private SaveFileResult getSaveFileNames()
    {
        try
        {
            saveFiles = Objects.requireNonNull(saveFileLocation.listFiles());
        } catch(NullPointerException e)
        {
            if(!saveFileLocation.mkdir()) return new SaveFileResult(SAVE_FOLDER_LOCATION_MISSING);
            saveFiles = Objects.requireNonNull(saveFileLocation.listFiles());
        }
        
        if(saveFiles.length == 0) return new SaveFileResult(SAVE_FOLDER_EMPTY);
        
        SaveFileResult sfResult = new SaveFileResult(SUCCESS);
        
        for(File save : saveFiles)
        {
            sfResult.addData(save.getName());
        }
        
        return sfResult;
    }
    
    public File getSaveFile()
    {
        return saveFile;
    }
}

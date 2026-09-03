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
        int saveFileIndex = number - 1;
        SaveFileResult sfResult = getSaveFileNames();
        if(saveFileIndex < 0 || saveFileIndex >= sfResult.size()) return NUMBER_OUT_OF_BOUNDS;
        saveFile = sfResult.getSelectedSaveFile(saveFileIndex);
        return SUCCESS;
    }
    
    public SaveErrors createNewSave(String name)
    {
        if(!checkFileNameValidity(name)) return INVALID_FILE_NAME;
        
        SaveFileResult sfResult = getSaveFileNames();
        
        for(File saveFile : sfResult.getSaveFiles())
        {
            if(Objects.equals(saveFile.getName(), name + ".save")) return FILE_ALREADY_EXISTS;
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
        
        SaveFileResult saveFiles = getSaveFileNames();
        
        for(File saveFile : saveFiles.getSaveFiles())
        {
            i++;
            saves.append("\n").append(i).append(". ").append(saveFile.getName());
        }
        
        return saves.toString();
    }
    
    private SaveFileResult getSaveFileNames()
    {
        SaveFileResult sfResult = new SaveFileResult(SUCCESS);
        
        try
        {
            sfResult.addArray(Objects.requireNonNull(saveFileLocation.listFiles()));
        } catch(NullPointerException e)
        {
            if(!saveFileLocation.mkdir()) return new SaveFileResult(SAVE_FOLDER_LOCATION_MISSING);
            sfResult.addArray(Objects.requireNonNull(saveFileLocation.listFiles()));
        }
        
        if(sfResult.size() == 0) return new SaveFileResult(SAVE_FOLDER_EMPTY);
        
        for(File save : sfResult.getSaveFiles())
        {
            sfResult.addData(save);
        }
        
        return sfResult;
    }
    
    public File getSaveFile()
    {
        return saveFile;
    }
}

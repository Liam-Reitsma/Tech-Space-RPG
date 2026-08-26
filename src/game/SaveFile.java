package game;

import static game.Errors.*;

import java.io.File;
import java.nio.file.Paths;
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
    
    public Errors createNewSave(String name)
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
    
    public Errors selectSave(int number)
    {
        if(number - 1 <= 0 || number - 1 >= saveFiles.length) return NUMBER_OUT_OF_BOUNDS;
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

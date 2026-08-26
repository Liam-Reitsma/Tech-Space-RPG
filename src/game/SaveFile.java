package game;

import java.io.File;
import java.util.Objects;

public class SaveFile
{
    private boolean fileExists;
    
    private File saveFile;
    private final File saveFileLocation;
    private File[] saveFiles;
    
    public SaveFile()
    {
        saveFileLocation = new File("/resources/saves");
    }
    
    public void createNewSave(String name)
    {
        for(String saveName : getSaveNames())
        {
            if(Objects.equals(saveName, name + ".save"))
            {
                fileExists = true;
                break;
            }
            else
            {
                fileExists = false;
            }
        }
        if(!fileExists)
        {
            saveFile = new File(saveFileLocation + "/" + name + ".save");
        }
    }
    
    public void findSave(int number)
    {
        saveFile = saveFiles[number - 1];
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
    
    public boolean doesFileExist()
    {
        return fileExists;
    }
    
    public File getSaveFile()
    {
        return saveFile;
    }
}

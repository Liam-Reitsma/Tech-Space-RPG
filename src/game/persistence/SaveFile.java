package game.persistence;

import game.entities.Persistable;
import game.entities.player.Player;
import game.items.Item;
import game.world.RuntimeRoom;

import static game.persistence.PersistenceErrors.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SaveFile
{
    private static final String INITIAL_PADDING = "               ";
    private File saveFile;
    private final File saveFileLocation;
    
    public SaveFile()
    {
        saveFileLocation = new File("resources/saves");
    }
    
    public PersistenceErrors clearFile()
    {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(saveFile)))
        {
            writer.close();
            return SUCCESS;
        } catch(IOException e)
        {
            return WRITE_ERROR;
        }
    }
    
    public PersistenceErrors writeObject(Persistable object)
    {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(saveFile, true)))
        {
            writer.write(object.getClass().getSimpleName() + " Data:");
            writer.newLine();
            writer.write(object.getSaveInfo());
            writer.write(INITIAL_PADDING);
            writer.newLine();
            
            return SUCCESS;
        } catch(FileNotFoundException e)
        {
            if(saveFileLocation.mkdirs()) { return writeObject(object); }
            return WRITE_ERROR;
        } catch(IOException | IllegalAccessException e)
        {
            return WRITE_ERROR;
        }
    }
    
    public PersistenceErrors writeItemList(List<? extends Item> items, String listName)
    {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(saveFile, true)))
        {
            writer.newLine();
            writer.write(listName + ":");
            writer.newLine();
            writer.write("Lines"+ Persistable.FIELD_SEPARATOR + items.size() + Persistable.CLASS_SEPARATOR);
            writer.newLine();
            for(Item item : items)
            {
                writer.write(item.getSaveInfo());
                writer.newLine();
            }
            
            return SUCCESS;
        } catch(FileNotFoundException e)
        {
            if(saveFileLocation.mkdirs()) { return writeItemList(items, listName); }
            return WRITE_ERROR;
        } catch(IOException | IllegalAccessException e)
        {
            return WRITE_ERROR;
        }
    }
    public PersistenceErrors writeWorld(List<RuntimeRoom> world, String listName)
    {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(saveFile, true)))
        {
            writer.newLine();
            writer.write(listName + ":");
            writer.newLine();
            writer.write("Lines"+ Persistable.FIELD_SEPARATOR + world.size() + Persistable.CLASS_SEPARATOR);
            writer.newLine();
            for(RuntimeRoom room : world)
            {
                writer.write(room.getSaveInfo());
                writer.newLine();
            }
            
            return SUCCESS;
        } catch(FileNotFoundException e)
        {
            if(saveFileLocation.mkdirs()) { return writeWorld(world, listName); }
            return WRITE_ERROR;
        } catch(IOException e)
        {
            return WRITE_ERROR;
        }
    }
    
    public PersistenceErrors writeList(String[] list)
    {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(saveFile)))
        {
            for(String line : list)
            {
                writer.write(line);
                writer.newLine();
            }
            
            return SUCCESS;
        } catch(FileNotFoundException e)
        {
            if(saveFileLocation.mkdirs()) { return writeList(list); }
            return WRITE_ERROR;
        } catch(IOException e)
        {
            return WRITE_ERROR;
        }
    }
    
    public PersistenceErrors writeDeltaPlayer(Player player)
    {
        try(RandomAccessFile fileEditor = new RandomAccessFile(saveFile, "rw"))
        {
            String line;
            String playerLine;
            String info = player.getSaveInfo();
            
            do
            {
                line = fileEditor.readLine();
                if(line == null) return SAVE_FILE_CORRUPTED;
            } while(!line.toLowerCase().startsWith("player"));
            
            long startLine = fileEditor.getFilePointer();
            
            playerLine = fileEditor.readLine();
            if(playerLine == null) return SAVE_FILE_CORRUPTED;
            String padding = " ".repeat(playerLine.length() - info.length());
            
            
            fileEditor.seek(startLine);
            fileEditor.write((info + padding).getBytes(StandardCharsets.UTF_8));
            
            return SUCCESS;
        } catch(IllegalArgumentException e)
        {
            return UNABLE_TO_SAVE;
        } catch(IOException | IllegalAccessException e)
        {
            return WRITE_ERROR;
        }
    }
    
    public PersistenceErrors writeDeltaRoom(RuntimeRoom room)
    {
        try(RandomAccessFile fileEditor = new RandomAccessFile(saveFile, "rw"))
        {
            String line;
            String info = room.getSaveInfo();
            int dsOne = info.indexOf(Persistable.DATA_SEPARATOR) + 1;
            int dsTwo = info.indexOf(Persistable.DATA_SEPARATOR, dsOne);
            long startLine;
            
            do
            {
                startLine = fileEditor.getFilePointer();
                line = fileEditor.readLine();
                if(line == null) return SAVE_FILE_CORRUPTED;
            } while(!line.toLowerCase().contains(info.substring(dsOne, dsTwo)));
            
            fileEditor.seek(startLine);
            fileEditor.write(info.getBytes(StandardCharsets.UTF_8));
            
            return SUCCESS;
        } catch(IOException e)
        {
            return WRITE_ERROR;
        }
    }
    
    public LoadResult readObject(String searchLine)
    {
        try(BufferedReader reader = new BufferedReader(new FileReader(saveFile)))
        {
            String[] player = new String[1];
            String metadata;
            do
            {
                metadata = reader.readLine();
                if(metadata == null) return new LoadResult(SAVE_FILE_CORRUPTED, new String[0]);
                else if(metadata.toLowerCase().startsWith(searchLine)) break;
            } while(true);
            
            String line = reader.readLine();
            if(line != null)
            {
                player[0] = line;
                return new LoadResult(SUCCESS, player);
            }
            
            return new LoadResult(SAVE_FILE_CORRUPTED, new String[0]);
        } catch(NullPointerException e)
        {
            return new LoadResult(SAVE_FILE_EMPTY, new String[0]);
        } catch(NumberFormatException e)
        {
            return new LoadResult(SAVE_FILE_CORRUPTED, new String[0]);
        } catch(FileNotFoundException e)
        {
            return new LoadResult(SAVE_FILE_MISSING, new String[0]);
        } catch(IOException e)
        {
            return new LoadResult(LOAD_ERROR, new String[0]);
        }
    }
    
    public LoadResult readList(String listName)
    {
        try(BufferedReader reader = new BufferedReader(new FileReader(saveFile)))
        {
            String[] data;
            String metadataLineOne;
            String metadataLineTwo;
            String length;
            
            do
            {
                metadataLineOne = reader.readLine();
                if(metadataLineOne == null) return new LoadResult(SAVE_FILE_CORRUPTED, new String[0]);
                else if(metadataLineOne.toLowerCase().startsWith(listName.toLowerCase())) break;
            } while(true);
            
            metadataLineTwo = reader.readLine();
            if(metadataLineTwo == null) return new LoadResult(SAVE_FILE_CORRUPTED, new String[0]);
            
            int metadataFS = metadataLineTwo.indexOf(Persistable.FIELD_SEPARATOR);
            if(metadataFS == -1) return new LoadResult(SAVE_FILE_CORRUPTED, new String[0]);
            int metadataCS = metadataLineTwo.indexOf(Persistable.CLASS_SEPARATOR);
            if(metadataCS == -1) return new LoadResult(SAVE_FILE_CORRUPTED, new String[0]);
            if(metadataCS <= metadataFS) return new LoadResult(SAVE_FILE_CORRUPTED, new String[0]);
            length = metadataLineTwo.substring(metadataFS + 1, metadataCS);
            data = new String[Integer.parseInt(length)];
            
            for(int i = 0; i < data.length; i++)
            {
                String datum = reader.readLine();
                if(datum == null) return new LoadResult(SAVE_FILE_CORRUPTED, new String[0]);
                data[i] = datum;
            }
            
            return new LoadResult(SUCCESS, data);
        } catch(NullPointerException e)
        {
            return new LoadResult(SAVE_FILE_EMPTY, new String[0]);
        } catch(NumberFormatException e)
        {
            return new LoadResult(SAVE_FILE_CORRUPTED, new String[0]);
        } catch(FileNotFoundException e)
        {
            return new LoadResult(SAVE_FILE_MISSING, new String[0]);
        } catch(IOException e)
        {
            return new LoadResult(LOAD_ERROR, new String[0]);
        }
    }
    
    public LoadResult readAndInsert(String nameHeader, Item item)
    {
        try(BufferedReader reader = new BufferedReader(new FileReader(saveFile)))
        {
            String line;
            String[] result;
            List<String> list = new ArrayList<>();
            boolean rightLocation = false;
            boolean inserted = false;
            
            do
            {
                line = reader.readLine();
                if(line == null && !inserted) return new LoadResult(SAVE_FILE_CORRUPTED, new String[0]);
                else if (line == null) break;
                if(line.toLowerCase().startsWith(nameHeader.toLowerCase()) && !rightLocation) rightLocation = true;
                if(rightLocation && line.toLowerCase().startsWith("lines"))
                {
                    int FS = line.indexOf(Persistable.FIELD_SEPARATOR);
                    if(FS == -1) return new LoadResult(SAVE_FILE_CORRUPTED, new String[0]);
                    int CS = line.indexOf(Persistable.CLASS_SEPARATOR);
                    if(CS == -1) return new LoadResult(SAVE_FILE_CORRUPTED, new String[0]);
                    if(CS <= FS) return new LoadResult(SAVE_FILE_CORRUPTED, new String[0]);
                    int number = Integer.parseInt(line.substring(FS + 1, CS)) + 1;
                    list.add("Lines" + Persistable.FIELD_SEPARATOR + number + Persistable.CLASS_SEPARATOR);
                }
                else if(rightLocation && line.isEmpty()) { rightLocation = false; inserted = true; list.add(item.getSaveInfo()); }
                else list.add(line);
            } while(true);
            
            result = list.toArray(new String[0]);
            
            return new LoadResult(SUCCESS, result);
        } catch(NullPointerException e)
        {
            return new LoadResult(SAVE_FILE_EMPTY, new String[0]);
        } catch(NumberFormatException e)
        {
            return new LoadResult(SAVE_FILE_CORRUPTED, new String[0]);
        } catch(FileNotFoundException e)
        {
            return new LoadResult(SAVE_FILE_MISSING, new String[0]);
        } catch(IOException | IllegalAccessException e)
        {
            return new LoadResult(LOAD_ERROR, new String[0]);
        }
    }
    
    public LoadResult readAndRemove(String nameHeader, Item item)
    {
        try(BufferedReader reader = new BufferedReader(new FileReader(saveFile)))
        {
            String line;
            String[] result;
            List<String> list = new ArrayList<>();
            boolean rightLocation = false;
            boolean removed = false;
            
            do
            {
                line = reader.readLine();
                if(line == null && !removed) return new LoadResult(SAVE_FILE_CORRUPTED, new String[0]);
                else if (line == null) break;
                if(line.toLowerCase().startsWith(nameHeader.toLowerCase()) && !rightLocation) rightLocation = true;
                if(rightLocation && line.toLowerCase().startsWith("lines"))
                {
                    int FS = line.indexOf(Persistable.FIELD_SEPARATOR);
                    if(FS == -1) return new LoadResult(SAVE_FILE_CORRUPTED, new String[0]);
                    int CS = line.indexOf(Persistable.CLASS_SEPARATOR);
                    if(CS == -1) return new LoadResult(SAVE_FILE_CORRUPTED, new String[0]);
                    if(CS <= FS) return new LoadResult(SAVE_FILE_CORRUPTED, new String[0]);
                    int number = Integer.parseInt(line.substring(FS + 1, CS)) - 1;
                    list.add("Lines" + Persistable.FIELD_SEPARATOR + number + Persistable.CLASS_SEPARATOR);
                }
                else if(rightLocation && line.equals(item.getSaveInfo())) { rightLocation = false; removed = true; }
                else list.add(line);
            } while(true);
            
            result = list.toArray(new String[0]);
            
            return new LoadResult(SUCCESS, result);
        } catch(NullPointerException e)
        {
            return new LoadResult(SAVE_FILE_EMPTY, new String[0]);
        } catch(NumberFormatException e)
        {
            return new LoadResult(SAVE_FILE_CORRUPTED, new String[0]);
        } catch(FileNotFoundException e)
        {
            return new LoadResult(SAVE_FILE_MISSING, new String[0]);
        } catch(IOException | IllegalAccessException e)
        {
            return new LoadResult(LOAD_ERROR, new String[0]);
        }
    }
    
    public PersistenceErrors selectSave(int number)
    {
        int saveFileIndex = number - 1;
        SaveResult sfResult = getSaveFileNames();
        if(sfResult.getError() != SUCCESS) return sfResult.getError();
        if(saveFileIndex < 0 || saveFileIndex >= sfResult.size()) return NUMBER_OUT_OF_BOUNDS;
        saveFile = sfResult.getSelectedSaveFile(saveFileIndex);
        return SUCCESS;
    }
    
    public PersistenceErrors createNewSave(String name)
    {
        PersistenceErrors check = checkCreateNewSave(name);
        if(check != SUCCESS) return check;
        saveFile = new File(saveFileLocation + "/" + name + ".save");
        return SUCCESS;
    }
    
    public boolean canCreateNewSave(String name)
    {
        return (checkCreateNewSave(name) == SUCCESS);
    }
    
    private PersistenceErrors checkCreateNewSave(String name)
    {
        if(!checkFileNameValidity(name)) return INVALID_FILE_NAME;
        
        SaveResult sfResult = getSaveFileNames();
        
        for(File saveFile : sfResult.getResult())
        {
            if(Objects.equals(saveFile.getName(), name + ".save")) return FILE_ALREADY_EXISTS;
        }
        
        return SUCCESS;
    }
    
    private boolean checkFileNameValidity(String name)
    {
        if(name == null || name.isEmpty() || name.length() >= 255) return false;
        
        if(name.contains("\\") || name.contains("/") || name.contains(":")) return false;
        if(name.contains("*") || name.contains("?") || name.contains("\"")) return false;
        if(name.contains("<") || name.contains(">") || name.contains("|"))  return false;
        
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
        
        SaveResult saveFiles = getSaveFileNames();
        
        for(File saveFile : saveFiles.getResult())
        {
            i++;
            saves.append(i).append(". ").append(saveFile.getName()).append("\n");
        }
        
        if(!saves.isEmpty()) saves.deleteCharAt(saves.length() - 1);
        
        return saves.toString();
    }
    
    public SaveResult getSaveFileNames()
    {
        SaveResult sfResult;
        
        try
        {
            sfResult = new SaveResult(SUCCESS, Objects.requireNonNull(saveFileLocation.listFiles()));
        } catch(NullPointerException e)
        {
            if(!saveFileLocation.mkdirs()) return new SaveResult(SAVE_FOLDER_LOCATION_MISSING, new File[0]);
            sfResult = new SaveResult(SUCCESS, Objects.requireNonNull(saveFileLocation.listFiles()));
        }
        
        if(sfResult.size() == 0) return new SaveResult(SAVE_FOLDER_EMPTY, new File[0]);

        return sfResult;
    }
}

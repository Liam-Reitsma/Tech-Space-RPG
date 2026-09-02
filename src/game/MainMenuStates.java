package game;

import game.entities.Persistable;
import game.persistence.LoadFactory;
import game.persistence.LoadResult;
import game.persistence.SaveErrors;
import game.persistence.SaveFileHandler;

public enum MainMenuStates implements Menu
{
    NEW_GAME("Start a New Game.")
            {
                @Override
                public void execute(ConsoleHandler consoleH, SaveFileHandler saveFH)
                {
                    consoleH.printLine("What would you like to call the save file?");
                    
                    do
                    {
                        SaveErrors result = saveFH.createNewSave(consoleH.nextLine());
                        
                        if(result == SaveErrors.SUCCESS) break;
                        consoleH.printLine(result.toString());
                    } while(true);
                }
            },
    LOAD_GAME("Load a Saved Game.")
            {
                @Override
                public void execute(ConsoleHandler consoleH, SaveFileHandler saveFH)
                {
                    LoadResult loadResult;
                    Persistable objectToAdd;
                    LoadFactory loadFactory = new LoadFactory();
                    
                    consoleH.printLine("Which save would you like to use?");
                    consoleH.printLine(saveFH.listAllSaves());
                    do
                    {
                        do
                        {
                            SaveErrors result = saveFH.selectSave(consoleH.nextInt());
                            
                            if(result == SaveErrors.SUCCESS) break;
                            consoleH.printLine(result.toString());
                            
                        } while(true);
                        
                        loadResult = saveFH.loadData();
                        if(loadResult.getError() != SaveErrors.SUCCESS) { consoleH.printLine(loadResult.getError().toString()); }
                        else break;
                        
                    } while(true);
                    
                    for(int i = 0; i < loadResult.size(); i++)
                    {
                        try
                        {
                            objectToAdd = (Persistable)loadFactory.create(loadResult.getClassName(i), loadResult.getClassData(i));
                            consoleH.printLine(objectToAdd.getSaveInfo());
                        } catch(IllegalAccessException e)
                        {
                            throw new RuntimeException(e);
                        } catch(IllegalArgumentException e)
                        {
                            consoleH.printLine(e.getMessage() + "\nLoad aborted");
                            return;
                        }
                    }
                    
                }
            },
    EXIT("Exit the Game")
            {
                @Override
                public void execute(ConsoleHandler consoleH, SaveFileHandler saveFH)
                {
                    consoleH.printLine("Exiting the game now...");
                }
            };
    
    final String message;
    
    MainMenuStates(String s)
    {
        message = s;
    }
    
    @Override
    public String toString()
    {
        return message;
    }
}
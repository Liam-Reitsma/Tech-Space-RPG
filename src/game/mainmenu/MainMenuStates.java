package game.mainmenu;

import static game.persistence.SaveErrors.*;

import game.ConsoleHandler;
import game.entities.ClassType;
import game.entities.Persistable;
import game.entities.Player;
import game.persistence.LoadFactory;
import game.persistence.LoadResult;
import game.persistence.SaveErrors;
import game.persistence.SaveFileHandler;

import java.util.ArrayList;
import java.util.List;

public enum MainMenuStates implements Menu
{
    NEW_GAME("Start a New Game.")
            {
                @Override
                public MainMenuResult execute(ConsoleHandler consoleH, SaveFileHandler saveFH)
                {
                    List<Persistable> list = new ArrayList<>();
                    do
                    {
                        SaveErrors result = saveFH.createNewSave(consoleH.confirmInput("What would you like to call the save file?"));
                        
                        if(result == SUCCESS) break;
                        consoleH.printLine(result.toString());
                    } while(true);
                    
                    list.add(Introduction(consoleH));
                    
                    return new MainMenuResult(MainMenuErrors.SUCCESS, list, this);
                }
            },
    LOAD_GAME("Load a Saved Game.")
            {
                @Override
                public MainMenuResult execute(ConsoleHandler consoleH, SaveFileHandler saveFH)
                {
                    List<Persistable> list = new ArrayList<>();
                    LoadResult loadResult;
                    LoadFactory loadFactory = new LoadFactory();
                    
                    consoleH.printLine("Which save would you like to use?");
                    consoleH.printLine(saveFH.listAllSaves());
                    do
                    {
                        do
                        {
                            SaveErrors result = saveFH.selectSave(consoleH.nextInt());
                            
                            if(result == SUCCESS) break;
                            consoleH.printLine(result.toString());
                        } while(true);
                        
                        loadResult = saveFH.loadData();
                        if(loadResult.getError() == SUCCESS) { break; }
                        consoleH.printLine(loadResult.getError().toString());
                    } while(true);
                    
                    for(int i = 0; i < loadResult.size(); i++)
                    {
                        try
                        {
                            list.add(loadFactory.create(loadResult.getClassName(i), loadResult.getClassData(i)));
                        } catch(IllegalArgumentException e)
                        {
                            consoleH.printLine(e.getMessage() + "\nLoad aborted");
                            return new MainMenuResult(MainMenuErrors.LOADING_LIST_FAILED, list, this);
                        }
                    }
                    
                    return new MainMenuResult(MainMenuErrors.SUCCESS, list, this);
                }
            },
    EXIT("Exit the Game")
            {
                @Override
                public MainMenuResult execute(ConsoleHandler consoleH, SaveFileHandler saveFH)
                {
                    return new MainMenuResult(MainMenuErrors.SUCCESS, new ArrayList<>(), this);
                }
            };
    
    final String message;
    
    MainMenuStates(String s)
    {
        message = s;
    }
    
    public Persistable Introduction(ConsoleHandler consoleH)
    {
        Player player;
        String charName;
        ClassType classType;
        
        charName = consoleH.confirmInput("What would you like to name your character?");
        classType = consoleH.confirmInput("What class do you want?", "Warrior, Rogue, or Mage", ClassType.class);
        
        player = new Player(charName, classType);
        
        return player;
    }
    
    @Override
    public String toString()
    {
        return message;
    }
}
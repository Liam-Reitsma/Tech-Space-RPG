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
import java.util.Optional;

public enum MainMenuStates implements Menu
{
    NEW_GAME("Start a New Game.")
            {
                @Override
                public MainMenuResult execute(ConsoleHandler consoleH, SaveFileHandler saveFH)
                {
                    List<List<Persistable>> list = new ArrayList<>(new ArrayList<>());
                    List<Persistable> innerList = new ArrayList<>();
                    String fileName;
                    String charName;
                    for(int i = 0; i < 4; i++)
                    {
                        switch(i)
                        {
                            case 0:
                                fileName = askFileName(saveFH, consoleH);
                                if(fileName == null) return null;
                                break;
                            case 1:
                                charName = askCharName(consoleH);
                                if(charName == null) i--;
                                break;
                                
                        }
                    }
                    
                    innerList.add(Introduction(consoleH));
                    list.add(innerList);
                    
                    return new MainMenuResult(MainMenuErrors.SUCCESS, list, this);
                }
            },
    LOAD_GAME("Load a Saved Game.")
            {
                @Override
                public MainMenuResult execute(ConsoleHandler consoleH, SaveFileHandler saveFH)
                {
                    List<List<Persistable>> list = new ArrayList<>();
                    List<Persistable> innerList = new ArrayList<>();
                    LoadResult loadResult;
                    LoadFactory loadFactory = new LoadFactory();
                    
                    consoleH.printLine("Which save would you like to use?");
                    consoleH.printLine(saveFH.listAllSaves());
                    do
                    {
                        do
                        {
                            Optional<Integer> userInput = consoleH.nextInt().getInput();
                            if(userInput.isEmpty()) break;
                            SaveErrors result = saveFH.selectSave(userInput.get());
                            
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
                            innerList.add(loadFactory.create(loadResult.getClassName(i), loadResult.getClassData(i)));
                        } catch(IllegalArgumentException e)
                        {
                            consoleH.printLine(e.getMessage() + "\nLoad aborted");
                            return new MainMenuResult(MainMenuErrors.LOADING_LIST_FAILED, list, this);
                        }
                    }
                    
                    list.add(innerList);
                    
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
    
    public String askFileName(SaveFileHandler saveFH, ConsoleHandler consoleH)
    {
        String result = "";
        
        do
        {
            Optional<String> userInput = consoleH.confirmInput("What would you like to call the save file?").getInput();
            if(userInput.isEmpty()) return null;
            if(saveFH.canCreateNewSave(userInput.get()))
            {
                result = userInput.get();
                break;
            }
            
            consoleH.printLine(saveFH.createNewSave(userInput.get()).toString());
        } while(true);
        
        return result;
    }
    
    public String askCharName(ConsoleHandler consoleH)
    {
        Optional<String> userInputName = consoleH.confirmInput("What would you like to name your character?").getInput();
        return userInputName.orElse(null);
    }
    
    public Persistable Introduction(ConsoleHandler consoleH)
    {
        Player player;
        String charName;
        ClassType classType;
        
        
        Optional<ClassType> userInputClass = consoleH.confirmInput("What class do you want?", "Warrior, Rogue, or Mage", ClassType.class).getInput();
        if(userInputClass.isEmpty()) return null;
        classType = userInputClass.get();
        
        player = new Player(null, classType);
        
        return player;
    }
    
    @Override
    public String toString()
    {
        return message;
    }
}
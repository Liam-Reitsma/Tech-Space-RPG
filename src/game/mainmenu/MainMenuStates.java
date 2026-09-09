package game.mainmenu;

import static game.persistence.PersistenceErrors.*;

import game.ConsoleHandler;
import game.entities.ClassType;
import game.entities.Persistable;
import game.entities.Player;
import game.persistence.LoadFactory;
import game.persistence.LoadResult;
import game.persistence.PersistenceErrors;
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
                    String fileName = "";
                    String charName = "";
                    ClassType classType = ClassType.UNSELECTED;
                    for(int i = 0; i < 3; i++)
                    {
                        switch(i)
                        {
                            case 0:
                                fileName = askFileName(saveFH, consoleH);
                                if(fileName == null) return new MainMenuResult(MainMenuErrors.USER_WENT_BACK, list, this);
                                break;
                            case 1:
                                charName = askCharName(consoleH);
                                if(charName == null) i -= 2;
                                break;
                            case 2:
                                classType = askClassType(consoleH);
                                if(classType == ClassType.UNSELECTED) i -= 2;
                                break;
                        }
                    }
                    
                    saveFH.createNewSave(fileName);
                    innerList.add(new Player(charName, classType));
                    list.add(innerList);
                    list.add(new ArrayList<Persistable>()); //TODO Add Weapon Based on Class
                    list.add(new ArrayList<Persistable>());
                    saveFH.saveData(list.getFirst());
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
                    LoadFactory loadFactory = new LoadFactory();
                    LoadResult loadResult = askFileToLoad(consoleH, saveFH);
                    
                    if(loadResult == null) return new MainMenuResult(MainMenuErrors.USER_WENT_BACK, list, this);
                    
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
    
    private static String askFileName(SaveFileHandler saveFH, ConsoleHandler consoleH)
    {
        String result;
        
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
    
    private static String askCharName(ConsoleHandler consoleH)
    {
        Optional<String> userInputName = consoleH.confirmInput("What would you like to name your character?").getInput();
        return userInputName.orElse(null);
    }
    
    private static ClassType askClassType(ConsoleHandler consoleH)
    {
        Optional<ClassType> userInputClass = consoleH.confirmInput("What class do you want?", "Warrior, Rogue, or Mage", ClassType.class).getInput();
        return userInputClass.orElse(ClassType.UNSELECTED);
    }
    
    private static LoadResult askFileToLoad(ConsoleHandler consoleH, SaveFileHandler saveFH)
    {
        LoadResult loadResult;
        String question = "Which save would you like to use?";
        String options = saveFH.listAllSaves();
        do
        {
            do
            {
                Optional<Integer> userInput = consoleH.confirmInput(question, options, "\n").getInput();
                if(userInput.isEmpty()) return null;
                PersistenceErrors result = saveFH.selectSave(userInput.get());
                
                if(result == SUCCESS) break;
                consoleH.printLine(result.toString());
            } while(true);
            
            loadResult = saveFH.loadData();
            if(loadResult.getError() == SUCCESS) { break; }
            consoleH.printLine(loadResult.getError().toString());
        } while(true);
        
        return loadResult;
    }
    
    @Override
    public String toString()
    {
        return message;
    }
}
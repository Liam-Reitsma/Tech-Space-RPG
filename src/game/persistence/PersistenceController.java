package game.persistence;

import static game.persistence.PersistenceErrors.*;

import game.*;
import game.entities.ClassType;
import game.entities.Player;

import java.util.*;

public class PersistenceController implements Startable<PersistenceResult>, Loopable<PersistenceResult>
{
    private final SaveFile saveF;
    private final ConsoleHandler consoleH;
    private final LoadFactory loadF;
    
    public PersistenceController(ConsoleHandler consoleHandler, SaveFile saveFile)
    {
        saveF = saveFile;
        consoleH = consoleHandler;
        loadF = new LoadFactory();
    }
    
    public PersistenceResult createNewGame()
    {
        String fileName = "";
        String charName = "";
        ClassType classType = ClassType.UNSELECTED;
        Player player;
        
        do
        {
            for(int i = 0; i < 3; i++)
            {
                switch(i)
                {
                    case 0:
                        fileName = askFileName(consoleH);
                        if(fileName == null) return new PersistenceResult(PersistenceErrors.USER_WENT_BACK, null, null, null, null);
                        break;
                    case 1:
                        Optional<String> userInputName = consoleH.confirmInput("What would you like to name your " + "character?").getInput();
                        if(userInputName.isPresent()) charName = userInputName.get();
                        else i -= 2;
                        break;
                    case 2:
                        Optional<ClassType> userInputClass = consoleH.confirmInput("What class do you want?", "Warrior, Rogue, or Mage", ClassType.class).getInput();
                        if(userInputClass.isPresent()) classType = userInputClass.get();
                        else i -= 2;
                        break;
                }
            }
            
            player = new Player(charName, classType);
            PersistenceErrors error = saveF.createNewSave(fileName);
            if(error != SUCCESS)  consoleH.printLine(error.toString());
            else  break;
        } while(true);
        
        //TODO Add WritePlayer();
        return new PersistenceResult(PersistenceErrors.SUCCESS, player, null, null, null);
    }
    
    private String askFileName(ConsoleHandler consoleH)
    {
        String result;
        
        do
        {
            Optional<String> userInput = consoleH.confirmInput("What would you like to call the save file?").getInput();
            if(userInput.isEmpty()) return null;
            if(saveF.canCreateNewSave(userInput.get()))
            {
                result = userInput.get();
                break;
            }
        } while(true);
        
        return result;
    }
    
    public PersistenceResult loadSaveGame()
    {
        PersistenceErrors result = setFileToLoad(consoleH);
        if(result != SUCCESS) return new PersistenceResult(PersistenceErrors.USER_WENT_BACK, null, null, null, null);
        
        //TODO add readPlayer() and readList()
        
        return new PersistenceResult(PersistenceErrors.SUCCESS, null, null, null, null);
    }
    
    private PersistenceErrors setFileToLoad(ConsoleHandler consoleH)
    {
        Optional<Integer> userInput;
        PersistenceErrors error;
        String question = "Which save would you like to use?";
        String options = saveF.toString();
        
        do
        {
            userInput = consoleH.confirmInput(question, options, "\n").getInput();
            if(userInput.isEmpty()) return null;
            error = saveF.selectSave(userInput.get());
            
            if(error == SUCCESS) break;
            consoleH.printLine(error.toString());
        } while(true);
        
        return error;
    }
    
    @Override
    public PersistenceResult start()
    {
        return null;
    }
    
    @Override
    public PersistenceResult loop()
    {
        return null;
    }
}
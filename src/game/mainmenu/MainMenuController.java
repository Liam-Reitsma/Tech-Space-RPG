package game.mainmenu;

import static game.mainmenu.MainMenuErrors.*;

import game.ConsoleHandler;
import game.entities.Persistable;
import game.persistence.SaveFileHandler;

import java.util.List;
import java.util.Optional;

public class MainMenuController
{
    private final ConsoleHandler consoleH;
    private final SaveFileHandler saveFH;
    
    public MainMenuController(ConsoleHandler cH, SaveFileHandler sFH)
    {
        consoleH = cH;
        saveFH = sFH;
    }
    
    public List<List<Persistable>> run()
    {
        boolean validOption;
        boolean success;
        
        MainMenuResult result = null;
        
        consoleH.printLine("Welcome to Tech Space");
        consoleH.printLine("Select an Option:");
        for(MainMenuStates state : MainMenuStates.values())
        {
            consoleH.printLine((state.ordinal() + 1) + ". "+ state);
        }
        
        do
        {
            validOption = false;
            success = false;
            Optional<Integer> userInput = consoleH.nextInt().getInput();
            if(userInput.isEmpty()) break;
            int response = userInput.get();
            
            for(MainMenuStates state : MainMenuStates.values())
            {
                if(response == state.ordinal() + 1)
                {
                    result = state.execute(consoleH, saveFH);
                    validOption = true;
                    if(result.getError() == SUCCESS) success = true;
                    else consoleH.printLine(result.getError().toString());
                    break;
                }
            }
        } while(!validOption || !success);
        
        return result.getList();
    }
}

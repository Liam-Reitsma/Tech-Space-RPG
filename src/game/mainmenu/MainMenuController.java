package game.mainmenu;

import static game.mainmenu.MainMenuErrors.*;

import game.ConsoleHandler;
import game.persistence.SaveFileHandler;

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
    
    public MainMenuResult start()
    {
        boolean validOption;
        boolean success;
        
        MainMenuResult result = new MainMenuResult(USER_WENT_BACK, null, MainMenuStates.EXIT);
        
        do
        {
            consoleH.printLine("Welcome to Tech Space.\nUse '~' whenever you wish to go back.");
            consoleH.printLine("Select an Option:");
            for(MainMenuStates state : MainMenuStates.values())
            {
                consoleH.printLine((state.ordinal() + 1) + ". "+ state);
            }
            
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
        } while((!validOption || !success));
        
        return result;
    }
}

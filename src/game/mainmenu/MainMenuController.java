package game.mainmenu;

import static game.mainmenu.MainMenuErrors.*;

import game.ConsoleHandler;
import game.Startable;

import java.util.Optional;

public class MainMenuController implements Startable<MainMenuResult>
{
    private final ConsoleHandler consoleH;
    
    public MainMenuController(ConsoleHandler cH)
    {
        consoleH = cH;
    }
    
    @Override
    public MainMenuResult start()
    {
        Optional<Integer> userInput;
        do
        {
            consoleH.printLine("Welcome to Tech Space.\nUse '~' whenever you wish to go back.");
            consoleH.printLine("Select an Option:");
            for(MainMenuStates state : MainMenuStates.values())
            {
                consoleH.printLine((state.ordinal() + 1) + ". "+ state);
            }
            
            userInput = consoleH.nextInt().getInput();
            if(userInput.isEmpty()) break;
            int response = userInput.get();
            
            for(MainMenuStates state : MainMenuStates.values())
            {
                if(response == state.ordinal() + 1)
                {
                    return state.execute();
                }
            }
        } while(true);
        
        return new MainMenuResult(USER_WENT_BACK, MainMenuStates.EXIT);
    }
}

package game;

import static game.MainMenuErrors.*;

import game.entities.Persistable;
import game.persistence.SaveFileHandler;

import java.util.List;

public class MainMenuController
{
    private final ConsoleHandler consoleH;
    private final SaveFileHandler saveFH;
    
    public MainMenuController(ConsoleHandler cH, SaveFileHandler sFH)
    {
        consoleH = cH;
        saveFH = sFH;
    }
    
    public List<Persistable> run()
    {
        boolean validOption;
        boolean success;
        
        UserResult result = null;
        
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
            int response = consoleH.nextInt();
            
            for(MainMenuStates state : MainMenuStates.values())
            {
                if(response == state.ordinal() + 1)
                {
                    consoleH.nextLine();
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

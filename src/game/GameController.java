package game;

import static game.entities.ClassType.*;

import game.entities.ClassType;
import game.entities.Player;
import game.entities.Saveable;

import java.util.ArrayList;
import java.util.List;

public class GameController
{
    private final ConsoleHandler consoleH;
    private final SaveFileHandler saveFH;
    private final List<Saveable> saveableList;
    
    private Player player;
    
    public GameController(ConsoleHandler cH, SaveFileHandler sFH)
    {
        consoleH = cH;
        saveFH = sFH;
        saveableList = new ArrayList<>();
    }
    
    public void run()
    {
        MainMenu();
        Introduction();
        
        consoleH.printLine("Your name is: " + player.getName());
        consoleH.printLine("Your chosen class is: " + player.getClassType());
        consoleH.printLine("Your stats are: " + player.getStatsToString());
    }
    
    public void MainMenu()
    {
        boolean invalidOption = true;
        
        consoleH.printLine("Welcome to Tech Space");
        consoleH.printLine("Select an Option:");
        for(MainMenuStates state : MainMenuStates.values())
        {
            consoleH.printLine((state.ordinal() + 1) + ". "+ state);
        }
        
        do
        {
            int response = consoleH.nextInt();
            
            for(MainMenuStates state : MainMenuStates.values())
            {
                if(response == state.ordinal() + 1)
                {
                    consoleH.nextLine();
                    state.execute(consoleH, saveFH);
                    invalidOption = false;
                    break;
                }
            }
        } while(invalidOption);
    }
    
    public void Introduction()
    {
        Character confirmation;
        String charName;
        String charClass;
        ClassType classType;
        
        do
        {
            consoleH.printLine("What is your character's name?");
            charName = consoleH.nextLine();
            consoleH.printLine("Are you sure you want this name?");
            confirmation = consoleH.nextCharToLowerCase();
        } while(!confirmation.equals('y'));
        
        do
        {
            charClass = consoleH.nextLine();
            consoleH.printLine("What class do you want? Warrior, Rogue, or Mage?");
            if(charClass.equalsIgnoreCase("warrior")) {classType = WARRIOR; break; }
            else if(charClass.equalsIgnoreCase("rogue")) { classType = ROGUE; break; }
            else if(charClass.equalsIgnoreCase("mage")) {classType = MAGE; break; }
        } while(true);
        
        player = new Player(charName, classType);
        
        saveableList.add(player);
        
        SaveErrors result = saveFH.saveData(saveableList);
        if(result != SaveErrors.SUCCESS) consoleH.printLine(result.toString());
    }
}

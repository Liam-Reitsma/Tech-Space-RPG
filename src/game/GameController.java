package game;

import static game.entities.ClassType.*;

import game.entities.ClassType;
import game.entities.Player;

public class GameController
{
    ConsoleHandler consoleH;
    SaveFileHandler saveFH;
    Player player;
    
    public GameController(ConsoleHandler ch, SaveFileHandler sfh)
    {
        consoleH = ch;
        saveFH = sfh;
    }
    
    public void run()
    {
        MainMenu();
        Introduction();
        
        
        System.out.println("Your name is: " + player.getName());
        System.out.println("Your chosen class is: " + player.getClassType());
        System.out.println("Your stats are: " + player.getStatsToString());
    }
    
    public void MainMenu()
    {
        System.out.println("Welcome to Tech Space");
        System.out.println("Select an Option:");
        for(MainMenuStates state : MainMenuStates.values())
        {
            System.out.println((state.ordinal() + 1) + ". "+ state);
        }
        
        do
        {
            int response = consoleH.nextInt();
            
            for(MainMenuStates state : MainMenuStates.values())
            {
                if(response == state.ordinal())
                {
                    state.execute(consoleH, saveFH);
                    break;
                }
            }
        } while(true);
    }
    
    public void Introduction()
    {
        Character confirmation;
        String charName;
        String charClass;
        ClassType classType;
        
        do
        {
            System.out.println("What is your character's name?");
            charName = consoleH.nextLine();
            System.out.println("Are you sure you want this name?");
            confirmation = consoleH.nextChar();
        } while(!confirmation.equals('y'));
        
        do
        {
            charClass = consoleH.nextLine();
            System.out.println("What class do you want? Warrior, Rogue, or Mage?");
            if(charClass.equalsIgnoreCase("warrior")) {classType = WARRIOR; break; }
            else if(charClass.equalsIgnoreCase("rogue")) { classType = ROGUE; break; }
            else if(charClass.equalsIgnoreCase("mage")) {classType = MAGE; break; }
        } while(true);
        
        player = new Player(charName, classType);
    }
}

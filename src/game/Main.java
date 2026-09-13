package game;

import game.combat.CombatController;
import game.entities.player.PlayerController;
import game.mainmenu.MainMenuController;
import game.persistence.SaveFile;
import game.persistence.PersistenceController;
import game.reward.RewardController;
import game.world.RoomController;
import game.world.WorldController;

import java.util.Scanner;

public class Main
{
    public static void main(String[] args)
    {
        System.out.println(intro);
        
        Scanner scanner = new Scanner(System.in);
        ConsoleHandler consoleH = new ConsoleHandler(scanner);
        SaveFile saveFile = new SaveFile();
        CombatController combatC = new CombatController(consoleH);
        MainMenuController mainMenuC = new MainMenuController(consoleH);
        PersistenceController persistenceC = new PersistenceController(consoleH, saveFile);
        PlayerController playerC = new PlayerController(consoleH);
        RewardController rewardC = new RewardController(consoleH);
        RoomController roomC = new RoomController(consoleH);
        WorldController worldC = new WorldController(consoleH);
        
        GameController gameController = new GameController(combatC, mainMenuC, persistenceC, playerC, rewardC, roomC, worldC);
        
        int exitValue = gameController.start();

        scanner.close();
        System.exit(exitValue);
    }
    
    private static final String intro =
    "Welcome to Tech Space.\nUse '~' whenever you wish to go back." +
        "Select an Option:";
}
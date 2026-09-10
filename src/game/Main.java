package game;

import game.combat.CombatController;
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
        Scanner scanner = new Scanner(System.in);
        ConsoleHandler consoleH = new ConsoleHandler(scanner);
        SaveFile saveFile = new SaveFile();
        CombatController combatC = new CombatController(consoleH);
        MainMenuController mainMenuC = new MainMenuController(consoleH);
        PersistenceController persistenceC = new PersistenceController(consoleH, saveFile);
        RewardController rewardC = new RewardController(consoleH);
        RoomController roomC = new RoomController(consoleH);
        WorldController worldC = new WorldController(consoleH);
        
        GameController gameController = new GameController(combatC, mainMenuC, persistenceC, roomC, worldC);
        
        int exitValue = gameController.start();

        scanner.close();
        System.exit(exitValue);
    }
}

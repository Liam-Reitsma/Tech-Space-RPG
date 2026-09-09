package game;

import game.mainmenu.MainMenuController;
import game.persistence.SaveFile;
import game.persistence.PersistenceController;
import game.world.WorldController;

import java.util.Scanner;

public class Main
{
    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        ConsoleHandler consoleH = new ConsoleHandler(scanner);
        SaveFile saveFile = new SaveFile();
        PersistenceController saveFH = new PersistenceController(saveFile);
        MainMenuController mmController = new MainMenuController(consoleH, saveFH);
        WorldController worldController = new WorldController(consoleH);
        
        GameController gameController = new GameController(mmController, worldController);
        
        int exitValue = gameController.start();

        scanner.close();
        System.exit(exitValue);
    }
}

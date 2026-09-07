package game;

import game.mainmenu.MainMenuController;
import game.persistence.SaveFile;
import game.persistence.SaveFileHandler;

import java.util.Scanner;

public class Main
{
    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        ConsoleHandler consoleH = new ConsoleHandler(scanner);
        SaveFile saveFile = new SaveFile();
        SaveFileHandler saveFH = new SaveFileHandler(saveFile);
        MainMenuController mmController = new MainMenuController(consoleH, saveFH);
        
        GameController gameController = new GameController(mmController);
        
        int exitValue = gameController.start();

        scanner.close();
        System.exit(exitValue);
    }
}

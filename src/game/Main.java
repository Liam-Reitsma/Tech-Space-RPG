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
        MainMenuController gameC = new MainMenuController(consoleH, saveFH);
        
        gameC.run();

        scanner.close();
        System.exit(0);
    }
}

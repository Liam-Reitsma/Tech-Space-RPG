package game;

import java.util.Scanner;

public class Main
{
    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        ConsoleHandler consoleH = new ConsoleHandler(scanner);
        SaveFile saveFile = new SaveFile();
        SaveFileHandler saveFH = new SaveFileHandler(saveFile);
        GameController gameC = new GameController(consoleH, saveFH);
        
        gameC.run();

        scanner.close();
        System.exit(0);
    }
}

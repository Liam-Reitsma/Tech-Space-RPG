package game;

import static game.entities.ClassType.*;

import game.entities.*;
import java.util.Scanner;

public class Main
{
    public static void main(String[] args)
    {
        System.out.println("Hello World");

        Scanner scanner = new Scanner(System.in);

        boolean noName = true;
        boolean noClass = true;
        String name;
        String response;
        ClassType classType = null;

        do
        {
            System.out.println("What is your character's name?");
            name = scanner.next();
            System.out.println("Are you sure you want this name?");
            response = scanner.next();
            if(response.substring(0, 1).equalsIgnoreCase("y"))
            {
                noName = false;
            }
        } while(noName);

        do
        {
            noClass = false;
            response = scanner.next();
            System.out.println("What class do you want? Warrior, Rogue, or Mage?");
            if(response.equalsIgnoreCase("warrior")) {classType = WARRIOR; }
            else if(response.equalsIgnoreCase("rogue")) { classType = ROGUE; }
            else if(response.equalsIgnoreCase("mage")) {classType = MAGE; }
            else noClass = true;
        } while(noClass);

        Player player = new Player(name, classType);

        System.out.println("Your name is: " + player.getName());
        System.out.println("Your chosen class is: " + player.getClassType());
        System.out.println("Your stats are: " + player.getStatsToString());

        scanner.close();
    }
    
    public void MainMenu()
    {
    
    }
    
    public void Introduction()
    {
    
    }
}

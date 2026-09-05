package game;

import java.util.InputMismatchException;
import java.util.Scanner;

public class ConsoleHandler
{
    Scanner scanner;
    
    private boolean splitNumber;
    
    public ConsoleHandler(Scanner scanner)
    {
        this.scanner = scanner;
    }
    
    public void printLine(String line)
    {
        System.out.println(line);
    }
    
    public String nextLine()
    {
        return scanner.nextLine();
    }
    
    public char nextChar()
    {
        return scanner.next().charAt(0);
    }
    
    public char nextCharToLowerCase()
    {
        return scanner.next().toLowerCase().charAt(0);
    }
    
    public int nextInt()
    {
        splitNumber = false;
        try
        {
            return scanner.nextInt();
        } catch(InputMismatchException e)
        {
            int input = parseInt(nextLine());
            if(splitNumber && input != -1)
            {
                printLine("Your input is: " + input + ". Is this correct?");
                if(nextCharToLowerCase() == 'y') return input;
            }
            else if(input != -1) return input;
            
            printLine("Please input a valid number.");
            return nextInt();
        }
    }
    
    private int parseInt(String string)
    {
        if(string.isEmpty()) return -1;
        int result;
        try
        {
            result = Integer.parseInt(string);
        } catch(NumberFormatException e)
        {
            if(!splitNumber)
            {
                try
                {
                    Integer.parseInt(string.substring(string.length()-1));
                    splitNumber = true;
                    result = parseInt(string.substring(0, string.length()-1));
                } catch(NumberFormatException ex)
                {
                    result = parseInt(string.substring(0, string.length()-1));
                }
            }
            else
            {
                result = parseInt(string.substring(0, string.length()-1));
            }
            
        }
        
        return result;
    }
}

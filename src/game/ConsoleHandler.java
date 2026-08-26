package game;

import java.util.Scanner;

public class ConsoleHandler
{
    Scanner scanner;
    
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
        return scanner.nextLine().charAt(0);
    }
    
    public int nextInt()
    {
        return scanner.nextInt();
    }
}

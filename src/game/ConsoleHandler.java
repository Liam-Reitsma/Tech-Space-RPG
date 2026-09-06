package game;

import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;

public class ConsoleHandler
{
    Scanner scanner;
    
    private boolean splitNumber;
    private boolean hasMoreWords;
    private int wordIndex;
    private String[] line;
    
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
    
    public String next()
    {
        if(hasMoreWords)
        {
            if(wordIndex < line.length) hasMoreWords = false;
        }
        else
        {
            line = scanner.nextLine().split(" ");
            if(line.length > 1) hasMoreWords = true;
            wordIndex = 0;
        }
       
        return line[wordIndex++];
    }
    
    public char nextChar()
    {
        return next().charAt(0);
    }
    
    public char nextCharToLowerCase()
    {
        String result = nextLine();
        while(result.isEmpty())
        {
            printLine("Please input something!");
            result = nextLine();
        }
        
        return result.toLowerCase().charAt(0);
    }
    
    public String confirmInput(String questionToAsk)
    {
        String result;
        String question;
        if(questionToAsk.contains(String.valueOf('?'))) question = questionToAsk.substring(0, questionToAsk.length() - 1);
        else question = questionToAsk;
        printLine(question + "?");
        
        do
        {
            result = nextLine();
            printLine("Are you sure?");
            if(nextCharToLowerCase() == 'y') break;
            printLine("Alright, " + question.toLowerCase() + " then?");
        } while(true);
        
        return result;
    }
    
    public <T extends Enum<T>> T confirmInput(String questionToAsk, String options, Class<T> type)
    {
        T result = null;
        String response;
        String question;
        if(questionToAsk.contains(String.valueOf('?'))) question = questionToAsk.substring(0, questionToAsk.length() - 1);
        else question = questionToAsk;
        printLine(question + "?");
        printLine("The options are: " + options + ".");
        
        do
        {
            response = nextLine().toLowerCase();
            for(T constant : type.getEnumConstants())
            {
                if(constant.name().toLowerCase().equals(response))
                {
                    result = constant;
                    break;
                }
            }
            
            if(result == null)
            {
                printLine("Sorry, that is not a valid input. Please try again.");
                printLine("The options are: " + options + ".");
            }
            else
            {
                printLine("Are you sure?");
                if(nextCharToLowerCase() != 'y')
                {
                    result = null;
                    printLine("Alright, " + question.toLowerCase() + " then?");
                    printLine(options + "?");
                }
            }
        } while(result == null);
        
        return result;
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

package game;

import java.util.InputMismatchException;
import java.util.Optional;
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
    
    public InputResult<String> nextLine()
    {
        String input = scanner.nextLine();
        if(input.equals("~")) return new InputResult<>();
        
        return new InputResult<>(input);
    }
    
    public InputResult<String> next()
    {
        if(hasMoreWords)
        {
            if(wordIndex >= line.length - 1) hasMoreWords = false;
        }
        else
        {
            Optional<String> iResult = nextLine().getInput();
            if(iResult.isEmpty()) return new InputResult<>();
            line = iResult.get().split(" ");
            if(line.length > 1) hasMoreWords = true;
            wordIndex = 0;
        }
        
        return new InputResult<>(line[wordIndex++]);
    }
    
    public InputResult<Character> nextChar()
    {
        Optional<String> iResult = next().getInput();
        
        return iResult.isPresent() ? new InputResult<>(iResult.get().charAt(0)) : new InputResult<>();
    }
    
    public InputResult<Character> nextCharToLowerCase()
    {
        Optional<String> iResult = next().getInput();
        
        return iResult.isPresent() ? new InputResult<>(iResult.get().toLowerCase().charAt(0)) : new InputResult<>();
    }
    
    public InputResult<String> confirmInput(String questionToAsk)
    {
        String result;
        String question;
        if(questionToAsk.contains(String.valueOf('?'))) question = questionToAsk.substring(0, questionToAsk.length() - 1);
        else question = questionToAsk;
        printLine(question + "?");
        
        do
        {
            Optional<String> iResultString;
            Optional<Character> iResultChar;
            iResultString = nextLine().getInput();
            if(iResultString.isEmpty()) return new InputResult<>();
            else result = iResultString.get();
            printLine("Are you sure?");
            iResultChar = nextCharToLowerCase().getInput();
            if(iResultChar.isEmpty())
            {
                printLine("Alright, " + question.toLowerCase() + " then?");
                continue;
            }
            if(iResultChar.get() == 'y') break;
            if(iResultChar.get() == 'n') printLine("Alright, " + question.toLowerCase() + " then?");
            else printLine(question + "?");
        } while(true);
        
        return new InputResult<>(result);
    }
    
    public InputResult<Integer> confirmInput(String questionToAsk, String options, String splitter)
    {
        int result;
        String question;
        if(questionToAsk.contains(String.valueOf('?'))) question = questionToAsk.substring(0, questionToAsk.length() - 1);
        else question = questionToAsk;
        printLine(question + "?");
        printLine("The options are:\n" + options);
        
        do
        {
            Optional<Integer> iResultInt;
            Optional<Character> iResultChar;
            iResultInt = nextInt().getInput();
            if(iResultInt.isEmpty()) return new InputResult<>();
            result = iResultInt.get();
            
            if(result > options.split(splitter).length) result = 0;

            if(result <= 0)
            {
                printLine("Sorry, that is not a valid input. Please try again.");
                printLine("The options are:\n" + options);
            }
            else
            {
                printLine("Are you sure?");
                iResultChar = nextCharToLowerCase().getInput();
                if(iResultChar.isEmpty()) continue;
                if(iResultChar.get() != 'y')
                {
                    result = 0;
                    printLine("Alright, " + question.toLowerCase() + " then?");
                    printLine(options + "?");
                }
            }
        } while(result <= 0);
        
        return new InputResult<>(result);
    }
    
    public <T extends Enum<T>> InputResult<T> confirmInput(String questionToAsk, String options, Class<T> type)
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
            Optional<String> iResultString;
            Optional<Character> iResultChar;
            iResultString = nextLine().getInput();
            if(iResultString.isEmpty()) return new InputResult<>();
            response = iResultString.get();
            for(T constant : type.getEnumConstants())
            {
                if(constant.name().equalsIgnoreCase(response))
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
                iResultChar = nextCharToLowerCase().getInput();
                if(iResultChar.isEmpty()) continue;
                if(iResultChar.get() != 'y')
                {
                    result = null;
                    printLine("Alright, " + question.toLowerCase() + " then?");
                    printLine(options + "?");
                }
            }
        } while(result == null);
        
        return new InputResult<>(result);
    }
    
    public InputResult<Integer> nextInt()
    {
        splitNumber = false;
        try
        {
            int result = scanner.nextInt();
            nextLine();
            return new InputResult<>(result);
        } catch(InputMismatchException e)
        {
            Optional<String> iResultString = nextLine().getInput();
            if(iResultString.isEmpty()) return new InputResult<>();
            String attempt = iResultString.get();
            
            int input = parseInt(attempt);
            if(splitNumber && input != -1)
            {
                printLine("Your input is: " + input + ". Is this correct?");
                Optional<Character> iResultChar = nextCharToLowerCase().getInput();
                if(iResultChar.isEmpty()) return new InputResult<>();
                if(iResultChar.get() == 'y') return new InputResult<>(input);
            }
            else if(input != -1) return new InputResult<>(input);
            
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

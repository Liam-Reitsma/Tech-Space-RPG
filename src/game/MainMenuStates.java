package game;

public enum MainMenuStates implements Menu
{
    NEW_GAME("Start a New Game.")
            {
                @Override
                public void execute(ConsoleHandler consoleH, SaveFileHandler saveFH)
                {
                    consoleH.printLine("What would you like to call the save file?");
                    saveFH.createNewSave(consoleH.nextLine());
                    
                    while(!saveFH.doesFileExist())
                    {
                        consoleH.printLine("Sorry that file already exists. Try another name.");
                        saveFH.createNewSave(consoleH.nextLine());
                    }
                }
            },
    LOAD_GAME("Load a Saved Game.")
            {
                @Override
                public void execute(ConsoleHandler consoleH, SaveFileHandler saveFH)
                {
                    consoleH.printLine("Which save would you like to use?");
                    consoleH.printLine(saveFH.toString());
                    saveFH.findSave(consoleH.nextInt());
                }
            },
    EXIT("Exit the Game")
            {
                @Override
                public void execute(ConsoleHandler consoleH, SaveFileHandler saveFH)
                {
                    consoleH.printLine("Exiting the game now...");
                }
            };
    
    final String message;
    
    MainMenuStates(String s)
    {
        message = s;
    }
    
    @Override
    public String toString()
    {
        return message;
    }
}
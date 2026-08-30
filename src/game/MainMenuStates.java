package game;

public enum MainMenuStates implements Menu
{
    NEW_GAME("Start a New Game.")
            {
                @Override
                public void execute(ConsoleHandler consoleH, SaveFileHandler saveFH)
                {
                    consoleH.printLine("What would you like to call the save file?");
                    
                    do
                    {
                        Errors result = saveFH.createNewSave(consoleH.nextLine());
                        
                        if(result == Errors.SUCCESS) break;
                        consoleH.printLine(result.toString());
                    } while(true);
                }
            },
    LOAD_GAME("Load a Saved Game.")
            {
                @Override
                public void execute(ConsoleHandler consoleH, SaveFileHandler saveFH)
                {
                    consoleH.printLine("Which save would you like to use?");
                    consoleH.printLine(saveFH.listAllSaves());
                    do
                    {
                        Errors result = saveFH.selectSave(consoleH.nextInt());
                        
                        if(result == Errors.SUCCESS) break;
                        consoleH.printLine(result.toString());
                    } while(true);
                    
                    LoadResult loadResult = saveFH.loadData();
                    if(loadResult.getError() == Errors.SUCCESS) { consoleH.printLine(loadResult.getPlayerData()); }
                    else { consoleH.printLine(loadResult.getError().toString()); }
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
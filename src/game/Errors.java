package game;

public enum Errors
{
    INVALID_FILE_NAME("The file given is an invalid name. Please try again"),
    FILE_ALREADY_EXISTS("The file given already exists and could not be created."),
    SUCCESS("");
    
    private final String errorMessage;
    
    Errors(String message)
    {
        errorMessage = message;
    }
    
    @Override
    public String toString()
    {
        return errorMessage;
    }
}

package game.persistence;

public enum SaveErrors
{
    INVALID_FILE_NAME("Creating Save Failed! The file given is an invalid name. Please try again"),
    FILE_ALREADY_EXISTS("Creating Save Failed! The file given already exists and could not be created."),
    NUMBER_OUT_OF_BOUNDS("Loading Failed! The number selected is not a save game."),
    WRITE_ERROR("Saving Failed! An unknown error occurred while saving your game."),
    LOAD_ERROR("Loading Failed! An unknown error occurred while loading your game."),
    MISSING_DATA("Loading Failed! Some data was lost during loading."),
    SAVE_FILE_MISSING("Loading Failed! Save file is missing."),
    SAVE_FILE_CORRUPTED("Loading Failed! Save file data is corrupted."),
    SAVE_FOLDER_LOCATION_MISSING("Getting Saves Failed! Cannot find save file location."),
    SAVE_FOLDER_EMPTY("No save files found."),
    SUCCESS("");
    
    private final String errorMessage;
    
    SaveErrors(String message)
    {
        errorMessage = message;
    }
    
    @Override
    public String toString()
    {
        return errorMessage;
    }
}

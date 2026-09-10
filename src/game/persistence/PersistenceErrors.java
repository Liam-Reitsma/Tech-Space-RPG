package game.persistence;

import game.Errors;

public enum PersistenceErrors implements Errors
{
    LOADING_MAP_FAILED("Loading Map Failed! The map data was unable to be loaded."),
    LOADING_ITEM_FAILED("Loading Item Failed! The item data was unable to be loaded."),
    LOADING_PLAYER_FAILED("Loading Player Failed! The player data was unable to be loaded."),
    UNABLE_TO_SAVE("Saving Player Failed! The updated player data size is too big."),
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
    SAVE_FILE_EMPTY("Loading Failed! Save file is empty."),
    USER_WENT_BACK(""),
    SUCCESS("");
    
    private final String errorMessage;
    
    PersistenceErrors(String message)
    {
        errorMessage = message;
    }
    
    @Override
    public String toString()
    {
        return errorMessage;
    }
}

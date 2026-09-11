package game.persistence;

import game.ErrorSeverity;
import game.Errors;

import static game.ErrorSeverity.*;

public enum PersistenceErrors implements Errors
{
    LOADING_MAP_FAILED("Loading Map Failed! The map data was unable to be loaded.", SEVERE),
    LOADING_ITEM_FAILED("Loading Item Failed! The item data was unable to be loaded.", RECOVERABLE),
    LOADING_WEAPON_FAILED("Loading Weapon Failed! The weapon data was unable to be loaded.", RECOVERABLE),
    LOADING_PLAYER_FAILED("Loading Player Failed! The player data was unable to be loaded.", SEVERE),
    UNABLE_TO_SAVE("Saving Player Failed! The updated player data size is too big.", RECOVERABLE),
    INVALID_FILE_NAME("Creating Save Failed! The file given is an invalid name. Please try again", RECOVERABLE),
    FILE_ALREADY_EXISTS("Creating Save Failed! The file given already exists and could not be created.", RECOVERABLE),
    NUMBER_OUT_OF_BOUNDS("Loading Failed! The number selected is not a save game.", RECOVERABLE),
    WRITE_ERROR("Saving Failed! An unknown error occurred while saving your game.", RECOVERABLE),
    LOAD_ERROR("Loading Failed! An unknown error occurred while loading your game.", SEVERE),
    MISSING_DATA("Loading Failed! Some data was lost during loading.", RECOVERABLE),
    SAVE_FILE_MISSING("Loading Failed! Save file is missing.", SEVERE),
    SAVE_FILE_CORRUPTED("Loading Failed! Save file data is corrupted.", SEVERE),
    SAVE_FOLDER_LOCATION_MISSING("Getting Saves Failed! Cannot find save file location.", SEVERE),
    SAVE_FOLDER_EMPTY("No save files found.", RECOVERABLE),
    SAVE_FILE_EMPTY("Loading Failed! Save file is empty.", RECOVERABLE),
    USER_WENT_BACK("", NONE),
    SUCCESS("", NONE);
    
    private final String errorMessage;
    private final ErrorSeverity errorSeverity;
    
    PersistenceErrors(String message, ErrorSeverity severity)
    {
        errorMessage = message;
        errorSeverity = severity;
    }
    
    @Override
    public String getMessage() { return errorMessage; }
    
    @Override
    public ErrorSeverity getSeverity() { return errorSeverity; }
}
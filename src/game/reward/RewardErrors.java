package game.reward;

import game.Errors;

public enum RewardErrors implements Errors
{
    SUCCESS("");

    private final String message;

    RewardErrors(String message)
    {
        this.message = message;
    }
}

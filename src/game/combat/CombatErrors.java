package game.combat;

import game.Errors;

public enum CombatErrors implements Errors
{
    WIN("You Win."),
    LOSE("You Lose."),
    FLEE("You Ran Away."),;

    private final String message;

    CombatErrors(String s)
    {
        message = s;
    }

    @Override
    public String toString()
    {
        return message;
    }
}

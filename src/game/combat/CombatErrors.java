package game.combat;

import game.Errors;

public enum CombatErrors implements Errors
{
    SUCCESS("");

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

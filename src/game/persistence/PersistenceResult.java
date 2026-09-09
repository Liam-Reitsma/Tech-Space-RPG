package game.persistence;

import game.Result;

public class PersistenceResult extends Result<PersistenceErrors>
{
    public PersistenceResult(PersistenceErrors e)
    {
        error = e;
    }
}

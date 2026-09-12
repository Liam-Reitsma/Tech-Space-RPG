package game.entities.player;

import game.Result;

public class PlayerResult extends Result<PlayerError>
{
    public PlayerResult(PlayerError er)
    {
        error = er;
    }
}

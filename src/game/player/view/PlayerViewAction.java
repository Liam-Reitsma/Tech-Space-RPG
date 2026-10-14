package game.player.view;

import game.player.model.StatEntry;

import java.util.List;

public interface PlayerViewAction
{
    void updatePlayerStats(List<StatEntry> statEntries);
}
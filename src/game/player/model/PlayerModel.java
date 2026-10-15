package game.player.model;

import game.maingame.model.GameModel;

import java.util.ArrayList;
import java.util.List;

public class PlayerModel
{
    private Player player;
    
    private final GameModel gameModel;
    
    public PlayerModel(GameModel gModel)
    {
        gameModel = gModel;
    }
    
    public List<StatEntry> getPlayerStatEntries()
    {
        player = gameModel.getPlayer();
        List<StatEntry> statEntries = new ArrayList<>();
        statEntries.add(new StatEntry("", "", ""));
        
        statEntries.add(new StatEntry("Health", Math.round(player.getHealth()) + "/" + Math.round(player.getMaximumHealth()), "Your current health out of your maximum health"));
        statEntries.add(new StatEntry("Level", player.getLevel(), "Your current level"));
        statEntries.add(new StatEntry("XP", player.getXP() + "/" + player.getXpToLevelUp(), "Your current XP out of XP required to level up"));
        statEntries.add(new StatEntry("", "", ""));
        
        statEntries.add(new StatEntry("Damage", player.attack(), "Your current damage per hit"));
        statEntries.add(new StatEntry("Prot. Value", player.getProtectionValue(), "Your current damage reduction from each hit."));
        statEntries.add(new StatEntry("", "", ""));
        
        statEntries.add(new StatEntry("Stats", "", "Increasing these will give the player scaling buffs and increase your damage with that type of weapon"));
        statEntries.add(new StatEntry("STR", player.getSTR(), "Increases Maximum Health"));
        statEntries.add(new StatEntry("DEX", player.getDEX(), "Increases Armour Penetration"));
        statEntries.add(new StatEntry("INT", player.getINT(), "Increases damage amplification and damage reduction"));
        statEntries.add(new StatEntry("", "", ""));
        
        statEntries.add(new StatEntry("Buffs", "", "Your current buffs due to your stat distribution"));
        statEntries.add(new StatEntry("Max Health", Math.round(player.getMaximumHealth()), "Increases maximum available health"));
        statEntries.add(new StatEntry("Armour Pen.", player.getArmourPenetrationS(), "Reduces enemy protection value by this amount; does not go below 0% protection value."));
        statEntries.add(new StatEntry("Damage Amp.", player.getDamageAmplification(), "Increases your damage by this amount"));
        statEntries.add(new StatEntry("Damage Red.", player.getDamageReduction(), "Increases your protection value by this amount"));
        
        return statEntries;
    }
}
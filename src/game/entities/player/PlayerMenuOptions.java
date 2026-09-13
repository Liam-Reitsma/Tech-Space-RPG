package game.entities.player;

public enum PlayerMenuOptions
{
    PROCEED_PAST,
    VIEW_INVENTORY,
    EQUIP_WEAPON,
    UNEQUIP_WEAPON,
    EQUIP_ARMOR,
    UNEQUIP_ARMOR,
    SAVE_GAME,
    EXIT_GAME;

    @Override
    public String toString()
    {
        String[] names = name().split("_");
        return names[0].charAt(0) + names[0].substring(1).toLowerCase() + " " + names[1].charAt(0) + names[1].substring(1).toLowerCase();
    }
    
    public static String optionsToString()
    {
        StringBuilder result = new StringBuilder();
        int i = 1;
        for(PlayerMenuOptions option : PlayerMenuOptions.values())
        {
            result.append(i++).append(". ").append(option.toString()).append("\n");
        }
        result.deleteCharAt(result.length() - 1);
        return result.toString();
    }
}
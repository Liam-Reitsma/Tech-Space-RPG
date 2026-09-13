package game.entities.player;

public enum PlayerMenuOption
{
    SKIP_MENU,
    VIEW_INVENTORY,
    EQUIP_WEAPON,
    UNEQUIP_WEAPON,
    EQUIP_ARMOR,
    UNEQUIP_ARMOR,
    SAVE_GAME,
    EXIT_GAME;

    public static String optionsToString()
    {
        StringBuilder result = new StringBuilder();
        int i = 1;
        for(PlayerMenuOption option : PlayerMenuOption.values())
        {
            result.append(i++).append(". ").append(option.toString()).append("\n");
        }
        result.deleteCharAt(result.length() - 1);
        return result.toString();
    }
}
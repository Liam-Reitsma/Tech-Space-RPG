package game.entities.player;

public enum PlayerMenuOption
{
    VIEW_INVENTORY,
    EQUIP_WEAPON,
    UNEQUIP_WEAPON,
    EQUIP_ARMOR,
    UNEQUIP_ARMOR;

    public static String optionsToString()
    {
        StringBuilder result = new StringBuilder();
        int i = 1;
        for(PlayerMenuOption option : PlayerMenuOption.values())
        {
            result.append(option.toString()).append(", ");
        }
        return result.toString();
    }
}

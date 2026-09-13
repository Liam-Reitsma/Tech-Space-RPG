package game.world;

import game.entities.Persistable;

import java.util.Random;

public enum RoomType
{
    COMBAT(true, new String[] {
            "As the door opens, a lone figure turns towards you and raises their weapon.",
            "A single hostile steps into view from the far side of the room.",
            "One armed enemy stands ahead, watching your approach carefully.",
            "The room appears quiet at first, until a lone opponent moves into your path.",
            "A solitary combatant waits inside, clearly prepared for a fight."
    }),
    
    CHEST(true, new String[] {
            "At the centre of the room sits a sealed storage container.",
            "Against the far wall, you spot a locked supply crate.",
            "Among the scattered equipment rests an intact storage chest.",
            "A reinforced container sits beneath the room's dim lighting.",
            "Near the back of the room, a closed equipment crate catches your attention."
    }),
    
    MINIBOSS(true, new String[] {
            "A heavily armoured figure stands ahead, blocking the way forward.",
            "A larger and more imposing enemy turns towards you as you enter.",
            "At the centre of the room waits a well-equipped opponent, clearly stronger than those before.",
            "A powerful combatant steps forward, weapon ready.",
            "The door closes behind you as a heavily armed enemy prepares to engage."
    }),
    
    BOSS(true, new String[] {
            "The doors open into a large chamber, where a powerful figure waits at the far end.",
            "As you enter, your final opponent turns to face you.",
            "The room falls silent as the figure ahead rises to meet you.",
            "At the centre of the chamber stands the enemy you have been searching for.",
            "The doors seal behind you as the room's most dangerous occupant steps forward."
    }),
    
    EMPTY(false, new String[] {
            "The door opens onto a quiet, unoccupied room.",
            "Inside, you find only scattered equipment and inactive machinery.",
            "The room appears abandoned, with no sign of movement.",
            "Your footsteps echo through the otherwise empty chamber.",
            "Aside from a few damaged objects, there seems to be nothing here."
    });
    
    private final boolean hasReward;
    private final String[] messages;
    private final Random random;
    
    RoomType(boolean reward, String[] messageArray)
    {
        hasReward = reward;
        messages = messageArray;
        
        random = new Random();
    }
    
    public boolean hasReward() { return hasReward; }
    
    public String getRoomFlavourText()
    {
        return messages[random.nextInt(5)];
    }
    
    @Override
    public String toString()
    {
        return switch(this)
        {
            case COMBAT ->   "roomType" + Persistable.FIELD_SEPARATOR + "Combat";
            case CHEST ->    "roomType" + Persistable.FIELD_SEPARATOR +"Chest";
            case MINIBOSS -> "roomType" + Persistable.FIELD_SEPARATOR +"Miniboss";
            case BOSS ->     "roomType" + Persistable.FIELD_SEPARATOR +"Boss";
            case EMPTY ->    "roomType" + Persistable.FIELD_SEPARATOR +"Empty";
        };
    }
    
    public static int maxToStringLength() { return MINIBOSS.toString().length(); }
    
    public static RoomType parseRoom(String data)
    {
        return switch(data)
        {
            case "Combat" -> COMBAT;
            case "Chest" -> CHEST;
            case "Miniboss" -> MINIBOSS;
            case "Boss" -> BOSS;
            default -> EMPTY;
        };
    }
}

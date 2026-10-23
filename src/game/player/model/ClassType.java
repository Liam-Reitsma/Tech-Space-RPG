package game.player.model;

import static game.items.weapon.WeaponType.*;

import game.items.weapon.WeaponType;

public enum ClassType
{
    GUARDIAN(5, 3, 3, ENERGY_SWORD,
            """
            Once a front-line soldier who held ground longer than anyone expected, he earned \
            a reputation for refusing orders to fall back, even when it cost him. That same \
            stubbornness turned on someone it shouldn't have, and he ended up locked in the \
            brig for it, dragged aboard in restraints built for someone twice his size. He sat \
            through the silence with his arms crossed, treating the cell like just one more \
            order he didn't feel like following. When the moment came, he drove his shoulder \
            into the door until the frame gave way around it. \
            """),
    SCOUNDREL(3, 5, 3, PULSE_PISTOL,
            """
            He'd talked his way out of worse scrapes than this one, right up until the job \
            that finally caught up with him mid-score. The guards who dragged him in still \
            hadn't found half of what he'd lifted on the way, which was the only thing keeping \
            him in a good mood. When the moment came, he slipped the cuffs and was through \
            the door and gone before the guard could even turn around. \
            """),
    SAGE(3, 3, 5, GRAVITY_CONDUIT,
            """
            He'd been brought in for asking questions the ship's officers didn't want answered, \
            questions that turned out to be closer to the truth than anyone was comfortable \
            admitting. They'd locked him up to keep him quiet, but a mind like his didn't need \
            company to keep working. When the moment came, he'd already mapped the cell's lock \
            cycle from the sound of it resetting, and he walked out the second it clicked open \
            on its own. \
            """),
    MERCENARY(4, 4, 3, SHOCK_DAGGER,
            """
            He'd taken plenty of contracts that paid well for staying quiet about who signed them, \
            and the one that landed him in the brig was no different, except this time the client \
            was the one who turned him in. Nobody on this ship had bothered to ask who he used to \
            work for, which suited him fine since he wasn't planning to be around to answer. When \
            the moment came, he grabbed the guard's arm through the food slot the second it opened, \
            hauled him into the door, and used the man's own keycard before he'd finished hitting \
            the floor. \
            """),
    SHADOW(3, 4, 4, SHOCK_DAGGER,
            """
            He'd been hired to settle a dispute the quiet way, the kind that wasn't supposed to \
            leave a trail, but someone on the client's end had sold the job out before he ever \
            reached the target. The crew that dragged him in still didn't know what he'd been sent \
            there to do, only that he'd gotten close enough to try. When the moment came, he'd \
            already traced the door's control line to a junction panel within arm's reach, and \
            he shorted it at the exact second the guard's patrol put his back to the cell. \
            """),
    POWERTECH(4, 3, 4, ENERGY_SWORD,
            """
            He'd been chasing a bounty worth enough to retire on when the mark turned out to have \
            friends in the crew running this ship, and they weren't interested in letting the \
            contract get collected. They threw him in the brig to sort out later, not realizing \
            "later" assumed he'd still be there. When the moment came, he rerouted the cell's power \
            conduit into the door's bolt housing, overloaded it until the steel warped, and put his \
            shoulder through what was left. \
            """),
    SCAVENGER(2, 2, 2, RUSTED_KNIFE,
            """
            Nobody on this ship could say for certain why he was in the brig, least of all the guards \
            who'd picked him up wandering a cargo hold with someone else's gear stuffed in his coat. \
            He didn't offer an explanation either, mostly because he'd stopped keeping track of which \
            parts of his story were true a long time ago. When the moment came, there wasn't a clever \
            plan behind it, the door just hadn't been latched properly, and he was gone before anyone \
            noticed it swinging. \
            """);
    
    private final int statStrength;
    private final int statDexterity;
    private final int statIntelligence;
    private final WeaponType startingWeapon;
    private final String startingDescription;
    
    ClassType(int sStr, int sDex, int sInt, WeaponType weapon, String description)
    {
        statStrength = sStr;
        statDexterity = sDex;
        statIntelligence = sInt;
        startingWeapon = weapon;
        startingDescription = description;
    }
    
    @Override
    public String toString()
    {
        String[] words = name().split("_");
        
        for(int i = 0; i < words.length; i++)
            words[i] = words[i].charAt(0) + words[i].substring(1).toLowerCase();
        
        return String.join(" ", words);
    }
    
    public String toFileName()
    {
        return toString().replace(" ", "_");
    }
    
    public int getStatStrength() { return statStrength; }
    
    public int getStatDexterity() { return statDexterity; }
    
    public int getStatIntelligence() { return statIntelligence; }

    public WeaponType getStartingWeapon() { return startingWeapon; }
    
    public String getStartingDescription() { return startingDescription; }
}
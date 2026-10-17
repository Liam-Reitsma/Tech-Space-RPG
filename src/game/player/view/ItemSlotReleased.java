package game.player.view;

import game.player.model.SlotIdentity;

@FunctionalInterface
public interface ItemSlotReleased
{
    void onRelease(SlotIdentity destination);
}
package game.player.view;

import java.awt.event.MouseEvent;

@FunctionalInterface
public interface ItemSlotReleased
{
    void onRelease(MouseEvent e, ItemSlot dragSlot);
}
package game.player.view;

import java.awt.event.MouseEvent;

public interface ItemSlotDragged
{
    void onDrag(MouseEvent e, ItemSlot dragSlot);
}
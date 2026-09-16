package game.gui;

import javax.swing.*;
import java.awt.event.ActionListener;

public class ConfirmButton extends JButton
{
    public ConfirmButton(String name, ActionListener e)
    {
        super(name);
        addActionListener(e);
    }
}
package game.combat.view;

import game.gui.common.scaledlayout.ScaledLayout;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.*;

public class CombatTextPane extends JPanel
{

    private final JScrollPane scrollPane;
    private final JTextPane logPane;

    public CombatTextPane()
    {
        setLayout(new ScaledLayout());
        logPane = new JTextPane();
        scrollPane = new JScrollPane(logPane);
        logPane.setEditable(false);

        add(scrollPane);
    }

    public void update(String text)
    {
        try{
            Document currentLog = logPane.getDocument();
            currentLog.insertString(currentLog.getLength(), text + "\n", null);
        }
        catch(BadLocationException e){
            e.printStackTrace();
        }
    }
}

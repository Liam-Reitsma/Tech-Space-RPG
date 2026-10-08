package game.combat.view;

import game.gui.common.scaledlayout.ScaledLayout;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.*;

public class CombatTextPane extends JPanel
{
    private static final Image MAP_IMAGE = new ImageIcon("resources/images/World Map.png").getImage();
    private static final Font FONT = new Font("Agency FB", Font.PLAIN, 20);

    private final JScrollPane scrollPane;
    private final JTextPane logPane;

    public CombatTextPane()
    {
        setLayout(new ScaledLayout());
        logPane = new JTextPane();
        scrollPane = new JScrollPane(logPane);
        logPane.setEditable(false);
        logPane.setOpaque(false);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        logPane.setFont(FONT);
        logPane.setForeground(Color.WHITE);
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

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.drawImage(MAP_IMAGE, 0, 0, getWidth(), getHeight(), this);
    }
}

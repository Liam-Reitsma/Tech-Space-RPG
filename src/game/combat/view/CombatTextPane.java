package game.combat.view;

import game.gui.common.scaledlayout.ScaledLayout;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CombatTextPane extends JPanel
{
    private static final Font FONT = new Font("Agency FB", Font.PLAIN, 15);
    private static final Image IMAGE = new ImageIcon("resources/images/World Map.png").getImage();
    private static final Logger LOGGER = Logger.getLogger(CombatTextPane.class.getName());

    private final JScrollPane scrollPane;
    private final JTextPane textPane;

    public CombatTextPane()
    {
        setLayout(new ScaledLayout());
        textPane = new JTextPane();
        scrollPane = new JScrollPane(textPane);
        textPane.setEditable(false);
        textPane.setOpaque(false);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        textPane.setFont(FONT);
        textPane.setForeground(Color.BLACK);
        add(scrollPane);
    }
    
    public void clearLog()
    {
        textPane.setText("");
    }

    public void updateLog(String text)
    {
        try
        {
            Document currentLog = textPane.getDocument();
            currentLog.insertString(currentLog.getLength(), text + "\n", null);
            textPane.setCaretPosition(currentLog.getLength());
        } catch(BadLocationException e)
        {
            LOGGER.log(Level.SEVERE, "Failed to append combat log message.", e);
        }
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.drawImage(IMAGE, 0, 0, getWidth(), getHeight(), this);
    }
}
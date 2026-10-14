package game.player.view;

import game.gui.common.scaledlayout.LayoutBox;
import game.gui.common.scaledlayout.ScaledLayout;
import game.player.model.StatEntry;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlayerStatsPanel extends JPanel
{
    private static final Logger LOGGER = Logger.getLogger(PlayerStatsPanel.class.getName());
    
    private final StyledDocument statTextDocument;
    private final StyledDocument statValueDocument;
    
    public PlayerStatsPanel()
    {
        setLayout(new ScaledLayout());
        
        LayoutBox[] boxes = new LayoutBox().splitHorizontal(50, 50);
        
        JTextPane statTextPane = new JTextPane();
        statTextPane.setEditable(false);
        statTextPane.setOpaque(false);
        statTextPane.setFont(new Font("Agency FB", Font.BOLD, 17));
        statTextPane.setForeground(Color.BLACK);
        add(statTextPane, boxes[0]);
        statTextDocument = statTextPane.getStyledDocument();
        
        JTextPane statValuePane = new JTextPane();
        statValuePane.setEditable(false);
        statValuePane.setOpaque(false);
        statValuePane.setFont(new Font("Agency FB", Font.PLAIN, 17));
        statValuePane.setForeground(Color.BLACK);
        add(statValuePane, boxes[1]);
        statValueDocument = statValuePane.getStyledDocument();
    }
    
    public void updatePlayerStats(List<StatEntry> statEntries)
    {
        try
        {
            SimpleAttributeSet textAttributeSet = new SimpleAttributeSet();
            SimpleAttributeSet valueAttributeSet = new SimpleAttributeSet();
            StyleConstants.setAlignment(valueAttributeSet, StyleConstants.ALIGN_CENTER);
            
            statTextDocument.remove(0, statTextDocument.getLength());
            statValueDocument.remove(0, statValueDocument.getLength());
            
            for(StatEntry statEntry : statEntries)
            {
                textAttributeSet = new SimpleAttributeSet();
                textAttributeSet.addAttribute("tooltip", statEntry.tooltip());
                
                statTextDocument.insertString(statTextDocument.getLength(), statEntry.name() + "\n", textAttributeSet);
                statValueDocument.insertString(statValueDocument.getLength(), statEntry.value() + "\n", valueAttributeSet);
            }
            
            statTextDocument.setParagraphAttributes(0, statTextDocument.getLength(), textAttributeSet, false);
            statValueDocument.setParagraphAttributes(0, statValueDocument.getLength(), valueAttributeSet, false);
        } catch(BadLocationException e)
        {
            LOGGER.log(Level.SEVERE, "Failed to update player stats", e);
        }
    }
}
package game.gui;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame
{
    private static final int programWidth = 1280;
    private static final int programHeight = 720;
    
    public GameFrame(String name)
    {
        super(name);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(programWidth, programHeight);
        setLocationRelativeTo(null);
        
        setLayout(new BorderLayout());
    }
}
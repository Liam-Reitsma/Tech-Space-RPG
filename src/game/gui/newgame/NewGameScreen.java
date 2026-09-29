package game.gui.newgame;

import game.entities.player.ClassType;

import javax.swing.*;
import java.awt.*;

public class NewGameScreen extends JPanel
{
    private static final Image MAIN_MENU_IMAGE = new ImageIcon("resources/images/New Game Background.png").getImage();
    
    private final JPanel picturePanel;
    private final JPanel characterPanel;
    
    private final JPanel characterInputPanel;
    private final JTextField nameTextField;
    private final JComboBox<ClassType> classComboBox;
    
    private final JPanel classInfoPanel;
    private final JLabel classStatsLabel;
    private final JLabel classBonusesLabel;
    private final JLabel classDescriptionLabel;
    
    private final JPanel navigationPanel;
    private final JButton backButton;
    private final JButton confirmButton;
    
    public NewGameScreen()
    {
        picturePanel = new JPanel();
        characterPanel = new JPanel();
        
        characterInputPanel = new JPanel();
        nameTextField = new JTextField();
        classComboBox = new JComboBox<>();
        
        classInfoPanel = new JPanel();
        classStatsLabel = new JLabel();
        classBonusesLabel = new JLabel();
        classDescriptionLabel = new JLabel();
        
        navigationPanel = new JPanel();
        backButton = new JButton();
        confirmButton = new JButton();
    }
}
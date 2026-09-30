package game.gui.newgame;

import game.entities.player.ClassType;
import game.gui.common.PicturePreview;

import javax.swing.*;
import java.awt.*;

public class NewGameScreen extends JPanel
{
    private static final Image NEW_GAME_IMAGE = new ImageIcon("resources/images/New Game Background.png").getImage();
    private static final Image GUARDIAN_IMAGE = new ImageIcon("resources/images/classes/Guardian.png").getImage();
    
    private JPanel picturePanel;
    private PicturePreview picturePreview;
    private JPanel characterPanel;
    
    private JPanel characterInputPanel;
    private JTextField nameTextField;
    private JComboBox<ClassType> classComboBox;
    
    private JPanel classInfoPanel;
    private JLabel classStatsLabel;
    private JLabel classBonusesLabel;
    private JLabel classDescriptionLabel;
    
    private JPanel navigationPanel;
    private JButton backButton;
    private JButton confirmButton;
    
    public NewGameScreen()
    {
        setLayout(new GridBagLayout());
        GridBagConstraints gbConstraints = new GridBagConstraints();
        
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        createPicturePanel(gbConstraints);
        createCharacterPanel(gbConstraints);
    }
    
    private void createPicturePanel(GridBagConstraints gbConstraints)
    {
        picturePanel = new JPanel(new BorderLayout());
        picturePreview = new PicturePreview(GUARDIAN_IMAGE);
        picturePanel.setOpaque(false);
        picturePanel.add(picturePreview, BorderLayout.CENTER);
        
        gbConstraints.gridx = 0;
        gbConstraints.gridy = 0;
        gbConstraints.gridheight = 1;
        gbConstraints.fill = GridBagConstraints.BOTH;
        gbConstraints.weightx = 1;
        gbConstraints.weighty = 1;
        
        add(picturePanel, gbConstraints);
    }
    
    private void createCharacterPanel(GridBagConstraints gbConstraints)
    {
        characterPanel = new JPanel(new GridBagLayout());
        characterPanel.setOpaque(false);
        GridBagConstraints characterPanelGBConstraints = new GridBagConstraints();
        createCharacterInputPanel(characterPanel, characterPanelGBConstraints);
        createClassInfoPanel(characterPanel, characterPanelGBConstraints);
        createNavigationPanel(characterPanel, characterPanelGBConstraints);
        
        gbConstraints.gridx = 1;
        gbConstraints.gridy = 0;
        gbConstraints.gridheight = 1;
        gbConstraints.fill = GridBagConstraints.BOTH;
        gbConstraints.weightx = 1;
        gbConstraints.weighty = 1;
        add(characterPanel, gbConstraints);
    }
    
    private void createCharacterInputPanel(JPanel characterPanel, GridBagConstraints gbConstraints)
    {
        characterInputPanel = new JPanel(new GridBagLayout());
        characterInputPanel.setOpaque(false);
        GridBagConstraints charGBConstraints = new GridBagConstraints();
        
        charGBConstraints.gridx = 0;
        charGBConstraints.gridheight = 1;
        charGBConstraints.fill = GridBagConstraints.NONE;
        charGBConstraints.anchor = GridBagConstraints.NORTH;
        charGBConstraints.weightx = 1;
        charGBConstraints.weighty = 0;
        
        charGBConstraints.gridy = 0;
        nameTextField = new JTextField(10);
        nameTextField.setFont(new Font("OCR Extended A", Font.BOLD, 50));
        nameTextField.setText("Insert Your Name Here!");
        nameTextField.setHorizontalAlignment(SwingConstants.CENTER);
        characterInputPanel.add(nameTextField, charGBConstraints);
        
        charGBConstraints.gridy = 1;
        classComboBox = new JComboBox<>();
        characterInputPanel.add(classComboBox, charGBConstraints);
        
        gbConstraints.gridx = 0;
        gbConstraints.gridy = 0;
        gbConstraints.gridheight = 1;
        gbConstraints.fill = GridBagConstraints.BOTH;
        gbConstraints.weightx = 1;
        gbConstraints.weighty = 0.25;
        characterPanel.add(characterInputPanel, gbConstraints);
    }
    
    private void createClassInfoPanel(JPanel characterPanel, GridBagConstraints gbConstraints)
    {
        classInfoPanel = new JPanel(new GridBagLayout());
        classInfoPanel.setOpaque(false);
        GridBagConstraints classGBConstraints = new GridBagConstraints();
        
        classGBConstraints.gridx = 0;
        classGBConstraints.gridheight = 1;
        classGBConstraints.fill = GridBagConstraints.BOTH;
        classGBConstraints.weightx = 1;
        classGBConstraints.weighty = 1;
        
        classGBConstraints.gridy = 0;
        classStatsLabel = new JLabel();
        classInfoPanel.add(classStatsLabel, classGBConstraints);
        
        classGBConstraints.gridy = 1;
        classBonusesLabel = new JLabel();
        classInfoPanel.add(classBonusesLabel, classGBConstraints);
        
        classGBConstraints.gridy = 2;
        classDescriptionLabel = new JLabel();
        classInfoPanel.add(classDescriptionLabel, classGBConstraints);
        
        gbConstraints.gridx = 0;
        gbConstraints.gridy = 1;
        gbConstraints.gridheight = 1;
        gbConstraints.fill = GridBagConstraints.BOTH;
        gbConstraints.weightx = 1;
        gbConstraints.weighty = 0.6;
        characterPanel.add(classInfoPanel, gbConstraints);
    }
    
    private void createNavigationPanel(JPanel characterPanel, GridBagConstraints gbConstraints)
    {
        navigationPanel = new JPanel(new GridBagLayout());
        navigationPanel.setOpaque(false);
        GridBagConstraints navigationGBConstraints = new GridBagConstraints();
        
        navigationGBConstraints.gridy = 0;
        navigationGBConstraints.gridheight = 1;
        navigationGBConstraints.fill = GridBagConstraints.NONE;
        navigationGBConstraints.anchor = GridBagConstraints.SOUTH;
        navigationGBConstraints.weightx = 1;
        navigationGBConstraints.weighty = 1;
        
        navigationGBConstraints.gridx = 0;
        backButton = new JButton("Back");
        navigationPanel.add(backButton, navigationGBConstraints);
        
        navigationGBConstraints.gridx = 1;
        confirmButton = new JButton("Confirm");
        navigationPanel.add(confirmButton, navigationGBConstraints);
        
        gbConstraints.gridx = 0;
        gbConstraints.gridy = 2;
        gbConstraints.gridheight = 1;
        gbConstraints.fill = GridBagConstraints.BOTH;
        gbConstraints.weightx = 1;
        gbConstraints.weighty = 0.15;
        characterPanel.add(navigationPanel, gbConstraints);
    }
    
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        
        g.drawImage(NEW_GAME_IMAGE, 0, 0, getWidth(), getHeight(), null);
    }
}
package game.gui.newgame;

import static game.gui.config.GUIConfig.PROGRAM_HEIGHT;
import static game.gui.config.GUIConfig.PROGRAM_WIDTH;

import game.entities.player.ClassType;
import game.gui.common.PanelButton;
import game.gui.common.PicturePreview;
import game.newgame.NewGameAction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class NewGameScreen extends JPanel
{
    private static final Image NEW_GAME_IMAGE = new ImageIcon("resources/images/New Game Background.png").getImage();
    private static final Image GUARDIAN_IMAGE = new ImageIcon("resources/images/classes/Guardian.png").getImage();
    private static final int BUTTON_PREFERRED_WIDTH = 400;
    private static final int BUTTON_PREFERRED_HEIGHT = 400;
    private final NewGameAction newGameAction;
    
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
    
    public NewGameScreen(NewGameAction ngAction)
    {
        newGameAction = ngAction;
        
        setLayout(new GridBagLayout());
        GridBagConstraints gbConstraints = new GridBagConstraints();
        
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        createCharacterPanel(gbConstraints);
        createPicturePanel(gbConstraints);
        
        addComponentListener(new ComponentAdapter()
        {
            @Override public void componentResized(ComponentEvent e)
            {
                for(Component component : characterInputPanel.getComponents())
                {
                    if(component instanceof JTextField textField)
                    {
                        textField.setFont(new Font(textField.getFont().getFontName(), textField.getFont().getStyle(),
                                (int)Math.max(Math.clamp(80 - 30000 * Math.log1p(1.0 / getWidth()), 0, 80), textField.getWidth() / 10.0)
                        ));
                    }
                    else if(component instanceof JComboBox<?> comboBox)
                    {
                        comboBox.setFont(new Font(comboBox.getFont().getFontName(), comboBox.getFont().getStyle(),
                                (int)Math.max(Math.clamp(80 - 30000 * Math.log1p(1.0 / getWidth()), 0, 80), comboBox.getWidth() / 10.0)
                        ));
                    }
                }
                for(Component component : classInfoPanel.getComponents())
                {
                    if(component instanceof JTextArea textArea)
                    {
                        int font = Math.max(12, textArea.getWidth() / 30);
                        textArea.setFont(new Font(textArea.getFont().getFontName(), textArea.getFont().getStyle(),
                                (int)Math.max(Math.clamp(60 - 30000 * Math.log1p(1.0 / getWidth()), 0, 15), textArea.getWidth() / 100.0)
                        ));
                    }
                }
                for(Component component : navigationPanel.getComponents())
                {
                    if(component instanceof PanelButton button)
                    {
                        button.setFont(new Font(button.getFont().getFontName(), button.getFont().getStyle(),
                                (int)Math.max(Math.clamp(80 - 30000 * Math.log1p(1.0 / getWidth()), 0, 80), button.getWidth() / 10.0)
                        ));
                        button.resizeButton((float)((BUTTON_PREFERRED_WIDTH * button.getParent().getWidth()) / PROGRAM_WIDTH), ((float)(BUTTON_PREFERRED_HEIGHT * button.getParent().getHeight()) / PROGRAM_HEIGHT));
                    }
                }
            }
        });
    }
    
    private void createPicturePanel(GridBagConstraints gbConstraints)
    {
        picturePanel = new JPanel(new BorderLayout());
        picturePreview = new PicturePreview();
        picturePanel.setOpaque(false);
        picturePanel.add(picturePreview, BorderLayout.CENTER);
        updateCharacter(new ImageIcon(ClassType.values()[0].getImagePath()).getImage(), ClassType.values()[0].getStartingDescription());
        
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
        nameTextField.setText("Insert Your Name!");
        nameTextField.setHorizontalAlignment(SwingConstants.CENTER);
        
        nameTextField.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e)
            {
                FontMetrics metrics = nameTextField.getFontMetrics(nameTextField.getFont());
                if(metrics.stringWidth(nameTextField.getText()) > nameTextField.getWidth() - 10)
                {
                    String text = nameTextField.getText();
                    nameTextField.setText(text.substring(0, text.length() - 1));
                }
            }
        });
        characterInputPanel.add(nameTextField, charGBConstraints);
        
        charGBConstraints.gridy = 1;
        classComboBox = new JComboBox<>(ClassType.values());
        classComboBox.setFont(new Font("OCR ExtendedA", Font.PLAIN, 40));
        classComboBox.setMaximumRowCount(4);
        classComboBox.addActionListener(e -> newGameAction.requestCharacterChange((ClassType)classComboBox.getSelectedItem()));
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
        backButton = new PanelButton("Back", e ->  newGameAction.requestGoBack());
        navigationPanel.add(backButton, navigationGBConstraints);
        
        navigationGBConstraints.gridx = 1;
        confirmButton = new PanelButton("Confirm", e -> newGameAction.requestConfirmCreation(nameTextField.getText(), (ClassType)classComboBox.getSelectedItem()));
        navigationPanel.add(confirmButton, navigationGBConstraints);
        
        gbConstraints.gridx = 0;
        gbConstraints.gridy = 2;
        gbConstraints.gridheight = 1;
        gbConstraints.fill = GridBagConstraints.BOTH;
        gbConstraints.weightx = 1;
        gbConstraints.weighty = 0.15;
        characterPanel.add(navigationPanel, gbConstraints);
    }
    
    public void updateCharacter(Image classImage, String startingDescription)
    {
        picturePreview.setImage(classImage);
        classDescriptionTextArea.setText(startingDescription);
    }
    
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        
        g.drawImage(NEW_GAME_IMAGE, 0, 0, getWidth(), getHeight(), null);
    }
}
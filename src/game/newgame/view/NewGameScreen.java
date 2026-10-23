package game.newgame.view;

import game.player.model.ClassType;
import game.gui.common.PanelButton;
import game.gui.common.PicturePreview;
import game.gui.common.scaledlayout.LayoutBox;
import game.gui.common.scaledlayout.ScaledConstraint;
import game.gui.common.scaledlayout.ScaledLayout;
import game.gui.common.scaledlayout.ScaledLayoutAnchor;
import game.gui.config.DisplayableScreen;
import game.newgame.controller.NewGameAction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class NewGameScreen extends JPanel implements NewGameViewAction, DisplayableScreen
{
    private static final Image NEW_GAME_IMAGE = new ImageIcon("resources/images/New Game Background.png").getImage();
    private static final String DEFAULT_NAME_TEXT_FIELD = "Insert Your Name!";
    private static final ClassType DEFAULT_CLASS_TYPE = ClassType.values()[0];
    
    private final NewGameAction newGameAction;
    
    private PicturePreview picturePreview;
    private JTextField nameTextField;
    private JComboBox<ClassType> classComboBox;
    private JTextArea classDescriptionTextArea;
    
    public NewGameScreen(NewGameAction ngAction)
    {
        setLayout(new ScaledLayout());
        newGameAction = ngAction;
        
        LayoutBox[] boxes = new LayoutBox().splitHorizontal(50, 50);
        
        createCharacterPanel(boxes[1]);
        createPicturePanel(boxes[0]);
    }
    
    private void createPicturePanel(LayoutBox characterPreviewBox)
    {
        picturePreview = new PicturePreview();
        
        add(picturePreview, new ScaledConstraint(1, characterPreviewBox, ScaledLayoutAnchor.CENTER));
    }
    
    private void createCharacterPanel(LayoutBox characterInfoBox)
    {
        LayoutBox[] boxes = characterInfoBox.splitVertical(32, 60, 8);
        
        createCharacterInputPanel(boxes[0].insets(10));
        createClassInfoPanel(boxes[1].insets(10));
        createNavigationPanel(boxes[2]);
    }
    
    private void createCharacterInputPanel(LayoutBox userInputBox)
    {
        LayoutBox[] boxes = userInputBox.splitVertical(50, 50);
        
        nameTextField = new JTextField(10);
        nameTextField.setFont(new Font("OCR Extended A", Font.BOLD, 50));
        nameTextField.setText(DEFAULT_NAME_TEXT_FIELD);
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
        
        add(nameTextField, new LayoutBox(boxes[0], 75, 30, ScaledLayoutAnchor.CENTER));
        
        classComboBox = new JComboBox<>(ClassType.values());
        classComboBox.setFont(new Font("OCR ExtendedA", Font.PLAIN, 40));
        classComboBox.setMaximumRowCount(4);
        classComboBox.addActionListener(e -> newGameAction.requestCharacterChange((ClassType)classComboBox.getSelectedItem()));
        add(classComboBox, new LayoutBox(boxes[1], 75, 30, ScaledLayoutAnchor.CENTER));
    }
    
    private void createClassInfoPanel(LayoutBox infoBox)
    {
        classDescriptionTextArea = new JTextArea();
        classDescriptionTextArea.setLineWrap(true);
        classDescriptionTextArea.setWrapStyleWord(true);
        classDescriptionTextArea.setEditable(false);
        classDescriptionTextArea.setOpaque(false);
        classDescriptionTextArea.setFocusable(false);
        classDescriptionTextArea.setFont(new Font("OCR ExtendedA", Font.PLAIN, 20));
        classDescriptionTextArea.setForeground(Color.WHITE);
        add(classDescriptionTextArea, infoBox);
    }
    
    private void createNavigationPanel(LayoutBox buttonBox)
    {
        LayoutBox[] boxes = buttonBox.splitHorizontal(50, 50);
        
        add(new PanelButton("Back", e ->  newGameAction.requestMainMenuScreen()), boxes[0]);
        
        add(new PanelButton("Confirm", e -> newGameAction.requestConfirmCreation(nameTextField.getText(), (ClassType)classComboBox.getSelectedItem())), boxes[1]);
    }
    
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        
        g.drawImage(NEW_GAME_IMAGE, 0, 0, getWidth(), getHeight(), null);
    }
    
    @Override
    public void changeCharacterPanel(ClassType classType)
    {
        picturePreview.setImage(new ImageIcon("resources/images/classes/" + classType.toFileName() + ".png").getImage());
        classDescriptionTextArea.setText(classType.getStartingDescription());
    }

    /**
     * Makes the screen invisible.
     *
     * @return The new game screen's JPanel instance.
     */
    @Override
    public JPanel getDisplay()
    {
        setVisible(false);
        return this;
    }

    /**
     * Makes the screen visible and plays the new game screen's music.
     * Also resets the player name to the default text and the selected class to its default.
     */
    @Override
    public void openDisplay()
    {
        newGameAction.requestOpenNewGameScreen();
        setVisible(true);
        nameTextField.setText(DEFAULT_NAME_TEXT_FIELD);
        classComboBox.setSelectedItem(DEFAULT_CLASS_TYPE);
    }
    
    @Override
    public void closeDisplay()
    {
        setVisible(false);
    }
}
package game.gui.common.scaledlayout.factory;

import javax.swing.*;
import java.awt.*;

public class FontFitFactory
{
    private FontFitFactory()
    {
    
    }
    
    public static FontFitStrategy createFontFit(Component component)
    {
        return switch(component)
        {
            case AbstractButton button -> new ButtonFontFit();
            case JLabel label -> new LabelFontFit();
            case JComboBox<?> comboBox -> new ComboBoxFontFit();
            case JTextField textField -> new TextFieldFontFit();
            case JTextArea textArea -> new TextAreaFontFit();
            default -> null;
        };
    }
}
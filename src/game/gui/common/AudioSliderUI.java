package game.gui.common;

import javax.swing.*;
import javax.swing.plaf.basic.BasicSliderUI;
import java.awt.*;

public class AudioSliderUI extends BasicSliderUI
{
    private static final int ALPHA_PASSES = 8;
    private static final int MIN_ALPHA = 128;
    private static final int MAX_ALPHA = 256;
    
    private static final Color UNFILLED_COLOR = Color.BLACK;
    
    private final Color barColour;
    private final float strokeSize;
    private final float thumbSizeScale;
    private final int xBorderGap;
    private final int yBorderGap;
    
    public AudioSliderUI(JSlider slider, Color colour, float barHeight, float barWidth)
    {
        super(slider);
        barColour = colour;
        strokeSize = (barHeight + barWidth) / 2;
        thumbSizeScale = barHeight * 4;
        xBorderGap = Math.round(barHeight * 70);
        yBorderGap = Math.round(barWidth * 5);
        
        slider.addChangeListener(e -> slider.repaint());
    }
    
    @Override
    public void paintTrack(Graphics g)
    {
        Graphics2D graphics = (Graphics2D)g;
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int centreY = trackRect.y + trackRect.height / 2;
        int startX = trackRect.x;
        int endX = trackRect.x + trackRect.width;
        int splitX = thumbRect.x + thumbRect.width / 2;
        
        int x = trackRect.x - xBorderGap;
        int y = trackRect.y - yBorderGap;
        int height = trackRect.height + 2 * yBorderGap;
        int width = trackRect.width + 2 * xBorderGap;
        
        createLineGlow(graphics, startX, splitX, centreY);
        createEmptyBar(graphics, splitX, endX, centreY);
        createBorderGlow(graphics, x, y, height, width);
    }
    
    private void createLineGlow(Graphics2D graphics, int startX, int endX, int y)
    {
        int red = barColour.getRed();
        int green = barColour.getGreen();
        int blue = barColour.getBlue();
        
        for(int i = MIN_ALPHA; i <= MAX_ALPHA; i += (MIN_ALPHA / ALPHA_PASSES))
        {
            float t = Math.clamp(((float)(MAX_ALPHA - i) / MIN_ALPHA), 0f, 1f);
            graphics.setStroke(new BasicStroke(Math.max(strokeSize * t, 1f)));
            graphics.setColor(new Color(Math.round(red + (255 - red) * t), Math.round(green + (255 - green) * t), Math.round(blue + (255 - blue) * t), (i - 1)));
            graphics.drawLine(startX, y, endX, y);
        }
    }
    
    private void createEmptyBar(Graphics2D graphics, int startX, int endX, int y)
    {
        graphics.setStroke(new BasicStroke(strokeSize));
        graphics.setColor(UNFILLED_COLOR);
        graphics.drawLine(startX, y, endX, y);
    }
    
    private void createBorderGlow(Graphics2D graphics, int x, int y, int height, int width)
    {
        int red = barColour.getRed();
        int green = barColour.getGreen();
        int blue = barColour.getBlue();
        
        for(int i = MIN_ALPHA; i <= MAX_ALPHA; i += (MIN_ALPHA / ALPHA_PASSES))
        {
            float t = Math.clamp(((float)(MAX_ALPHA - i) / MIN_ALPHA), 0f, 1f);
            graphics.setStroke(new BasicStroke(Math.max(strokeSize * t, 1f)));
            graphics.setColor(new Color(Math.round(red + (255 - red) * t), Math.round(green + (255 - green) * t), Math.round(blue + (255 - blue) * t), (i - 1)));
            graphics.drawRoundRect(x, y, width, height, height, height);
        }
    }
    
    @Override
    public void calculateTrackRect()
    {
        super.calculateTrackRect();
        
        trackRect.x += xBorderGap;
        trackRect.y += yBorderGap;
        trackRect.height -= 2 * yBorderGap;
        trackRect.width -= 2 * xBorderGap;
    }
    
    @Override
    public void paintThumb(Graphics g)
    {
        Graphics2D graphics = (Graphics2D)g;
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int height = thumbRect.height;
        int x = thumbRect.x;
        int y = thumbRect.y - (height - thumbRect.height) / 2;
        createBoxGlow(graphics, x, y, height);
    }
    
    private void createBoxGlow(Graphics2D graphics, int x, int y, int height)
    {
        int red = barColour.getRed();
        int green = barColour.getGreen();
        int blue = barColour.getBlue();
        
        for(int i = MIN_ALPHA; i <= MAX_ALPHA; i += (MIN_ALPHA / ALPHA_PASSES))
        {
            float t = Math.clamp(((float)(MAX_ALPHA - i) / MIN_ALPHA), 0f, 1f);
            graphics.setColor(new Color(Math.round(red + (255 - red) * t), Math.round(green + (255 - green) * t), Math.round(blue + (255 - blue) * t), (i - 1)));
            graphics.fillRoundRect(Math.round(x - ((height / 2f) * t) / 2), Math.round(y - ((height / 2f) * t)/2), Math.round(height + (height / 2f) * t), Math.round(height + (height / 2f) * t), Math.round((height + (height / 2f) * t) / 2), Math.round((height + (height / 2f) * t) / 2));
        }
        graphics.fillRoundRect(x, y, height, height, height, height);
    }
    
    @Override
    public Dimension getThumbSize()
    {
        int size = Math.round(slider.getHeight() * thumbSizeScale);
        return new Dimension(size, size);
    }
    
    @Override
    public void calculateThumbLocation()
    {
        super.calculateThumbLocation();
        
        thumbRect.y = trackRect.y + ((trackRect.height - thumbRect.height) / 2);
    }
}
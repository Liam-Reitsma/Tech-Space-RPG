package game.gui.config;

public enum DisplayResolution
{
    UHD(3840, 2160),
    QHD(2560, 1440),
    FHD(1920, 1080),
    HD(1280, 720),
    FWVGA(854, 480),
    FWQVGA(426, 240);
    
    private final int width;
    private final int height;
    
    DisplayResolution(int w, int h)
    {
        width = w;
        height = h;
    }
    
    public int getWidth() { return width; }
    
    public int getHeight() { return height; }
    
    @Override
    public String toString()
    {
        return width + "x" + height;
    }
}
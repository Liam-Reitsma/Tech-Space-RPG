package game.gui.common.scaledlayout;

public class ScaledConstraint implements ScaledLayoutConstraint
{
    private final float aspectRatio;
    private final LayoutBox box;
    private final ScaledLayoutAnchor alignment;
    
    public ScaledConstraint(float targetAspectRatio, LayoutBox targetBox, ScaledLayoutAnchor targetAlignment)
    {
        aspectRatio = targetAspectRatio > 0 ? targetAspectRatio : 1;
        box = targetBox;
        alignment = targetAlignment;
    }
    
    public float getAspectRatio() { return aspectRatio; }
    
    @Override
    public LayoutBox getBox() { return box; }
    
    public ScaledLayoutAnchor getAlignment() { return alignment; }
}
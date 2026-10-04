package game.saves.controller;

public interface SavesAction
{
    void requestOpenSavesOverlay();
    void requestSelectSave(String saveName);
    void requestCloseSaves();
}
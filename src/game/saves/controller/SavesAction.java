package game.saves.controller;

public interface SavesAction
{
    void requestOpenSavesOverlay();
    void requestDeleteSave(String saveName);
    void requestSelectSave(String saveName);
    void requestCloseSaves();
}
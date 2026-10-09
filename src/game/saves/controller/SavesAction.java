package game.saves.controller;

public interface SavesAction
{
    void requestOpenSavesOverlay();
    void requestDeleteSave(int deleteIndex);
    void requestSelectSave(int saveIndex);
    void requestCloseSaves();
}
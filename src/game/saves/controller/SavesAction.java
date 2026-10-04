package game.saves.controller;

public interface SavesAction
{
    String[] requestOpenSaves();
    void requestSelectSave(String saveName);
    void requestCloseSaves();
}
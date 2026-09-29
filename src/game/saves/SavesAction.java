package game.saves;

public interface SavesAction
{
    void requestSelectSave(String saveName);
    void requestCloseSaves();
}
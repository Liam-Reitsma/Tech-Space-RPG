package game.saves;

public interface SavesAction
{
    String[] requestOpenSaves();
    void requestSelectSave(String saveName);
    void requestCloseSaves();
}
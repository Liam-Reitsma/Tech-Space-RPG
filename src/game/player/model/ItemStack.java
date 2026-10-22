package game.player.model;

import game.items.Item;

public class ItemStack
{
    private Item item;
    private int quantity;
    
    public ItemStack()
    {
        item = null;
        quantity = 0;
    }
    
    public ItemStack(Item newItem, int newQuantity)
    {
        item = newItem;
        quantity = newQuantity;
    }
    
    public void setItem(Item newItem)
    {
        if(newItem == null)
            return;
        item = newItem;
        quantity = 1;
    }
    
    public void setItem(Item newItem, int newQuantity)
    {
        if(newItem == null || newQuantity <= 0)
            return;
        item = newItem;
        quantity = newQuantity;
    }
    
    public void addItem()
    {
        if(item != null)
            quantity++;
    }
    
    public void removeItem()
    {
        if(item != null)
            if(quantity <= 1)
            {
                item = null;
                quantity = 0;
            }
            else
                quantity--;
    }
    
    public void removeStack()
    {
        item = null;
        quantity = 0;
    }
    
    public Item getItem() { return item; }
    
    public int getQuantity() { return quantity; }
}
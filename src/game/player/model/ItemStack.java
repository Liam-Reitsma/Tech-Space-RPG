package game.player.model;

import game.items.Item;
import game.items.weapon.Weapon;

import java.util.Objects;

public class ItemStack
{
    private int quantity;
    private Item item;
    
    public ItemStack()
    {
        quantity = 0;
        item = null;
    }
    
    public ItemStack(Item newItem, int newQuantity)
    {
        if(newQuantity <= 0 || newItem == null)
        {
            quantity = 0;
            item = null;
        }
        else
        {
            quantity = newQuantity;
            item = newItem;
        }
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
                quantity = 0;
                item = null;
            }
            else
                quantity--;
    }
    
    public void removeItemStack()
    {
        quantity = 0;
        item = null;
    }
    
    public void setItem(Item newItem)
    {
        if(newItem == null)
            return;
        quantity = 1;
        item = newItem;
    }
    
    public ItemStack copy() { return new ItemStack(item == null ? null : item.copy(), quantity); }
    
    public Item getItem() { return item; }
    
    public int getQuantity() { return quantity; }
    
    @Override
    public boolean equals(Object object)
    {
        if(this == object)
            return true;
        if(!(object instanceof ItemStack otherStack))
            return false;
        else
            return quantity == otherStack.quantity && Objects.equals(item, otherStack.item);
    }
    
    @Override
    public int hashCode()
    {
        return Objects.hash(item, quantity);
    }
}
package game.entities;

import game.SaveExclude;

import java.lang.reflect.Field;

public interface Saveable
{
    default String getSaveInfo() throws IllegalAccessException
    {
        StringBuilder saveInfo = new StringBuilder();
        saveInfo.append(this.getName()).append(": ");
        Class<?> current = this.getClass();
        
        while(current != null && current != Object.class)
        {
            for(Field field : current.getDeclaredFields())
            {
                if(field.isAnnotationPresent(SaveExclude.class)) continue;
                field.setAccessible(true);
                saveInfo.append(field.getName()).append(" ").append(field.get(this)).append(", ");
            }
            
            current = current.getSuperclass();
        }
        
        saveInfo.delete(saveInfo.length() - 2, saveInfo.length());
        
        return saveInfo.toString();
    }
    
    String getName();
}

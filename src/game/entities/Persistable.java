package game.entities;

import java.lang.reflect.Field;

public interface Persistable
{
    static final char CLASS_SEPARATOR = '\u001E';
    static final char DATA_SEPARATOR  = '\u001F';
    static final char FIELD_SEPARATOR = '\u0005';
    
    default String getSaveInfo() throws IllegalAccessException
    {
        StringBuilder saveInfo = new StringBuilder();
        //Creates: "ClassName: "
        saveInfo.append(this.getClassName()).append(CLASS_SEPARATOR);
        Class<?> current = this.getClass();
        
        while(current != null && current != Object.class)
        {
            for(Field field : current.getDeclaredFields())
            {
                if(field.isAnnotationPresent(SaveExclude.class)) continue;
                field.setAccessible(true);
                //Creates: "Type-Name=Value, "
                saveInfo.append(field.getName()).append(FIELD_SEPARATOR).append(field.get(this)).append(DATA_SEPARATOR);
            }
            
            current = current.getSuperclass();
        }
        
        //Removes: ", " from end
        saveInfo.delete(saveInfo.length() - String.valueOf(DATA_SEPARATOR).length(), saveInfo.length());
        
        return saveInfo.toString();
    }
    
    default void loadSaveInfo(String[] data)
    {
        Class<?> current = this.getClass();
        String name;
        String value;
        
        while(current != null && current != Object.class)
        {
            for(String datum : data)
            {
                name = datum.split(String.valueOf(FIELD_SEPARATOR))[0];
                value = datum.split(String.valueOf(FIELD_SEPARATOR))[1];
                
                for(Field field : current.getDeclaredFields())
                {
                    if(field.isAnnotationPresent(SaveExclude.class)) continue;
                    field.setAccessible(true);
                    if(field.getName().equals(name))
                    {
                        try
                        {
                            if(field.getType().equals(int.class))
                            {
                                field.set(this, Integer.parseInt(value));
                            }
                            else if(field.getType().equals(String.class))
                            {
                                field.set(this, value);
                            }
                            else if(field.getType().equals(ClassType.class))
                            {
                                field.set(this, ClassType.parseType(value));
                            }
                            else if(field.getType().equals(float.class))
                            {
                                field.set(this, Float.parseFloat(value));
                            }
                        } catch(IllegalAccessException e)
                        {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
            
            current = current.getSuperclass();
        }
    }
    
    default String getClassName()
    {
        return this.getClass().getSimpleName();
    }
}
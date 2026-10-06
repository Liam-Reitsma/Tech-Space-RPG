package game.entities;

import game.world.model.Point;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/** Interface is used to load and save games.
 * Has 3 non-printable characters and used to separate each piece of data so that it can load accurately through loadSaveInfo().*/
public interface Persistable
{
    static final char CLASS_SEPARATOR = '\u001E';
    static final char DATA_SEPARATOR  = '\u001F';
    static final char FIELD_SEPARATOR = '\u0005';

    /** Method creates a string. It first save the class name, then goes through the class and saves each field name first then the value separated by FIELD_SEPARATOR non-printable. Each field is separated by the DATA_SEPARATOR non-printable character.
     * It then returns the string after it has added all the fields.*/
    default String getSaveInfo() throws IllegalAccessException
    {
        StringBuilder saveInfo = new StringBuilder();
        saveInfo.append(this.getClass().getName()).append(CLASS_SEPARATOR);
        Class<?> current = this.getClass();
        
        while(current != null && current != Object.class)
        {
            for(Field field : current.getDeclaredFields())
            {
                if(field.isAnnotationPresent(SaveExclude.class)) continue;
                field.setAccessible(true);
                if(field.getType() == Map.class) saveInfo.append(field.getName()).append(FIELD_SEPARATOR).append(field.get(this).toString()).append(DATA_SEPARATOR);
                else if(field.getType() == float.class) saveInfo.append(field.getName()).append(FIELD_SEPARATOR).append(Math.round((float)field.get(this))).append(DATA_SEPARATOR);
                else saveInfo.append(field.getName()).append(FIELD_SEPARATOR).append(field.get(this)).append(DATA_SEPARATOR);
            }
            
            current = current.getSuperclass();
        }
        
        saveInfo.delete(saveInfo.length() - String.valueOf(DATA_SEPARATOR).length(), saveInfo.length());
        
        return saveInfo.toString();
    }

    /** Method gets the class type. If it is not null, each piece of data will be separated by using FIELD_SEPARATOR and will go through all the fields and sets the correct value to it.*/
    default void loadSaveInfo(String[] data)
    {
        Class<?> current = this.getClass();
        String name;
        String value;
        
        while(current != null && current != Object.class)
        {
            for(String datum : data)
            {
                name = datum.split(String.valueOf(FIELD_SEPARATOR))[0].trim();
                value = datum.split(String.valueOf(FIELD_SEPARATOR))[1].trim();
                
                for(Field field : current.getDeclaredFields())
                {
                    if(field.isAnnotationPresent(SaveExclude.class)) continue;
                    field.setAccessible(true);
                    if(field.getName().equals(name))
                    {
                        try
                        {
                            if(field.getType().equals(int.class)) { field.set(this, Integer.parseInt(value)); }
                            else if(field.getType().equals(String.class)) { field.set(this, value); }
                            else if(field.getType().equals(float.class)) { field.set(this, Float.parseFloat(value)); }
                            else if(field.getType().equals(boolean.class)) { field.set(this, Boolean.parseBoolean(value)); }
                            else if(field.getType().equals(Point.class)) { field.set(this, Point.parsePoint(value)); }
                            else if(field.getType().equals(Map.class))
                            {
                                Map<StatType, Integer> map = new HashMap<>();
                                value = value.substring(1, value.length() - 1);
                                String[] strings = value.split(", ");
                                for(String string : strings)
                                {
                                    String[] result = string.split("=");
                                    for(StatType type : StatType.values())
                                    {
                                        if(Objects.equals(result[0], type.toString())) { map.put(type, Integer.parseInt(result[1]));}
                                    }
                                }
                                field.set(this, map);
                            }
                            else if(field.getType().isEnum())
                            {
                                for(Object type : field.getType().getEnumConstants())
                                {
                                    if(Objects.equals(value, type.toString()))
                                    {
                                        field.set(this, type);
                                        break;
                                    }
                                }
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
}
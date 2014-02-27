/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mainLeveledMobs;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

/**
 *
 * @author ferrago
 */
public class LeveledEntity{
    int lvl;
    int health;
    LeveledEntity(int lvlIn, int loc)
    {
        lvl  = lvlIn;
    }
    
    public int getLvl()
    {
        return lvl;
    }
    public void setLvl(int lvlIn)
    {
       lvl = lvlIn; 
    }
    
    public int getHp()
    {
        return health;
    }
    
    public void setHp(int hpIn)
    {
        health = hpIn;
    }
    
}

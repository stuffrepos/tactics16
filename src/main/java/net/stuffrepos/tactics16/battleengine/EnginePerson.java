package net.stuffrepos.tactics16.battleengine;

import net.stuffrepos.tactics16.battleengine.Map.MapCoordinate;

/**
 *
 * @author eduardo
 */
public interface EnginePerson {

    public MapCoordinate getPosition();

    public int getHealthPoints();

    public int getMaximumHealthPoints();

    public int getSpecialPoints();

    public int getMaximumSpecialPoints();

    public float getSpeedPoints();

    public float getSpeed();

    public boolean isAlive();

    public int getEvasiveness();

    public int getId();
    
    public int getPlayerId();
}

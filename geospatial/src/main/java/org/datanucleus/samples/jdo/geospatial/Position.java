package org.datanucleus.samples.jdo.geospatial;

import org.postgis.Point;

public class Position
{
    private String name;

    private Point point;

    public Position(String name, Point point)
    {
        this.name = name;
        this.point = point;
    }

    public String getName()
    {
        return name;
    }
    
    public Point getPoint()
    {
        return point;
    }
    
    public String toString()
    {
        return "[name] "+ name + " [point] "+point;
    }
}

package nh.util;

import java.util.LinkedList;
import java.util.List;
import nh.map.MapPoint;

public class DoublePolygon
{
   private final List<MapPoint> points;
   
   public DoublePolygon()
   {
      points = new LinkedList<MapPoint>();
   }

   public DoublePolygon add(MapPoint point)
   {
      points.add(point);
      return this;
   }
   
   public List<MapPoint> getPoints()
   {
      return points;
   }
}

package nh.map;

import java.awt.Point;
import nh.util.DoublePoint;

public class MapPoint extends DoublePoint
{
   public MapPoint(double x, double y)
   {
      super(x, y);
   }

   public MapPoint(MapPoint toCopy)
   {
      this(toCopy.x, toCopy.y);
   }

   public static MapPoint fromPoint(Point point)
   {
      return new MapPoint(point.x, point.y);
   }
}

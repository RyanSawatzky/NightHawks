package nh.map.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import nh.hex.HexCoordinate;
import nh.map.MapFacing;
import nh.map.MapPoint;
import nh.util.Coordinates;
import nh.util.DoublePoint;
import nh.util.DoublePolygon;
import nh.util.PolarPoint;

public abstract class Entity
{
   private HexCoordinate location;
   private MapFacing facing;
   
   protected Entity()
   {
      location = null;
      facing = null;
   }
   
   public Entity setLocation(HexCoordinate value)
   {
      location = value;
      return this;
   }
   
   public Entity setFacing(MapFacing value)
   {
      facing = value;
      return this;
   }
   
   public HexCoordinate getLocation()
   {
      return location;
   }
   
   public MapFacing getFacing()
   {
      return facing;
   }

   public abstract DoublePolygon getPolygon(MapPoint center, double facingInRadians);
   

   public static final double MaximumWidth = 56.0d;
   public static final double MaximumHeight = 68.0d;
   private static final double Maximum = Math.max(MaximumWidth, MaximumHeight);
   private static final double HalfMaximum = Maximum / 2.0d;

   public static List<PolarPoint> centerPoints(List<DoublePoint> list)
   {
      if(list.isEmpty())
         return Collections.emptyList();

      double minX = Double.MAX_VALUE;
      double minY = Double.MAX_VALUE;
      double maxX = Double.MIN_VALUE;
      double maxY = Double.MIN_VALUE;

      for(DoublePoint point : list)
      {
         minX = Math.min(minX, point.x);
         minY = Math.min(minY, point.y);
         maxX = Math.max(maxX, point.x);
         maxY = Math.max(maxY, point.y);
      }

      DoublePoint center = new DoublePoint(((maxX - minX) / 2.0d) + minX, ((maxY - minY) / 2.0d) + minY);
      List<PolarPoint> newList = new ArrayList<>(list.size());
      for(DoublePoint point : list)
      {
         DoublePoint shiftedPoint = new DoublePoint((point.x - minX - (maxX / 2.0d)) / HalfMaximum,
                                                    ((maxY / 2.0d) - (point.y - minY)) / HalfMaximum);
         newList.add(Coordinates.cartesianToPolar(shiftedPoint).rotate(Math.PI));
      }
      return newList;
   }
}

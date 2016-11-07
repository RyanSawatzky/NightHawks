package nh.map.entity;

import java.util.ArrayList;
import java.util.List;
import nh.util.DoublePoint;

public abstract class Entity
{
   public static final double MaximumWidth = 56.0d;
   public static final double MaximumHeight = 68.0d;
   private static final double Maximum = Math.max(MaximumWidth, MaximumHeight);
   private static final double HalfMaximum = Maximum / 2.0d;

   public static List<DoublePoint> centerPoints(List<DoublePoint> list)
   {
      if(list.isEmpty())
         return list;

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
      List<DoublePoint> newList = new ArrayList<>(list.size());
      for(DoublePoint point : list)
      {
         newList.add(Coordinates.cartesianToPolar(
                     new DoublePoint((point.x - minX - (maxX / 2.0d)) / HalfMaximum,
                                     ((maxY / 2.0d) - (point.y - minY)) / HalfMaximum)));
      }
      return newList;
   }
}

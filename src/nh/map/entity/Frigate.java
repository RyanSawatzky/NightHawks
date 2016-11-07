package nh.map.entity;

import java.util.LinkedList;
import java.util.List;
import nh.util.DoublePoint;

public class Frigate extends Entity
{
   public static final Frigate Frigate = new Frigate();

   private final List<DoublePoint> points;
   Frigate()
   {
      List<DoublePoint> iPoints = new LinkedList<DoublePoint>();
      iPoints.add(new DoublePoint(11.0d, 0.0d));
      iPoints.add(new DoublePoint(22.0d, 27.0d));
      iPoints.add(new DoublePoint(19.5d, 32.0d));
      iPoints.add(new DoublePoint(11.0d, 23.0d));
      iPoints.add(new DoublePoint(2.5d, 32.0d));
      iPoints.add(new DoublePoint(0.0d, 27.0d));
      points = centerPoints(iPoints);
   }
   
   public static void main(String[] args)
   {
      Frigate frigate = new Frigate();
      for(DoublePoint point : frigate.points)
      {
         System.out.println(point.toString());
      }
   }
}

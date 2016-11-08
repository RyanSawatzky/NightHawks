package nh.map.entity;

import java.util.LinkedList;
import java.util.List;
import nh.hex.HexMetrics;
import nh.map.MapPoint;
import nh.util.Coordinates;
import nh.util.DoublePoint;
import nh.util.DoublePolygon;
import nh.util.PolarPoint;

public class Frigate extends Entity
{
   private static final List<PolarPoint> points;
   static
   {
      List<DoublePoint> iPoints = new LinkedList<>();
      iPoints.add(new DoublePoint(11.0d, 0.0d));
      iPoints.add(new DoublePoint(22.0d, 27.0d));
      iPoints.add(new DoublePoint(17.5d, 31.0d));
      iPoints.add(new DoublePoint(11.0d, 24.0d));
      iPoints.add(new DoublePoint(4.5d, 31.0d));
      iPoints.add(new DoublePoint(0.0d, 27.0d));   // 645 494   - 0, 12.7 
      points = centerPoints(iPoints);
   }

   public Frigate()
   {
      super();
   }

   @Override
   public DoublePolygon getPolygon(MapPoint center, double facingInRadians)
   {
      DoublePolygon polygon = new DoublePolygon();
      for(PolarPoint polarPoint : points)
      {
         polygon.add(new MapPoint(Coordinates.polarToCartesian(polarPoint.rotate(facingInRadians).amplify(HexMetrics.HexSize)).translate(center)));
      }
      return polygon;
   }

   public static void main(String[] args)
   {
      for(PolarPoint point : Frigate.points)
      {
         System.out.println(Coordinates.polarToCartesian(point).toString());
      }
   }
}

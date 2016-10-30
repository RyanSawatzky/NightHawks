/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nh.hex;

import java.util.List;
import nh.map.MapPoint;
import nh.util.DoublePoint;

/**
 *
 * @author Ryan
 */
public abstract class HexMetrics
{
   public static final double SquareRootOfThree = Math.sqrt(3.0d);

   public abstract MapPoint hexCenter(HexCoordinate hex);
   public abstract List<MapPoint> hexPoints(HexCoordinate hex);
   public abstract CubeCoordinate mapPointToHex(double x, double y);

   public CubeCoordinate mapPointToHex(DoublePoint point)
   {
      return mapPointToHex(point.x, point.y);
   }

   public static CubeCoordinate roundToHex(double x, double y, double z)
   {
      double rx = Math.round(x);
      double ry = Math.round(y);
      double rz = Math.round(z);

      double xDiff = Math.abs(rx - x);
      double yDiff = Math.abs(ry - y);
      double zDiff = Math.abs(rz - z);
      
      if((xDiff > yDiff) && (xDiff > zDiff))
         rx = -ry - rz;
      else if(yDiff > zDiff)
         ry = -rx - rz;
      else
         rz = -rx - ry;
      
      return new CubeCoordinate((int)rx, (int)ry, (int)rz);
   }

   public static final HexMetrics create(Orientation orientation, double hexSize)
   {
      if(orientation.isHorizontal())
         return new HorizontalHexMetrics(hexSize);
      else
         return new VerticalHexMetrics(hexSize);
   }
}

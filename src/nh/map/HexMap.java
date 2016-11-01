package nh.map;

import java.util.List;
import nh.hex.AxialCoordinate;
import nh.hex.CubeCoordinate;
import nh.hex.HexCoordinate;
import nh.hex.HexMetrics;
import nh.hex.Orientation;

public class HexMap implements Map
{
   private final Orientation orientation;
   private final int mapSize;
   
   public HexMap(Orientation orientation, int mapSize)
   {
      this.orientation = orientation;
      this.mapSize = mapSize;
   }
   
   @Override
   public Orientation getOrientation()
   {
      return orientation;
   }

   @Override
   public boolean isValidHex(HexCoordinate coord)
   {
      CubeCoordinate cube = coord.toCube(orientation);
      return (cube.x >= -mapSize) &&
             (cube.x <= mapSize) &&
             (cube.y >= -mapSize) &&
             (cube.y <= mapSize) &&
             (cube.z >= -mapSize) &&
             (cube.z <= mapSize);
   }

   @Override
   public boolean isBorderHex(HexCoordinate coord)
   {
      CubeCoordinate cube = coord.toCube(orientation);
      return (cube.x == -mapSize) ||
             (cube.x == mapSize) ||
             (cube.y == -mapSize) ||
             (cube.y == mapSize) ||
             (cube.z == -mapSize) ||
             (cube.z == mapSize);
   }

   @Override
   public MapPoint getMapOrigin(HexMetrics hexMetrics)
   {
      List<MapPoint> leftPoints = hexMetrics.hexPoints(new AxialCoordinate(-mapSize, 0));
      List<MapPoint> topPoints = hexMetrics.hexPoints(new AxialCoordinate(0, -mapSize));

      double minX = 0;
      double minY = 0;

      for(MapPoint mapPoint : topPoints)
      {
         if(mapPoint.y < minY)
            minY = mapPoint.y;
      }
      for(MapPoint mapPoint : leftPoints)
      {
         if(mapPoint.x < minX)
            minX = mapPoint.x;
      }
      
      return new MapPoint(minX, minY);
   }

   @Override
   public MapPoint getMapExtent(HexMetrics hexMetrics)
   {
      List<MapPoint> bottomPoints = hexMetrics.hexPoints(new AxialCoordinate(0, mapSize));
      List<MapPoint> rightPoints = hexMetrics.hexPoints(new AxialCoordinate(mapSize, 0));

      double maxX = 0;
      double maxY = 0;

      for(MapPoint mapPoint : bottomPoints)
      {
         if(mapPoint.y > maxY)
            maxY = mapPoint.y;
      }
      for(MapPoint mapPoint : rightPoints)
      {
         if(mapPoint.x > maxX)
            maxX = mapPoint.x;
      }
      
      return new MapPoint(maxX, maxY);
   }
}

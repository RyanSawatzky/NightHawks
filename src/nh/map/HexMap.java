package nh.map;

import nh.hex.CubeCoordinate;
import nh.hex.HexCoordinate;
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
}

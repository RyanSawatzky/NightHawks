package nh.map;

import nh.hex.HexCoordinate;
import nh.hex.HexMetrics;
import nh.hex.Orientation;

public interface Map
{
   public Orientation getOrientation();
   public boolean isValidHex(HexCoordinate coord);
   public boolean isBorderHex(HexCoordinate coord);
   public MapPoint getMapOrigin(HexMetrics hexMetrics);
   public MapPoint getMapExtent(HexMetrics hexMetrics);
}

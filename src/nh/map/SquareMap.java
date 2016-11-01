package nh.map;

import java.util.List;
import nh.hex.CubeCoordinate;
import nh.hex.HexCoordinate;
import nh.hex.HexMetrics;
import nh.hex.OffsetCoordinate;
import nh.hex.Orientation;

public class SquareMap implements Map
{
   private final Orientation orientation;
   private final int numberCols;
   private final int numberRows;
   
   public SquareMap(Orientation orientation, int numberCols, int numberRows)
   {
      this.orientation = orientation;
      this.numberCols = numberCols;
      this.numberRows = numberRows;
   }
   
   @Override
   public Orientation getOrientation()
   {
      return orientation;
   }

   @Override
   public boolean isValidHex(HexCoordinate coord)
   {
      OffsetCoordinate offset = coord.toOffset(orientation);
      return (offset.col >= 0) &&
             (offset.col < numberCols) &&
             (offset.row >= 0) &&
             (offset.row < numberRows);
   }

   @Override
   public boolean isBorderHex(HexCoordinate coord)
   {
      OffsetCoordinate offset = coord.toOffset(orientation);
      return (offset.col == 0) ||
             (offset.col == (numberCols - 1)) ||
             (offset.row == 0) ||
             (offset.row == (numberRows - 1));
   }

   @Override
   public MapPoint getMapOrigin(HexMetrics hexMetrics)
   {
      return new MapPoint(0, 0);
   }

   @Override
   public MapPoint getMapExtent(HexMetrics hexMetrics)
   {
      List<MapPoint> bottomRowPoints = hexMetrics.hexPoints(new OffsetCoordinate(1, numberRows - 1));
      List<MapPoint> rightRowPoints = hexMetrics.hexPoints(new OffsetCoordinate(numberCols - 1, 1));

      double maxX = 0;
      double maxY = 0;

      for(MapPoint mapPoint : bottomRowPoints)
      {
         if(mapPoint.y > maxY)
            maxY = mapPoint.y;
      }
      for(MapPoint mapPoint : rightRowPoints)
      {
         if(mapPoint.x > maxX)
            maxX = mapPoint.x;
      }
      
      return new MapPoint(maxX, maxY);
   }
}

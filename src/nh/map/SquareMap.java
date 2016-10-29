package nh.map;

import nh.hex.CubeCoordinate;
import nh.hex.HexCoordinate;
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
}

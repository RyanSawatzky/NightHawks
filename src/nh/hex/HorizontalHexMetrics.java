package nh.hex;

import java.util.ArrayList;
import java.util.List;
import static nh.hex.HexMetrics.SquareRootOfThree;
import nh.map.MapPoint;

class HorizontalHexMetrics extends HexMetrics
{
   private final double hexSize;
   private final double height;
   private final double width;

   public HorizontalHexMetrics(double hexSize)
   {
      this.hexSize = hexSize;
      height = hexSize * 2;
      width = (int)Math.round((SquareRootOfThree/2) * height);
   }

   @Override
   public MapPoint hexCenter(HexCoordinate hex)
   {
      OffsetCoordinate offset = hex.toOffset(Orientation.Horizontal);
      double x = hexSize * SquareRootOfThree * (offset.col + (0.5d * (offset.row & 1)));
      double y = hexSize * 3.0d/2.0d * offset.row;
      return new MapPoint(x, y);
   }

   @Override
   public List<MapPoint> hexPoints(HexCoordinate hex)
   {
      OffsetCoordinate offset = hex.toOffset(Orientation.Horizontal);
      List<MapPoint> list = new ArrayList<>();

      double leftSide, center, rightSide;
      double tipTop, tipBottom, top, bottom;

      if((offset.row & 1) == 0)
      {
         leftSide = (offset.col * height * SquareRootOfThree) / 2;
         center = ((offset.col + 0.5d) * height * SquareRootOfThree) / 2;
         rightSide = ((offset.col + 1) * height * SquareRootOfThree) / 2;
         tipTop = offset.row * ((height * 3) / 4);
         tipBottom = tipTop + height;
         top = tipTop + (height / 4);
         bottom = tipBottom - (height / 4);
      }
      else
      {
         leftSide = ((offset.col + 0.5d) * height * SquareRootOfThree) / 2;
         center = ((offset.col + 1.0d) * height * SquareRootOfThree) / 2;
         rightSide = ((offset.col + 1.5d) * height * SquareRootOfThree) / 2;

         tipTop = (offset.row * ((height * 3) / 4));// - (height / 4);
         tipBottom = tipTop + height;
         top = tipTop + (height / 4);
         bottom = tipBottom - (height / 4);
      }

      list.add(new MapPoint(center, tipTop));
      list.add(new MapPoint(rightSide, top));
      list.add(new MapPoint(rightSide, bottom));
      list.add(new MapPoint(center, tipBottom));
      list.add(new MapPoint(leftSide, bottom));
      list.add(new MapPoint(leftSide, top));

      return list;
   }

   @Override
   public CubeCoordinate mapPointToHex(double x, double y)
   {
      double row = (y / ((height * 3) / 4));
      double rowFloor = Math.floor(row);
      double rowRemainder = row - rowFloor;
      int rowInt = (int)rowFloor;

      if((rowInt & 1) != 0)
         x -= width / 2;

      double col = x / width;
      double colFloor = Math.floor(col);
      double colRemainder = col - colFloor;
      int colInt = (int)colFloor;

      if(rowRemainder < (1.0d/3.0d))
      {
         if(colRemainder < 0.5d)
         {
            if(rowRemainder < ((1.0d / 3.0d) - (2.0d * colRemainder / 3.0d)))
            {
               if((rowInt & 1) == 0)
                  colInt--;
               rowInt--;
            }
         }
         else
         {
            colRemainder -= 0.5d;
            if(rowRemainder < ((2.0d * colRemainder) / 3.0d))
            {
               if((rowInt & 1) != 0)
                  colInt++;
               rowInt--;
            }
         }
      }

      return new OffsetCoordinate(colInt, rowInt).toCube(Orientation.Horizontal);
   }
}

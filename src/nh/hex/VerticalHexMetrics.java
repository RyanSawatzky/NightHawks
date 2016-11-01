package nh.hex;

import java.util.ArrayList;
import java.util.List;
import static nh.hex.HexMetrics.SquareRootOfThree;
import nh.map.MapPoint;

class VerticalHexMetrics extends HexMetrics
{
   private final double hexSize;
   private final double height;
   private final double width;

   public VerticalHexMetrics(double hexSize)
   {
      this.hexSize = hexSize;
      width = hexSize * 2;
      height = (SquareRootOfThree/2) * width;
   }

   @Override
   public MapPoint hexCenter(HexCoordinate hex)
   {
      OffsetCoordinate offset = hex.toOffset(Orientation.Vertical);
      double y = hexSize * SquareRootOfThree * (offset.row + (0.5d * (offset.col & 1)));
      double x = hexSize * 3.0d/2.0d * offset.col;
      return new MapPoint(x, y);
   }

   @Override
   public List<MapPoint> hexPoints(HexCoordinate hex)
   {
      OffsetCoordinate offset = hex.toOffset(Orientation.Vertical);
      List<MapPoint> list = new ArrayList<>();

      double tipLeft, tipRight, left, right;
      double top, center, bottom;

      if((offset.col & 1) == 0)
      {
         top = offset.row * height;
         center = (offset.row + 0.5d) * height;
         bottom = (offset.row + 1.0d) * height;

         tipLeft = (offset.col * 3 * width) / 4;
         left = tipLeft + (width / 4);
         tipRight = (((offset.col + 1) * 3 * width) / 4) + (width / 4);
         right = tipRight - (width / 4);
      }
      else
      {
         top = (offset.row * height) + (height / 2);
         center = ((offset.row + 0.5d) * height) + (height / 2);
         bottom = ((offset.row + 1.0d) * height) + (height / 2);

         tipLeft = (offset.col * 3 * width) / 4;
         left = tipLeft + (width / 4);
         tipRight = (((offset.col + 1) * 3 * width) / 4) + (width / 4);
         right = tipRight - (width / 4);
      }

      list.add(new MapPoint(right, top));
      list.add(new MapPoint(tipRight, center));
      list.add(new MapPoint(right, bottom));
      list.add(new MapPoint(left, bottom));
      list.add(new MapPoint(tipLeft, center));
      list.add(new MapPoint(left, top));

      return list;
   }

   @Override
   public CubeCoordinate mapPointToHex(double x, double y)
   {
      double col = (x / ((width * 3) / 4));
      double colFloor = Math.floor(col);
      double colRemainder = col - colFloor;
      int colInt = (int)colFloor;

      if((colInt & 1) != 0)
         y -= height / 2;

      double row = y / height;
      double rowFloor = Math.floor(row);
      double rowRemainder = row - rowFloor;
      int rowInt = (int)rowFloor;

      if(colRemainder < (1.0d/3.0d))
      {
         if(rowRemainder < 0.5d)
         {
            if(colRemainder < ((1.0d / 3.0d) - (2.0d * rowRemainder / 3.0d)))
            {
               if((colInt & 1) == 0)
                  rowInt--;
               colInt--;
            }
         }
         else
         {
            rowRemainder -= 0.5d;
            if(colRemainder < ((2.0d * rowRemainder) / 3.0d))
            {
               if((colInt & 1) != 0)
                  rowInt++;
               colInt--;
            }
         }
      }

      return new OffsetCoordinate(colInt, rowInt).toCube(Orientation.Vertical);
   }
}

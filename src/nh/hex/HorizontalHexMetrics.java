package nh.hex;

import java.util.ArrayList;
import java.util.List;
import static nh.hex.HexMetrics.SquareRootOfThree;
import nh.util.DoublePoint;

class HorizontalHexMetrics extends HexMetrics
{
   private final double hexSize;
   private final double height;
   private final double width;
   private final double verticalSpace;
   private final double horizontalSpace;
   private final double verticalStagger;
   private final double horizontalStagger;

   public HorizontalHexMetrics(double hexSize)
   {
      this.hexSize = hexSize;
      height = hexSize * 2;
      width = (int)Math.round((SquareRootOfThree/2) * height);
      verticalSpace = (int)Math.round(((double)height * 3.0d) / 4.0d);
      horizontalSpace = width;
      verticalStagger = 0;
      horizontalStagger = (int)Math.round((double)width / 2.0d);
   }

   @Override
   public DoublePoint hexCenter(HexCoordinate hex)
   {
      OffsetCoordinate offset = hex.toOffset(Orientation.Horizontal);
      double x = hexSize * SquareRootOfThree * (offset.col + (0.5d * (offset.row & 1)));
      double y = hexSize * 3.0d/2.0d * offset.row;
      return new DoublePoint(x, y);
   }

   @Override
   public List<DoublePoint> hexPoints(HexCoordinate hex)
   {
      OffsetCoordinate offset = hex.toOffset(Orientation.Horizontal);
      List<DoublePoint> list = new ArrayList<>();

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

      list.add(new DoublePoint(center, tipTop));
      list.add(new DoublePoint(rightSide, top));
      list.add(new DoublePoint(rightSide, bottom));
      list.add(new DoublePoint(center, tipBottom));
      list.add(new DoublePoint(leftSide, bottom));
      list.add(new DoublePoint(leftSide, top));

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

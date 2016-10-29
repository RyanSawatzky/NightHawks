/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nh.hex;

import java.util.ArrayList;
import java.util.List;
import nh.hex.CubeCoordinate;
import nh.hex.HexCoordinate;
import nh.hex.OffsetCoordinate;
import nh.hex.Orientation;
import nh.util.DoublePoint;

/**
 *
 * @author Ryan
 */
public class HexMetrics
{
   public static final double SquareRootOfThree = Math.sqrt(3.0d);

   private final Orientation orientation;
   public final double hexSize;
   public final double height;
   public final double width;
   public final double verticalSpace;
   public final double horizontalSpace;
   public final double verticalStagger;
   public final double horizontalStagger;

   private HexMetrics(Orientation orientation,
                      double hexSize,
                      double height,
                      double width,
                      double verticalSpace,
                      double horizontalSpace,
                      double verticalStagger,
                      double horizontalStagger)
   {
      this.orientation = orientation;
      this.hexSize = hexSize;
      this.height = height;
      this.width = width;
      this.verticalSpace = verticalSpace;
      this.horizontalSpace = horizontalSpace;
      this.verticalStagger = verticalStagger;
      this.horizontalStagger = horizontalStagger;
   }

   public DoublePoint hexCenter(HexCoordinate hex)
   {
      OffsetCoordinate offset = hex.toOffset(orientation);
      double x = hexSize * SquareRootOfThree * (offset.col + (0.5d * (offset.row & 1)));
      double y = hexSize * 3.0d/2.0d * offset.row;
      return new DoublePoint(x, y);
   }

   public List<DoublePoint> hexPoints(HexCoordinate hex)
   {
      OffsetCoordinate offset = hex.toOffset(orientation);
      List<DoublePoint> list = new ArrayList<>();

      if(orientation.isHorizontal())
      {
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
      }
      else
      {
         throw new IllegalArgumentException();
      }

      return list;
   }

   public CubeCoordinate mapPointToHex(DoublePoint point)
   {
      return mapPointToHex(point.x, point.y);
   }

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

      return new OffsetCoordinate(colInt, rowInt).toCube(orientation);
   }

   public CubeCoordinate roundToHex(double x, double y, double z)
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

   public static final HexMetrics create(Orientation orientation, int hexSize)
   {
      if(orientation.isHorizontal())
         return createHorizontalOrientation(hexSize);
      else
         throw new IllegalArgumentException();
   }

   public static final HexMetrics createHorizontalOrientation(int hexSize)
   {
      int height = hexSize * 2;
      int width = (int)Math.round((SquareRootOfThree/2) * height);
      int verticalSpace = (int)Math.round(((double)height * 3.0d) / 4.0d);
      int horizontalSpace = width;
      int verticalStagger = 0;
      int horizontalStagger = (int)Math.round((double)width / 2.0d);

      return new HexMetrics(Orientation.Horizontal,
                            hexSize,
                            height,
                            width,
                            verticalSpace,
                            horizontalSpace,
                            verticalStagger,
                            horizontalStagger);
   }
}

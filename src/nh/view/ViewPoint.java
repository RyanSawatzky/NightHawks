package nh.view;

import nh.util.DoubleDimension;
import nh.util.DoublePoint;

public class ViewPoint extends DoublePoint
{
   public ViewPoint(double x, double y)
   {
      super(x, y);
   }

   public ViewPoint(ViewPoint toCopy)
   {
      this(toCopy.x, toCopy.y);
   }
}

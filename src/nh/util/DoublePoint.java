package nh.util;

import java.awt.Point;

public class DoublePoint
{
   public final double x;
   public final double y;
   
   public DoublePoint(long x, long y)
   {
      this.x = x;
      this.y = y;
   }

   public DoublePoint(double x, double y)
   {
      this(Math.round(x), Math.round(y));
   }

   public Point toPoint()
   {
      return DoublePoint.toPoint(x, y);
   }
   
   public static DoublePoint fromPoint(Point point)
   {
      return new DoublePoint(point.x, point.y);
   }
   
   @Override
   public String toString()
   {
      return "(" + Double.toString(x) + ", " + Double.toString(y) + ")";
   }
   
   @Override
   public boolean equals(Object o)
   {
      if(o == this)
         return true;
      else if((o == null) || !(o instanceof DoublePoint))
         return false;
      else
      {
         DoublePoint other = (DoublePoint)o;
         return (Double.compare(this.x, other.x) == 0) &&
                (Double.compare(this.y, other.y) == 0);
      }
   }
   
   public static Point toPoint(double dX, double dY)
   {
      long x = Math.round(dX);
      long y = Math.round(dY);
      if(x > (long)Integer.MAX_VALUE)
         throw new IllegalStateException("LongPoint.toPoint() - x value '" + Long.toString(x) + "' larger than max integer size '" + Integer.toString(Integer.MAX_VALUE) + "'");
      if(y > (long)Integer.MAX_VALUE)
         throw new IllegalStateException("LongPoint.toPoint() - y value '" + Long.toString(y) + "' larger than max integer size '" + Integer.toString(Integer.MAX_VALUE) + "'");

      return new Point((int)x, (int)y);
   }
}

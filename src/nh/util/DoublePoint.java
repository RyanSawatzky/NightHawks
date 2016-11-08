package nh.util;

import java.awt.Point;

public class DoublePoint
{
   public double x;
   public double y;
   
   public DoublePoint(double x, double y)
   {
      this.x = x;
      this.y = y;
   }

   public DoublePoint(DoublePoint toCopy)
   {
      this(toCopy.x, toCopy.y);
   }

   public Point toPoint()
   {
      return DoublePoint.toPoint(x, y);
   }

   public DoublePoint translate(DoublePoint otherPoint)
   {
      this.x += otherPoint.x;
      this.y += otherPoint.y;
      return this;
   }

   public static DoublePoint fromPoint(Point point)
   {
      return new DoublePoint(point.x, point.y);
   }

   public DoubleDimension delta(DoublePoint other)
   {
      return new DoubleDimension(this.x - other.x, this.y - other.y);
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

   @Override
   public int hashCode()
   {
      int hash = 5;
      hash = 71 * hash + (int) (Double.doubleToLongBits(this.x) ^ (Double.doubleToLongBits(this.x) >>> 32));
      hash = 71 * hash + (int) (Double.doubleToLongBits(this.y) ^ (Double.doubleToLongBits(this.y) >>> 32));
      return hash;
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

package nh.util;

import java.awt.Dimension;

public class LongDimension
{
   public final long width;
   public final long height;
   
   public LongDimension(long width, long height)
   {
      this.width = width;
      this.height = height;
   }

   public Dimension toDimension()
   {
      if(width > (long)Integer.MAX_VALUE)
         throw new IllegalStateException("LongPoint.toPoint() - width value '" + Long.toString(width) + "' larger than max integer size '" + Integer.toString(Integer.MAX_VALUE) + "'");
      if(height > (long)Integer.MAX_VALUE)
         throw new IllegalStateException("LongPoint.toPoint() - y value '" + Long.toString(height) + "' larger than max integer size '" + Integer.toString(Integer.MAX_VALUE) + "'");

      return new Dimension((int)width, (int)height);
   }
   
   public static LongDimension fromDimension(Dimension dimension)
   {
      return new LongDimension(dimension.width, dimension.height);
   }
   
   @Override
   public String toString()
   {
      return "(" + Long.toString(width) + ", " + Long.toString(height) + ")";
   }
   
   @Override
   public boolean equals(Object o)
   {
      if(o == this)
         return true;
      else if((o == null) || !(o instanceof LongDimension))
         return false;
      else
      {
         LongDimension other = (LongDimension)o;
         return (this.width == other.width) && (this.height == other.height);
      }
   }

   @Override
   public int hashCode()
   {
      int hash = 3;
      hash = 53 * hash + (int) (this.width ^ (this.width >>> 32));
      hash = 53 * hash + (int) (this.height ^ (this.height >>> 32));
      return hash;
   }
}

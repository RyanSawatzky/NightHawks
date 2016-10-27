package nh.util;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Objects;

public class LongRectangle
{
   public final DoublePoint origin;
   public final LongDimension size;
   
   public LongRectangle(DoublePoint origin, LongDimension size)
   {
      this.origin = origin;
      this.size = size;
   }

   public LongRectangle(long x, long y, long width, long height)
   {
      this(new DoublePoint(x, y), new LongDimension(width, height));
   }

   public Rectangle toRectangle()
   {
      Point pOrigin = origin.toPoint();
      Dimension dSize = size.toDimension();

      return new Rectangle(pOrigin, dSize);
   }
   
   public static LongRectangle fromRectangle(Rectangle rect)
   {
      return new LongRectangle(new DoublePoint(rect.x, rect.y), new LongDimension(rect.width, rect.height));
   }
   
   @Override
   public String toString()
   {
      return origin.toString() + " -> " + size.toString();
   }
   
   @Override
   public boolean equals(Object o)
   {
      if(o == this)
         return true;
      else if((o == null) || !(o instanceof LongRectangle))
         return false;
      else
      {
         LongRectangle other = (LongRectangle)o;
         return origin.equals(other.origin) && size.equals(other.size);
      }
   }

   @Override
   public int hashCode()
   {
      int hash = 7;
      hash = 97 * hash + Objects.hashCode(this.origin);
      hash = 97 * hash + Objects.hashCode(this.size);
      return hash;
   }
}


package nh.util;

import java.awt.Point;

public class PolarPoint
{
   public final double radial;
   public final double theta;
   
   public PolarPoint(double radial, double theta)
   {
      this.radial = radial;
      this.theta = theta;
   }

   public PolarPoint(PolarPoint toCopy)
   {
      this(toCopy.radial, toCopy.theta);
   }

   public PolarPoint amplify(double magnitude)
   {
      return new PolarPoint(radial * magnitude, theta);
   }

   public PolarPoint rotate(double radians)
   {
      return new PolarPoint(radial, theta + radians);
   }

   public PolarPoint rotateClockwise(double radians)
   {
      return new PolarPoint(radial, theta - radians);
   }

   public PolarPoint rotateCounterClockwise(double radians)
   {
      return rotate(radians);
   }

   @Override
   public String toString()
   {
      return "(" + Double.toString(radial) + ", " + Double.toString(theta) + ")";
   }
   
   @Override
   public boolean equals(Object o)
   {
      if(o == this)
         return true;
      else if((o == null) || !(o instanceof PolarPoint))
         return false;
      else
      {
         PolarPoint other = (PolarPoint)o;
         return (Double.compare(this.radial, other.radial) == 0) &&
                (Double.compare(this.theta, other.theta) == 0);
      }
   }

   @Override
   public int hashCode()
   {
      int hash = 5;
      hash = 71 * hash + (int) (Double.doubleToLongBits(this.radial) ^ (Double.doubleToLongBits(this.radial) >>> 32));
      hash = 71 * hash + (int) (Double.doubleToLongBits(this.theta) ^ (Double.doubleToLongBits(this.theta) >>> 32));
      return hash;
   }
}

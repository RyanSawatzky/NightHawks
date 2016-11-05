package nh.util;

public class DoubleDimension
{
   public double width;
   public double height;
   
   public DoubleDimension()
   {
      this(0.0d, 0.0d);
   }

   public DoubleDimension(double width, double height)
   {
      this.width = width;
      this.height = height;
   }
   
   @Override
   public String toString()
   {
      return "(" + Double.toString(width) + ", " + Double.toString(height) + ")";
   }
}

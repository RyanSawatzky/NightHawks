package nh.util;

public class BackgroundInfo
{
   public final DoublePoint center;
   public final DoublePoint origin;
   public final DoublePoint extent;
   public double zoom;
   
   public BackgroundInfo()
   {
      center = new DoublePoint(0.0d, 0.0d);
      origin = new DoublePoint(0.0d, 0.0d);
      extent = new DoublePoint(0.0d, 0.0d);
      zoom = 1.0;
   }
}

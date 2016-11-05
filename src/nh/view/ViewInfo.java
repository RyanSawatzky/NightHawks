package nh.view;

public class ViewInfo
{
   public ViewPoint center;
   public ViewPoint origin;
   public ViewPoint extent;
   public double zoom;

   public ViewInfo()
   {
      center = new ViewPoint(0.0d, 0.0d);
      origin = new ViewPoint(0.0d, 0.0d);
      extent = new ViewPoint(0.0d, 0.0d);
      zoom = 1.0d;
   }
}

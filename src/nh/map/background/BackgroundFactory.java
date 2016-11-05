package nh.map.background;

public class BackgroundFactory
{
   private BackgroundFactory() {}
   
   public static Background create(BackgroundType type)
   {
      switch(type)
      {
         case Normal:
            return new NormalBackground();
         
         default:
            throw new IllegalArgumentException("Unknown background type " + type.toString());
      }
   }
}

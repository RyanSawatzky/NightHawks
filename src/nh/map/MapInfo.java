package nh.map;

import java.util.Objects;

public class MapInfo
{
   public MapPoint origin;
   public MapPoint extent;
   public MapPoint center;

   public MapInfo()
   {
      origin = new MapPoint(0.0d, 0.0d);
      extent = new MapPoint(0.0d, 0.0d);
      center = new MapPoint(0.0d, 0.0d);
   }

   @Override
   public boolean equals(Object o)
   {
      if(this == o)
         return true;
      else if((o == null) || !(o instanceof MapInfo))
         return false;
      else
      {
         MapInfo other = (MapInfo) o;

         return origin.equals(other.origin) &&
                extent.equals(other.extent) &&
                center.equals(other.center);
      }
              
   }

   @Override
   public int hashCode()
   {
      int hash = 5;
      hash = 17 * hash + Objects.hashCode(this.origin);
      hash = 17 * hash + Objects.hashCode(this.extent);
      hash = 17 * hash + Objects.hashCode(this.center);
      return hash;
   }
}

package nh.hex;

public class Orientation
{
   public static final Orientation Horizontal = new Orientation("Horizontal", true);
   public static final Orientation Vertical = new Orientation("Vertical", false);
   
   private final String name;
   private final boolean horizontal;

   private Orientation(String name, boolean horizontal)
   {
      this.name = name;
      this.horizontal = horizontal;
   }

   public boolean isHorizontal()
   {
      return horizontal;
   }
   
   public boolean isVertical()
   {
      return (horizontal == false);
   }

   @Override
   public String toString()
   {
      return name;
   }
   
   @Override
   public boolean equals(Object o)
   {
      return (this == o);
   }
}

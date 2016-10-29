package nh.hex;

public class CubeCoordinate implements HexCoordinate
{
   public final int x;
   public final int y;
   public final int z;

   public CubeCoordinate(int x, int y, int z)
   {
      this.x = x;
      this.y = y;
      this.z = z;

      if(x + y + z != 0)
         throw new IllegalArgumentException("Cube coordinates must sum to zero");
   }


   @Override
   public OffsetCoordinate toOffset(Orientation orientation)
   {
      if(orientation.isHorizontal())
      {
         // # convert cube to odd-r offset
         // col = x + (z - (z&1)) / 2
         // row = z
         int col = x + (z - (z & 1)) / 2;
         int row = z;
         
         return new OffsetCoordinate(col, row);
      }
      else
      {
         // # convert cube to odd-q offset
         // col = x
         // row = z + (x - (x&1)) / 2
         int col = x;
         int row = z + (x - (x & 1)) / 2;
         
         return new OffsetCoordinate(col, row);
      }
   }

   @Override
   public CubeCoordinate toCube(Orientation orientation)
   {
      return this;
   }

   @Override
   public AxialCoordinate toAxial(Orientation orientation)
   {
      // # convert cube to axial
      // q = x
      // r = z
      return new AxialCoordinate(x, z);
   }
   
   @Override
   public String toString()
   {
      return "(" + x + ", " + y + ", " + z + ")";
   }
}

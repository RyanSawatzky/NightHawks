package nh.hex;

public class AxialCoordinate implements HexCoordinate
{
   public final int q;
   public final int r;
   
   public AxialCoordinate(int q, int r)
   {
      this.q = q;
      this.r = r;
   }

   @Override
   public OffsetCoordinate toOffset(Orientation orientation)
   {
      if(orientation.isHorizontal())
      {
         // # convert cube to odd-r offset
         // col = x + (z - (z&1)) / 2
         // row = z
         int col = q + (r - (r & 1)) / 2;
         int row = r;
         
         return new OffsetCoordinate(col, row);
      }
      else
      {
         // # convert cube to odd-q offset
         // col = x
         // row = z + (x - (x&1)) / 2
         int col = q;
         int row = r + (q - (q & 1)) / 2;
         
         return new OffsetCoordinate(col, row);
      }
   }

   @Override
   public CubeCoordinate toCube(Orientation orientation)
   {
      // # convert axial to cube
      // x = q
      // z = r
      // y = -x-z
      return new CubeCoordinate(q, -q-r, r);
   }

   @Override
   public AxialCoordinate toAxial(Orientation orientation)
   {
      return this;
   }
}

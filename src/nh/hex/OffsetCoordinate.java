package nh.hex;

public class OffsetCoordinate implements HexCoordinate
{
   public final int col;
   public final int row;
   
   public OffsetCoordinate(int col, int row)
   {
      this.col = col;
      this.row = row;
   }

   @Override
   public OffsetCoordinate toOffset(Orientation orientation)
   {
      return this;
   }

   @Override
   public CubeCoordinate toCube(Orientation orientation)
   {
      if(orientation.isHorizontal())
      {
         // # convert odd-r offset to cube
         // x = col - (row - (row&1)) / 2
         // z = row
         // y = -x-z
         int x = col - (row - (row & 1)) / 2;
         int z = row;
         int y = -x - z;

         return new CubeCoordinate(x, y, z);
      }
      else
      {
         // # convert odd-q offset to cube
         // x = col
         // z = row - (col - (col&1)) / 2
         // y = -x-z   
         int x = col;
         int z = row - (col - (col & 1)) / 2;
         int y = -x - z;
         
         return new CubeCoordinate(x, y, z);
      }
   }

   @Override
   public AxialCoordinate toAxial(Orientation orientation)
   {
      if(orientation.isHorizontal())
      {
         // # convert odd-r offset to cube
         // x = col - (row - (row&1)) / 2
         // z = row
         // y = -x-z
         int q = col - (row - (row & 1)) / 2;
         int r = row;

         return new AxialCoordinate(q, r);
      }
      else
      {
         // # convert odd-q offset to cube
         // x = col
         // z = row - (col - (col&1)) / 2
         // y = -x-z   
         int q = col;
         int r = row - (col - (col & 1)) / 2;
         
         return new AxialCoordinate(q, r);
      }
   }
   
   @Override
   public boolean equals(Object o)
   {
      if(this == o)
         return true;
      else if((o == null) || !(o instanceof OffsetCoordinate))
         return false;
      else
      {
         OffsetCoordinate other = (OffsetCoordinate) o;
         return (col == other.col) && (row == other.row);
      }
   }

   @Override
   public int hashCode()
   {
      int hash = 7;
      hash = 53 * hash + col;
      hash = 53 * hash + row;
      return hash;
   }
   
   @Override
   public String toString()
   {
      return "(" + Integer.toString(col) + ", " + Integer.toString(row) + ")";
   }
}

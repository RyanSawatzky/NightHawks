package nh.hex;

public interface HexCoordinate
{
   public OffsetCoordinate toOffset(Orientation orientation);
   public CubeCoordinate toCube(Orientation orientation);
   public AxialCoordinate toAxial(Orientation orientation);
}

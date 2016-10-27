package nh.hex;

public interface Map
{
   public Orientation getOrientation();
   public boolean isValidHex(HexCoordinate coord);
   public boolean isBorderHex(HexCoordinate coord);
}

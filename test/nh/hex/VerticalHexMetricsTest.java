package nh.hex;

import nh.map.MapPoint;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;

public class VerticalHexMetricsTest
{
   
   public VerticalHexMetricsTest()
   {
   }

   @Test
   public void testPointToHex1()
   {
      HexMetrics metrics = new VerticalHexMetrics();
      CubeCoordinate cube = new CubeCoordinate(100, -100, 0);

      Assert.assertEquals(cube, metrics.mapPointToHex(new MapPoint(3786.02d, 2207.7854d)));
      Assert.assertEquals(cube, metrics.mapPointToHex(new MapPoint(3763.02d, 2207.035d)));
      Assert.assertEquals(cube, metrics.mapPointToHex(new MapPoint(3750.77d, 2186.285d)));
      Assert.assertEquals(cube, metrics.mapPointToHex(new MapPoint(3763.023d, 2165.535d)));
      Assert.assertEquals(cube, metrics.mapPointToHex(new MapPoint(3787.27d, 2165.535d)));
      Assert.assertEquals(cube, metrics.mapPointToHex(new MapPoint(3799.02d, 2186.535d)));
   }
   
   @Test
   public void testPointToHex2()
   {
      HexMetrics metrics = new VerticalHexMetrics();
      CubeCoordinate cube = new CubeCoordinate(99, -100, 1);

      Assert.assertEquals(cube, metrics.mapPointToHex(new MapPoint(3749.27d, 2229.78d)));
      Assert.assertEquals(cube, metrics.mapPointToHex(new MapPoint(3725.02d, 2228.28d)));
      Assert.assertEquals(cube, metrics.mapPointToHex(new MapPoint(3713.27d, 2208.28d)));
      Assert.assertEquals(cube, metrics.mapPointToHex(new MapPoint(3725.52d, 2187.53d)));
      Assert.assertEquals(cube, metrics.mapPointToHex(new MapPoint(3749.27d, 2187.78d)));
      Assert.assertEquals(cube, metrics.mapPointToHex(new MapPoint(3761.27d, 2207.53d)));
   }
}

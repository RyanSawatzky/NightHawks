/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nh.hex;

import nh.map.MapPoint;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Ryan
 */
public class HorizontalHexMetricsTest {
   
   public HorizontalHexMetricsTest() {
   }

   @Test
   public void testPointToHex1()
   {
      HexMetrics metrics = new HorizontalHexMetrics(25.0d);
      CubeCoordinate cube = new CubeCoordinate(0, -100, 100);

      Assert.assertEquals(cube, metrics.mapPointToHex(new MapPoint(2186.75d, 3799.25d)));
      Assert.assertEquals(cube, metrics.mapPointToHex(new MapPoint(2166.0d,  3786.75d)));
      Assert.assertEquals(cube, metrics.mapPointToHex(new MapPoint(2165.75d, 3763.25d)));
      Assert.assertEquals(cube, metrics.mapPointToHex(new MapPoint(2186.75d, 3751.5d)));
      Assert.assertEquals(cube, metrics.mapPointToHex(new MapPoint(2207.25d, 3763.5d)));
      Assert.assertEquals(cube, metrics.mapPointToHex(new MapPoint(2207.0d, 3786.75d)));
   }
   
   @Test
   public void testPointToHex2()
   {
      HexMetrics metrics = new HorizontalHexMetrics(25.0d);
      CubeCoordinate cube = new CubeCoordinate(1, -100, 99);

      Assert.assertEquals(cube, metrics.mapPointToHex(new MapPoint(2208.25d, 3761.75d)));
      Assert.assertEquals(cube, metrics.mapPointToHex(new MapPoint(2187.5d,  3749.0d)));
      Assert.assertEquals(cube, metrics.mapPointToHex(new MapPoint(2187.25d, 3725.5d)));
      Assert.assertEquals(cube, metrics.mapPointToHex(new MapPoint(2208.0d,  3714.0d)));
      Assert.assertEquals(cube, metrics.mapPointToHex(new MapPoint(2229.0d, 3725.5d)));
      Assert.assertEquals(cube, metrics.mapPointToHex(new MapPoint(2229.0d, 3749.0d)));
   }
}

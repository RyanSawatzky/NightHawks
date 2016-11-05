package nh.map.background;

import java.awt.Color;
import java.awt.Point;
import java.util.Random;
import nh.main.DrawInfo;
import nh.view.ViewInfo;
import nh.util.DoubleDimension;
import nh.util.BackgroundInfo;
import nh.util.Coordinates;
import nh.util.DoublePoint;
import nh.view.ViewPoint;

public class NormalBackground implements Background
{
   private final Random random;
   private final long rootSeed;
   private final double rootGridSize;
   private final BackgroundInfo info;

   public NormalBackground()
   {
      random = new Random();
      rootSeed = System.currentTimeMillis();
      rootGridSize = 100.0d;
      info = new BackgroundInfo();
   }

   @Override
   public void scroll(DoubleDimension movement)
   {
      DoublePoint oldCenter = new DoublePoint(info.center);
      info.center.x += (movement.width / (info.zoom * 16));
      info.center.y += (movement.height / (info.zoom * 16));
      System.out.println("Background scroll " + info.center.delta(oldCenter).toString());
   }
   
   @Override
   public void zoom(double zoomAdjust)
   {
      info.zoom = info.zoom + (zoomAdjust / 8);
      System.out.println("Background zoom " + info.zoom);
   }

   private static DoublePoint viewToBackground(ViewInfo viewInfo, BackgroundInfo backInfo, ViewPoint viewPoint)
   {
      return new DoublePoint(backInfo.center.x - ((viewInfo.center.x - viewPoint.x) / backInfo.zoom),
                             backInfo.center.y - ((viewInfo.center.y - viewPoint.y) / backInfo.zoom));
   }

   private static ViewPoint backgroundToView(ViewInfo viewInfo, BackgroundInfo backInfo, DoublePoint backPoint)
   {
      return new ViewPoint(viewInfo.center.x - ((backInfo.center.x - backPoint.x) * backInfo.zoom),
                           viewInfo.center.y - ((backInfo.center.y - backPoint.y) * backInfo.zoom));
   }

   @Override
   public void draw(DrawInfo d, ViewInfo viewInfo)
   {
      DoublePoint origin = viewToBackground(viewInfo, info, viewInfo.origin);
      DoublePoint extent = viewToBackground(viewInfo, info, viewInfo.extent);

      int originGridX = (int)Math.floor(origin.x / rootGridSize);
      int originGridY = (int)Math.floor(origin.y / rootGridSize);
      int extentGridX = (int)Math.floor(extent.x / rootGridSize);
      int extentGridY = (int)Math.floor(extent.y / rootGridSize);
      
      for(int y = originGridY; y <= extentGridY; y++)
      {
         for(int x = originGridX; x <= extentGridX; x++)
         {
            drawGridSquare(d, viewInfo, x, y);
         }
      }
   }
   
   public long seed(int gridX, int gridY)
   {
      long gridXl = ((long)gridX & 0x00000000FFFFFFFFl);
      long gridYl = ((long)gridY & 0x00000000FFFFFFFFl);
      gridXl = (gridXl << 32) & 0xFFFFFFFF00000000l;
      return(rootSeed ^ (gridXl | gridYl));
   }

   public void drawGridSquare(DrawInfo d,
                              ViewInfo viewInfo,
                              int gridX,
                              int gridY)
   {
      random.setSeed(seed(gridX, gridY));

      DoublePoint gridOrigin = new DoublePoint(gridX * rootGridSize,
                                               gridY * rootGridSize);

      int numberStars = (random.nextInt(20) + 1) + (random.nextInt(20) + 1);
      for(int s = 0; s < numberStars; s++)
      {
         DoublePoint starPoint = new DoublePoint((random.nextDouble() * rootGridSize) + gridOrigin.x,
                                                 (random.nextDouble() * rootGridSize) + gridOrigin.y);
         int brightness = random.nextInt(192) + 64;

         ViewPoint starInView = backgroundToView(viewInfo, info, starPoint);
         Point starInComp = Coordinates.viewToComponent(d.comp, viewInfo, starInView);

         d.g.setColor(new Color(brightness, brightness, brightness));
         d.g.drawLine(starInComp.x, starInComp.y, starInComp.x, starInComp.y);
      }
   }
}

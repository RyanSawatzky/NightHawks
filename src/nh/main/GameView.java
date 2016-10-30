package nh.main;

import nh.hex.HexMetrics;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;
import nh.hex.CubeCoordinate;
import nh.map.Map;
import nh.hex.OffsetCoordinate;
import nh.map.MapPoint;
import nh.util.DoublePoint;
import nh.util.LongDimension;

public class GameView
{
   private static final double HexSize = 25.0d;
   private static final double ZoomMinimum = 0.320d;
   private static final double ZoomMaximum = 4.0d;

   private final BufferedImage background;
   private final Map map;
   private DrawInfo d;

   // Scroll Info
   private MapPoint mapCenter;
   private MapPoint mapOrigin;
   private MapPoint mapExtent;

   // Zoom Info
   private double zoom = 1.0d;
   private HexMetrics hexMetrics;

   public GameView(Map map)
   {
      BufferedImage iBackground = null;
      try
      {
         iBackground = ImageIO.read(Map.class.getResource("backgrounds/Starfield.jpg"));
      }
      catch(IOException e) {}
      background = iBackground;
      
      this.map = map;
      mapOrigin = new MapPoint(0, 0);
      mapCenter = new MapPoint(0, 0);
      hexMetrics = HexMetrics.create(map.getOrientation(), HexSize);
   }

   public void draw(DrawInfo d)
   {
      this.d = d;

      calculate();
      setRenderingHints();
      drawBackground();
      drawHoverHex();
      drawHexes();
   }

   public void scrollView(Dimension movement)
   {
      if(mapCenter != null)
      {
         Point centerInScreen = this.mapToView(mapCenter);
         centerInScreen.x -= movement.width;
         centerInScreen.y -= movement.height;
         mapCenter = viewToMap(centerInScreen);
//         viewCenter = new MapPoint(viewCenter.x - movement.width, viewCenter.y - movement.height);
      }
   }

   public void zoomView(double zoomAdjust)
   {
//      double oldZoom = zoom;
      zoom -= (zoomAdjust / 25);
      zoom = Math.max(zoom, ZoomMinimum);
      zoom = Math.min(zoom, ZoomMaximum);
//
//      if((zoom < oldZoom) || (zoom > oldZoom))
//      {
//         viewCenter = new DoublePoint((viewCenter.x * zoom) / oldZoom,
//                                      (viewCenter.y * zoom) / oldZoom);
//      }
   }

   private void calculate()
   {
      int halfWidth = d.size.width / 2;
      int halfHeight = d.size.height / 2;

      Point viewCenter = mapToView(mapCenter);
      Point viewExtent = new Point(viewCenter);

      viewCenter.x -= halfWidth;
      viewCenter.y -= halfHeight;
      mapOrigin = viewToMap(viewCenter);

      viewExtent.x += halfWidth;
      viewExtent.y += halfHeight;
      mapExtent = viewToMap(viewExtent);
   }

   private void setRenderingHints()
   {
      d.g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      d.g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
      d.g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
      d.g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
   }

   private void drawBackground()
   {
      d.g.setColor(Color.BLACK);
      d.g.fillRect(d.location.x, d.location.y, d.size.width, d.size.height);

      DoublePoint imageOrigin = new DoublePoint(mapOrigin.x / (zoom/100.0d),
                                                mapOrigin.y / (zoom/100.0d));
//      d.g.drawImage(background, -(int)Math.round(imageOrigin.x), -(int)Math.round(imageOrigin.y), null);

//      d.g.drawImage(background, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null)
   }

   private Point mapToView(MapPoint mapPoint)
   {
      double x = (mapPoint.x - mapCenter.x) * zoom;
      double y = (mapPoint.y - mapCenter.y) * zoom;

      x += (d.size.width / 2) + d.location.x;
      y += (d.size.height / 2) + d.location.y;

      return DoublePoint.toPoint(x, y);
   }

   private MapPoint viewToMap(Point viewPoint)
   {
      double x = viewPoint.x - (d.size.width / 2.0d) - d.location.x;
      double y = viewPoint.y - (d.size.height / 2.0d) - d.location.y;

      x = (x / zoom) + mapCenter.x;
      y = (y / zoom) + mapCenter.y;

      return new MapPoint(x, y);
   }

   private void drawHoverHex()
   {
      if(d.mouseLocation != null)
      {
         d.g.setColor(Color.BLUE);

         DoublePoint mapMouseLocation = viewToMap(d.mouseLocation);
         CubeCoordinate hex = hexMetrics.mapPointToHex(mapMouseLocation);

         if(map.isValidHex(hex))
            fillHex(hexMetrics.hexPoints(hex));
      }
   }

   private void drawHexes()
   {
      d.g.setColor(Color.DARK_GRAY);

      OffsetCoordinate originHex = hexMetrics.mapPointToHex(mapOrigin).toOffset(map.getOrientation());
      OffsetCoordinate extentHex = hexMetrics.mapPointToHex(mapExtent).toOffset(map.getOrientation());
      originHex = new OffsetCoordinate(originHex.col - 1, originHex.row - 1);
      extentHex = new OffsetCoordinate(extentHex.col + 1, extentHex.row + 1);

      for(int row = originHex.row; row <= extentHex.row; row++)
      {
         boolean foundAtLeastOneValidHexInRow = false;

         for(int col = originHex.col; col <= extentHex.col; col++)
         {
            OffsetCoordinate hex = new OffsetCoordinate(col, row);
            boolean validHex = map.isValidHex(hex);

            if((validHex == false) && (foundAtLeastOneValidHexInRow == true))
               break;

            if(validHex)
            {
               foundAtLeastOneValidHexInRow = true;

               List<MapPoint> hexPoints = hexMetrics.hexPoints(hex);
               drawLine(hexPoints.get(0), hexPoints.get(1));
               drawLine(hexPoints.get(1), hexPoints.get(2));
               drawLine(hexPoints.get(2), hexPoints.get(3));
               drawLine(hexPoints.get(3), hexPoints.get(4));
               drawLine(hexPoints.get(4), hexPoints.get(5));
               drawLine(hexPoints.get(5), hexPoints.get(0));
            }
         }
      }
   }
   
   private void drawLine(MapPoint map1, MapPoint map2)
   {
      Point view1 = mapToView(map1);
      Point view2 = mapToView(map2);

      d.g.drawLine(view1.x, view1.y, view2.x, view2.y);
   }

   private void fillHex(List<MapPoint> hexPoints)
   {
      int numberPoints = hexPoints.size();
      int[] xPoints = new int[numberPoints];
      int[] yPoints = new int[numberPoints];

      for(int i = 0; i < numberPoints; i++)
      {
         MapPoint mapPoint = hexPoints.get(i);
         Point viewPoint = this.mapToView(mapPoint);
         xPoints[i] = viewPoint.x;
         yPoints[i] = viewPoint.y;
      }

      d.g.fillPolygon(xPoints, yPoints, numberPoints);
   }
}

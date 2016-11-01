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
   private final MapPoint mapOrigin;
   private final MapPoint mapExtent;
   private boolean debug;
   private DrawInfo d;

   // Scroll Info
   private MapPoint viewCenterInMap;
   private MapPoint viewOriginInMap;
   private MapPoint viewExtentInMap;
   private MapPoint minimumViewOriginInMap;
   private MapPoint maximumViewExtentInMap;
   private Point backgroundOrigin;
   private Point backgroundExtent;


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
      viewOriginInMap = new MapPoint(0, 0);
      viewCenterInMap = new MapPoint(0, 0);
      hexMetrics = HexMetrics.create(map.getOrientation(), HexSize);
      mapOrigin = map.getMapOrigin(hexMetrics);
      mapExtent = map.getMapExtent(hexMetrics);
   }

   public void setDebug(boolean value)
   {
      debug = value;
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
      if(viewCenterInMap != null)
      {
         Point centerInScreen = this.mapToView(viewCenterInMap);
         centerInScreen.x -= movement.width;
         centerInScreen.y -= movement.height;
         viewCenterInMap = viewToMap(centerInScreen);

         viewCenterInMap = new MapPoint(Math.min(mapExtent.x, Math.max(mapOrigin.x, viewCenterInMap.x)),
                                  Math.min(mapExtent.y, Math.max(mapOrigin.y, viewCenterInMap.y)));
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

      Point viewCenter = mapToView(viewCenterInMap);
      Point viewExtent = new Point(viewCenter);

      viewCenter.x -= halfWidth;
      viewCenter.y -= halfHeight;
      viewOriginInMap = viewToMap(viewCenter);

      viewExtent.x += halfWidth;
      viewExtent.y += halfHeight;
      viewExtentInMap = viewToMap(viewExtent);

      // Determine image location and size
      Point minimumViewOrigin = mapToView(ZoomMinimum, d.location, d.size, mapOrigin, mapOrigin);
      minimumViewOrigin.x -= (d.size.width / 2);
      minimumViewOrigin.y -= (d.size.height / 2);

      Point maximumViewExtent = mapToView(ZoomMinimum, d.location, d.size, mapExtent, mapExtent);
      maximumViewExtent.x += (d.size.width / 2);
      maximumViewExtent.y += (d.size.height / 2);

      minimumViewOriginInMap = viewToMap(ZoomMinimum, d.location, d.size, mapOrigin, minimumViewOrigin);
      maximumViewExtentInMap = viewToMap(ZoomMinimum, d.location, d.size, mapExtent, maximumViewExtent);

      double widthScale = background.getWidth() / (maximumViewExtentInMap.x - minimumViewOriginInMap.x);
      double heightScale = background.getHeight() / (maximumViewExtentInMap.y - minimumViewOriginInMap.y);
      double backgroundScaleAtLowestZoom = Math.min(widthScale, heightScale);

      backgroundOrigin = new Point(0,0);
      backgroundExtent = new Point((int)Math.floor(backgroundScaleAtLowestZoom * (maximumViewExtentInMap.x - minimumViewOriginInMap.x)),
                                   (int)Math.floor(backgroundScaleAtLowestZoom * (maximumViewExtentInMap.y - minimumViewOriginInMap.y)));
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

      DoublePoint viewOriginInBackground = mapToBackground(viewOriginInMap);
      DoublePoint viewExtentInBackground = mapToBackground(viewExtentInMap);
      DoublePoint minimumViewOriginInBackground = mapToBackground(minimumViewOriginInMap);
      DoublePoint maximumViewExtentInBackground = mapToBackground(maximumViewExtentInMap);
      DoublePoint viewOriginRatioOfBackground = new DoublePoint((viewOriginInBackground.x - minimumViewOriginInBackground.x) / (maximumViewExtentInBackground.x - minimumViewOriginInBackground.x),
                                                                (viewOriginInBackground.y - minimumViewOriginInBackground.y) / (maximumViewExtentInBackground.y - minimumViewOriginInBackground.y));
      DoublePoint viewExtentRatioOfBackground = new DoublePoint((viewExtentInBackground.x - minimumViewOriginInBackground.x) / (maximumViewExtentInBackground.x - minimumViewOriginInBackground.x),
                                                                (viewExtentInBackground.y - minimumViewOriginInBackground.y) / (maximumViewExtentInBackground.y - minimumViewOriginInBackground.y));
      int backgroundX1 = (int)Math.floor(backgroundExtent.x * viewOriginRatioOfBackground.x);
      int backgroundX2 = (int)Math.floor(backgroundExtent.x * viewExtentRatioOfBackground.x);
      int backgroundY1 = (int)Math.floor(backgroundExtent.y * viewOriginRatioOfBackground.y);
      int backgroundY2 = (int)Math.floor(backgroundExtent.y * viewExtentRatioOfBackground.y);

      Point backgroundOrigin = mapToView(minimumViewOriginInMap);
      Point backgroundExtent = mapToView(maximumViewExtentInMap);
//      d.g.drawImage(background,
//                    d.location.x, d.location.y, d.size.width, d.size.height,
//                    backgroundX1, backgroundY1, backgroundX2, backgroundY2,
//                    null);
   }

   private DoublePoint mapToBackground(MapPoint mapPoint)
   {
      return mapToBackground(zoom, d.location, d.size, viewCenterInMap, mapPoint);
   }

   private static DoublePoint mapToBackground(double zoom,
                                              Point viewLocation, Dimension viewSize,
                                              MapPoint viewCenterInMap, MapPoint mapPoint)
   {
      double x = (mapPoint.x - viewCenterInMap.x) * (zoom / 10);
      double y = (mapPoint.y - viewCenterInMap.y) * (zoom / 10);

      x += (viewSize.width / 2) + viewLocation.x;
      y += (viewSize.height / 2) + viewLocation.y;

      return new DoublePoint(x, y);
   }

   private Point mapToView(MapPoint mapPoint)
   {
      return mapToView(zoom, d.location, d.size, viewCenterInMap, mapPoint);
   }

   private static Point mapToView(double zoom,
                                  Point viewLocation, Dimension viewSize,
                                  MapPoint viewCenterInMap, MapPoint mapPoint)
   {
      double x = (mapPoint.x - viewCenterInMap.x) * zoom;
      double y = (mapPoint.y - viewCenterInMap.y) * zoom;

      x += ((double)viewSize.width / 2.0d) + (double)viewLocation.x;
      y += ((double)viewSize.height / 2.0d) + (double)viewLocation.y;

      return DoublePoint.toPoint(x, y);
   }

   private MapPoint viewToMap(Point viewPoint)
   {
      return viewToMap(zoom, d.location, d.size, viewCenterInMap, viewPoint);
   }

   private static MapPoint viewToMap(double zoom,
                                     Point viewLocation, Dimension viewSize,
                                     MapPoint viewCenterInMap, Point viewPoint)
   {
      double x = (double)viewPoint.x - ((double)viewSize.width / 2.0d) - (double)viewLocation.x;
      double y = (double)viewPoint.y - ((double)viewSize.height / 2.0d) - (double)viewLocation.y;

      x = (x / zoom) + viewCenterInMap.x;
      y = (y / zoom) + viewCenterInMap.y;

      return new MapPoint(x, y);
   }

   private void drawHoverHex()
   {
      if(d.mouseLocation != null)
      {
         d.g.setColor(Color.BLUE);

         MapPoint mapMouseLocation = viewToMap(d.mouseLocation);
         CubeCoordinate hex = hexMetrics.mapPointToHex(mapMouseLocation);

         if(map.isValidHex(hex))
            fillHex(hexMetrics.hexPoints(hex));
      }
   }

   private void drawHexes()
   {
      d.g.setColor(Color.DARK_GRAY);

      OffsetCoordinate originHex = hexMetrics.mapPointToHex(viewOriginInMap).toOffset(map.getOrientation());
      OffsetCoordinate extentHex = hexMetrics.mapPointToHex(viewExtentInMap).toOffset(map.getOrientation());
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

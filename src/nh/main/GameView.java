package nh.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.RenderingHints;
import java.util.List;
import nh.hex.Map;
import nh.hex.OffsetCoordinate;
import nh.util.DoublePoint;
import nh.util.LongDimension;
import nh.util.LongRectangle;

public class GameView
{
   private final Map map;
   private DrawInfo d;

   // Scroll Info
   private DoublePoint viewCenter;
   private DoublePoint viewOrigin;
   private LongDimension viewSize;

   // Zoom Info
   private int hexSize = 25;
   private HexMetrics hexMetrics;

   public GameView(Map map)
   {
      this.map = map;
      viewCenter = new DoublePoint(0, 0);
   }

   public void draw(DrawInfo d)
   {
      this.d = d;

      calculate();
      drawBackground();
      drawHexes();
      drawMouseHoverHex();
   }

   public void scrollView(Dimension movement)
   {
      if(viewCenter != null)
      {
         System.out.println("Scroll Game View " + movement.toString());
         viewCenter = new DoublePoint(viewCenter.x - movement.width, viewCenter.y - movement.height);
      }
   }

   private void calculate()
   {
      int halfWidth = d.size.width / 2;
      int halfHeight = d.size.height / 2;
      viewOrigin = new DoublePoint(viewCenter.x - halfWidth, viewCenter.y - halfHeight);
      viewSize = new LongDimension(d.size.width, d.size.height);
      hexMetrics = HexMetrics.create(map.getOrientation(), hexSize);
   }

   private void drawBackground()
   {
      d.g.setColor(Color.BLACK);
      d.g.fillRect(d.location.x, d.location.y, d.size.width, d.size.height);
   }

   private Point mapToView(DoublePoint mapPoint)
   {
      return DoublePoint.toPoint(mapPoint.x - viewOrigin.x, mapPoint.y - viewOrigin.y);
   }

   private DoublePoint viewToMap(Point viewPoint)
   {
      return new DoublePoint(viewPoint.x + viewOrigin.x, viewPoint.y + viewOrigin.y);
   }

   private void drawHexes()
   {
      d.g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      d.g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
      d.g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
      d.g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
      d.g.setColor(Color.DARK_GRAY);

      OffsetCoordinate originHex = hexMetrics.mapPointToHex(viewOrigin).toOffset(map.getOrientation());
      OffsetCoordinate extentHex = hexMetrics.mapPointToHex(viewOrigin.x + viewSize.width, viewOrigin.y + viewSize.height).toOffset(map.getOrientation());
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

               DoublePoint centerInMap = hexMetrics.hexCenter(hex);
               Point center = mapToView(centerInMap);

               List<DoublePoint> hexPoints = hexMetrics.hexPoints(hex);
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
   
   private void drawMouseHoverHex()
   {
      
   }
   
   private void drawLine(DoublePoint p1, DoublePoint p2)
   {
      drawLine(p1.x, p1.y, p2.x, p2.y);
   }

   private void drawLine(double x1, double y1, double x2, double y2)
   {
      d.g.drawLine(mapCoordToScreenX(x1),
                   MapCoordToScreenY(y1),
                   mapCoordToScreenX(x2),
                   MapCoordToScreenY(y2));
   }
   
   private int mapCoordToScreenX(double x)
   {
      return (int)Math.round(x - viewOrigin.x);
   }
   
   private int MapCoordToScreenY(double y)
   {
      return (int) Math.round(y - viewOrigin.y);
   }
}

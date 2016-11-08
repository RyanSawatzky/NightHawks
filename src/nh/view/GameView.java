package nh.view;

import nh.hex.HexMetrics;
import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import nh.hex.CubeCoordinate;
import nh.hex.HexCoordinate;
import nh.map.Map;
import nh.hex.OffsetCoordinate;
import nh.main.DrawInfo;
import nh.map.MapFacing;
import nh.map.MapInfo;
import nh.map.MapPoint;
import nh.map.background.Background;
import nh.map.background.BackgroundFactory;
import nh.map.background.BackgroundType;
import nh.map.entity.Entity;
import nh.map.entity.Frigate;
import nh.util.Coordinates;
import nh.util.DoublePolygon;

public class GameView
{
   private static final double ZoomInitial = 1.00d;
   private static final double ZoomMinimum = 0.320d;
   private static final double ZoomMaximum = 4.0d;

   private final Map map;
   private final Background background;
   private final MapPoint mapOrigin;
   private final MapPoint mapExtent;
   private final HexMetrics hexMetrics;

   // Scroll Info
   private final ViewInfo viewInfo;
   private final MapInfo mapInfo;

   // Entities
   private final Set<Entity> entities;

   // State
   private boolean debug;
   private DrawInfo d;


   public GameView(Map map)
   {
      this.map = map;
      this.background = BackgroundFactory.create(BackgroundType.Normal);
      viewInfo = new ViewInfo();
      viewInfo.zoom = ZoomInitial;
      mapInfo = new MapInfo();
      hexMetrics = HexMetrics.create(map.getOrientation());
      mapOrigin = map.getMapOrigin(hexMetrics);
      mapExtent = map.getMapExtent(hexMetrics);
      entities = new HashSet<>();

      entities.add(new Frigate().setLocation(new OffsetCoordinate(0, 1)).setFacing(MapFacing.NorthEast));
      entities.add(new Frigate().setLocation(new OffsetCoordinate(1, 0)).setFacing(MapFacing.PositiveR));
      entities.add(new Frigate().setLocation(new OffsetCoordinate(2, 0)).setFacing(MapFacing.PositiveQ));
      entities.add(new Frigate().setLocation(new OffsetCoordinate(2, 1)).setFacing(MapFacing.SouthWest));
      entities.add(new Frigate().setLocation(new OffsetCoordinate(2, 2)).setFacing(MapFacing.NegativeR));
      entities.add(new Frigate().setLocation(new OffsetCoordinate(1, 2)).setFacing(MapFacing.NegativeQ));
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
      drawEntities();
      drawHexes();
   }

   public void scrollView(Component comp, Point old, Point current)
   {
      ViewPoint viewOld = Coordinates.componentToView(comp, viewInfo, old);
      ViewPoint viewCurrent = Coordinates.componentToView(comp, viewInfo, current);

      MapPoint mapOld = Coordinates.viewToMap(viewInfo, mapInfo, viewOld);
      MapPoint mapCurrent = Coordinates.viewToMap(viewInfo, mapInfo, viewCurrent);

      ViewPoint viewOrigin = Coordinates.mapToView(viewInfo, mapInfo, mapOrigin);
      ViewPoint viewExtent = Coordinates.mapToView(viewInfo, mapInfo, mapExtent);

      ViewPoint viewOldCenter = new ViewPoint(viewInfo.center);
      viewInfo.center.x -= viewCurrent.x - viewOld.x;
      viewInfo.center.y -= viewCurrent.y - viewOld.y;
      
      mapInfo.center.x -= mapCurrent.x - mapOld.x;
      mapInfo.center.y -= mapCurrent.y - mapOld.y;

      mapInfo.center.x = Math.min(mapExtent.x, Math.max(mapOrigin.x, mapInfo.center.x));
      mapInfo.center.y = Math.min(mapExtent.y, Math.max(mapOrigin.y, mapInfo.center.y));
      
      viewInfo.center.x = Math.min(viewExtent.x, Math.max(viewOrigin.x, viewInfo.center.x));
      viewInfo.center.y = Math.min(viewExtent.y, Math.max(viewOrigin.y, viewInfo.center.y));

      background.scroll(viewOldCenter.delta(viewInfo.center));
   }

   public void zoomView(double zoomAdjust)
   {
      double oldZoom = viewInfo.zoom;
      viewInfo.zoom = Math.min(ZoomMaximum, Math.max(ZoomMinimum, viewInfo.zoom * (1.0d - (zoomAdjust / 30.0d))));
      background.zoom(viewInfo.zoom - oldZoom);
   }

   private void calculate()
   {
      viewInfo.origin.x = viewInfo.center.x - (d.size.width / 2.0d);
      viewInfo.origin.y = viewInfo.center.y - (d.size.height / 2.0d);

      viewInfo.extent.x = viewInfo.center.x + (d.size.width / 2.0d);
      viewInfo.extent.y = viewInfo.center.y + (d.size.height / 2.0d);

      mapInfo.origin = Coordinates.viewToMap(viewInfo, mapInfo, viewInfo.origin);
      mapInfo.extent = Coordinates.viewToMap(viewInfo, mapInfo, viewInfo.extent);
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
      
      background.draw(d, viewInfo);
   }

   private final Color HoverColor = new Color(40, 110, 240, 192);
   private void drawHoverHex()
   {
      if(d.mouseLocationInComponent != null)
      {
         d.g.setColor(HoverColor);

         MapPoint mapMouseLocation = Coordinates.viewToMap(viewInfo, mapInfo, Coordinates.componentToView(d.comp, viewInfo, d.mouseLocationInComponent));
         CubeCoordinate hex = hexMetrics.mapPointToHex(mapMouseLocation);

         if(map.isValidHex(hex))
            fillHex(hex, HoverColor);
      }
   }

   private Color getHexColor()
   {
      if(viewInfo.zoom >= 1.0d)
         return new Color(64, 64, 64, 255);
      else
      {
         double opacity = ((viewInfo.zoom - ZoomMinimum) * (1.0d / (1.0d - ZoomMinimum)) * 128) + 127;
         return new Color(64, 64, 64, (int)Math.round(opacity));
      }
   }

   private void drawHexes()
   {
      d.g.setColor(getHexColor());

      OffsetCoordinate originHex = hexMetrics.mapPointToHex(Coordinates.viewToMap(viewInfo, mapInfo, viewInfo.origin)).toOffset(map.getOrientation());
      OffsetCoordinate extentHex = hexMetrics.mapPointToHex(Coordinates.viewToMap(viewInfo, mapInfo, viewInfo.extent)).toOffset(map.getOrientation());
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

   private final Color EntityHexColor = new Color(40, 110, 240, 64);
   private void drawEntities()
   {
      for(Entity entity : entities)
      {
         MapPoint mapPoint = hexMetrics.hexCenter(entity.getLocation());
         DoublePolygon polygon = entity.getPolygon(mapPoint, hexMetrics.facingInRadians(entity.getFacing()));

         fillHex(entity.getLocation(), EntityHexColor);
         fillPolygon(polygon);
      }
   }

   private void drawLine(MapPoint map1, MapPoint map2)
   {
      ViewPoint view1 = Coordinates.mapToView(viewInfo, mapInfo, map1);
      ViewPoint view2 = Coordinates.mapToView(viewInfo, mapInfo, map2);

      Point point1 = Coordinates.viewToComponent(d.comp, viewInfo, view1);
      Point point2 = Coordinates.viewToComponent(d.comp, viewInfo, view2);

      d.g.drawLine(point1.x, point1.y, point2.x, point2.y);
   }

   private void fillHex(HexCoordinate hex, Color color)
   {
      List<MapPoint> hexPoints = hexMetrics.hexPoints(hex);
      int numberPoints = hexPoints.size();
      int[] xPoints = new int[numberPoints];
      int[] yPoints = new int[numberPoints];

      for(int i = 0; i < numberPoints; i++)
      {
         MapPoint mapPoint = hexPoints.get(i);
         Point viewPoint = Coordinates.viewToComponent(d.comp, viewInfo, Coordinates.mapToView(viewInfo, mapInfo, mapPoint));
         xPoints[i] = viewPoint.x;
         yPoints[i] = viewPoint.y;
      }

      d.g.setColor(color);
      d.g.fillPolygon(xPoints, yPoints, numberPoints);
   }
   
   private void fillPolygon(DoublePolygon doublePolygon)
   {
      Polygon polygon = new Polygon();
      for(MapPoint mapPoint : doublePolygon.getPoints())
      {
         ViewPoint viewPoint = Coordinates.mapToView(viewInfo, mapInfo, mapPoint);
         Point point = Coordinates.viewToComponent(d.comp, viewInfo, viewPoint);
         polygon.addPoint(point.x, point.y);
      }
      d.g.setColor(Color.white);
      d.g.fillPolygon(polygon);
   }
}

package nh.util;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import nh.main.DrawInfo;
import nh.map.MapInfo;
import nh.map.MapPoint;
import nh.view.ViewInfo;
import nh.view.ViewPoint;

public class Coordinates
{
   private Coordinates() {}

   public static Point viewToComponent(Component comp, ViewInfo viewInfo, ViewPoint viewPoint)
   {
      Point location = comp.getLocation();
      Dimension size = comp.getSize();

      double x = (viewPoint.x - viewInfo.center.x) + (location.x + (size.width / 2.0d));
      double y = (viewPoint.y - viewInfo.center.y) + (location.y + (size.height / 2.0d));
      
      return new Point((int)Math.round(x), (int)Math.round(y));
   }

   public static ViewPoint componentToView(Component comp, ViewInfo viewInfo, Point point)
   {
      Point location = comp.getLocation();
      Dimension size = comp.getSize();

      double x = point.x;
      double y = point.y;

      return new ViewPoint(viewInfo.center.x + x - (location.x + (size.width / 2.0d)),
                           viewInfo.center.y + y - (location.y + (size.height / 2.0d)));
   }

   public static ViewPoint mapToView(ViewInfo viewInfo, MapInfo mapInfo, MapPoint mapPoint)
   {
      return new ViewPoint(viewInfo.center.x - ((mapInfo.center.x - mapPoint.x) * viewInfo.zoom),
                           viewInfo.center.y - ((mapInfo.center.y - mapPoint.y) * viewInfo.zoom));
   }

   public static MapPoint viewToMap(ViewInfo viewInfo, MapInfo mapInfo, ViewPoint viewPoint)
   {
      return new MapPoint(mapInfo.center.x - ((viewInfo.center.x - viewPoint.x) / viewInfo.zoom),
                          mapInfo.center.y - ((viewInfo.center.y - viewPoint.y) / viewInfo.zoom));
   }
}

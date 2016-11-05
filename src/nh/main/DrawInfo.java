package nh.main;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;

public class DrawInfo
{
   public final Graphics2D g;
   public final Component comp;
   public final Point location;
   public final Dimension size;
   public final Point mouseLocationInComponent;

   public DrawInfo(Graphics2D g, Component comp, Point mouseLocationInComponent)
   {
      this.g = g;
      this.comp = comp;
      this.location = comp.getLocation();
      this.size = comp.getSize();
      this.mouseLocationInComponent = mouseLocationInComponent;
   }
}

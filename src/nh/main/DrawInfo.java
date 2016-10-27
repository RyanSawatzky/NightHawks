package nh.main;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;

public class DrawInfo
{
   public final Graphics2D g;
   public final Point location;
   public final Dimension size;
   public final Point mouseLocation;
   
   public DrawInfo(Graphics2D g, Component component, Point mouseLocation)
   {
      this.g = g;
      this.location = component.getLocation();
      this.size = component.getSize();
      this.mouseLocation = mouseLocation;
   }
}

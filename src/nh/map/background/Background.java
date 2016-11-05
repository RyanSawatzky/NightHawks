package nh.map.background;

import nh.main.DrawInfo;
import nh.util.DoubleDimension;
import nh.view.ViewInfo;

public interface Background
{
   public void scroll(DoubleDimension movement);
   public void zoom(double zoomAdjust);
   public void draw(DrawInfo d, ViewInfo viewInfo);
}

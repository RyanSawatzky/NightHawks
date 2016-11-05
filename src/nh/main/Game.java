package nh.main;

import nh.view.GameView;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.swing.JPanel;
import nh.hex.Orientation;
import nh.map.HexMap;

public class Game
         extends JPanel
{
   private final ConcurrentLinkedQueue<ScrollEvent> inputQueue;
   private final GameView gameView;
   
   private Point mouseLocation;

   public Game()
   {
      super.setDoubleBuffered(true);
      
      inputQueue = new ConcurrentLinkedQueue<>();
      gameView = new GameView(new HexMap(Orientation.Horizontal, 40));
      mouseLocation = null;

      GameMouseListener gameMouseListener = new GameMouseListener();
      addMouseListener(gameMouseListener);
      addMouseMotionListener(gameMouseListener);
      addMouseWheelListener(gameMouseListener);
   }

   public void loop()
   {
      processInput();
      repaint();
   }

   private void processInput()
   {
      ScrollEvent event;
      while((event = inputQueue.poll()) != null)
      {
         if(event instanceof ScrollMovementEvent)
         {
            ScrollMovementEvent sme = (ScrollMovementEvent)event;
            gameView.scrollView(this, sme.old, sme.current);
         }
         else if(event instanceof ScrollZoomEvent)
            gameView.zoomView(((ScrollZoomEvent) event).getZoomAdjustment());
         else if(event instanceof DebugEvent)
            gameView.setDebug(((DebugEvent) event).value);
      }
   }

   @Override
   public void paint(Graphics g)
   {
      super.paint(g);

      Graphics2D g2d = (Graphics2D)g;
      Color oldColor = g2d.getColor();
      Font oldFont = g2d.getFont();
      RenderingHints oldRenderingHints = g2d.getRenderingHints();

      gameView.draw(new DrawInfo(g2d, this, mouseLocation));
      
      g2d.setColor(oldColor);
      g2d.setFont(oldFont);
      g2d.setRenderingHints(oldRenderingHints);
   }

   private class GameMouseListener extends MouseAdapter
   {
      private boolean dragButtonDown = false;

      @Override
      public void mouseExited(MouseEvent e)
      {
         dragButtonDown = false;
         mouseLocation = null;
      }

      @Override
      public void mouseEntered(MouseEvent e)
      {
         dragButtonDown = false;
         mouseLocation = e.getPoint();
      }

      @Override
      public void mousePressed(MouseEvent e)
      {
         if(e.getButton() == MouseEvent.BUTTON1)
         {
            dragButtonDown = true;
            mouseLocation = e.getPoint();
            inputQueue.add(new DebugEvent(true));
         }
      }

      @Override
      public void mouseReleased(MouseEvent e)
      {
         if(e.getButton() == MouseEvent.BUTTON1)
         {
            dragButtonDown = false;
            mouseLocation = e.getPoint();
            inputQueue.add(new DebugEvent(false));
         }
      }
      
      @Override
      public void mouseMoved(MouseEvent e)
      {
         mouseLocation = e.getPoint();
      }

      @Override
      public void mouseDragged(MouseEvent e)
      {
         Point newLocation = e.getPoint();
         if(mouseLocation != null)
            inputQueue.add(new ScrollMovementEvent(mouseLocation, newLocation));
         mouseLocation = newLocation;
      }

      @Override
      public void mouseWheelMoved(MouseWheelEvent e)
      {
         inputQueue.add(new ScrollZoomEvent(e.getPreciseWheelRotation()));
         mouseLocation = e.getPoint();
      }
      
      private Dimension calculateDelta(Point oldPoint, Point newPoint)
      {
         return new Dimension(newPoint.x - oldPoint.x, newPoint.y - oldPoint.y);
      }
   }
   
   private static class ScrollEvent
   {
   }
   
   private static class DebugEvent extends ScrollEvent
   {
      private final boolean value;
      
      public DebugEvent(boolean value)
      {
         this.value = value;
      }
      
      public boolean getValue()
      {
         return value;
      }
   }
   
   private static class ScrollMovementEvent extends ScrollEvent
   {
      public final Point old;
      public final Point current;
      
      public ScrollMovementEvent(Point old, Point current)
      {
         this.old = old;
         this.current = current;
      }
   }
   
   private static class ScrollZoomEvent extends ScrollEvent
   {
      private final double zoom;
      
      public ScrollZoomEvent(double zoom)
      {
         this.zoom = zoom;
      }
      
      public double getZoomAdjustment()
      {
         return zoom;
      }
   }
}

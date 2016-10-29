package nh.main;

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
import nh.map.SquareMap;

public class Game
         extends JPanel
{
   private final ConcurrentLinkedQueue<ScrollEvent> inputQueue;
   private final GameView gameView;
   
   private Point mouseLocation;

   public Game()
   {
      inputQueue = new ConcurrentLinkedQueue<>();
      gameView = new GameView(new SquareMap(Orientation.Vertical, 20, 10));
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
         gameView.scrollView(event.getMovement());
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
         }
      }

      @Override
      public void mouseReleased(MouseEvent e)
      {
         if(e.getButton() == MouseEvent.BUTTON1)
         {
            dragButtonDown = false;
            mouseLocation = e.getPoint();
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
         {
            inputQueue.add(new ScrollEvent(calculateDelta(mouseLocation, newLocation)));
         }
         mouseLocation = newLocation;
      }

      @Override
      public void mouseWheelMoved(MouseWheelEvent e)
      {
         inputQueue.add(null);
         mouseLocation = e.getPoint();
      }
      
      private Dimension calculateDelta(Point oldPoint, Point newPoint)
      {
         return new Dimension(newPoint.x - oldPoint.x, newPoint.y - oldPoint.y);
      }
   }
   
   private static class ScrollEvent
   {
      private final Dimension movement;
      
      public ScrollEvent(Dimension movement)
      {
         this.movement = new Dimension(movement);
      }
      
      public Dimension getMovement()
      {
         return movement;
      }
   }
}

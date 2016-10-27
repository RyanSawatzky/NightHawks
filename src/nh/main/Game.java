package nh.main;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import nh.hex.HexMap;
import nh.hex.Orientation;

public class Game
         extends JPanel
         implements MouseListener
{
   private final ConcurrentLinkedQueue<AWTEvent> inputQueue;
   private final GameView gameView;
   
   private Point mouseLocation;

   public Game()
   {
      inputQueue = new ConcurrentLinkedQueue<>();
      gameView = new GameView(new HexMap(Orientation.Horizontal, 10));
      mouseLocation = null;
   }

   public void loop()
   {
      processInput();
      repaint();
   }

   private void processInput()
   {
      AWTEvent event;
      while((event = inputQueue.poll()) != null)
      {
         if(event instanceof MouseEvent)
         {
            Point location = ((MouseEvent)event).getLocationOnScreen();
            SwingUtilities.convertPointFromScreen(location, this);
            mouseLocation = location;
         }
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

   // Mouse Listener Interfaces
   @Override
   public void mouseClicked(MouseEvent e)
   {
      inputQueue.add(e);
   }

   @Override
   public void mousePressed(MouseEvent e)
   {
      inputQueue.add(e);
   }

   @Override
   public void mouseReleased(MouseEvent e)
   {
      inputQueue.add(e);
   }

   @Override
   public void mouseEntered(MouseEvent e)
   {
      inputQueue.add(e);
   }

   @Override
   public void mouseExited(MouseEvent e)
   {
      inputQueue.add(e);
   }
}

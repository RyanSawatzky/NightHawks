package nh.main;

import java.awt.Dimension;
import java.awt.Point;
import javax.swing.JFrame;

/**
 *
 * @author Ryan
 */
public class MainFrame extends JFrame
{
   public static void main(String[] args)
   {
      MainFrame mainFrame = new MainFrame();
      Game game = new Game();
      mainFrame.add(game);
      mainFrame.setLocation(new Point(0,0));
      mainFrame.setSize(new Dimension(400,400));
      mainFrame.setExtendedState(mainFrame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
      mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      mainFrame.setVisible(true);

      boolean exit = false;
      long loopStart = 0L;
      long loopEnd = 0L;
      long loopTime = 10L;
      double fps = 0L;
      while(exit == false)
      {
         loopStart = System.currentTimeMillis();
         game.loop();
         loopEnd = System.currentTimeMillis();

         if((loopEnd - loopStart) < loopTime)
         {
            try
            {
//               System.out.println("Sleep " + (loopTime - (loopEnd - loopStart)));
               Thread.sleep(loopTime - (loopEnd - loopStart));
            }
            catch(InterruptedException e)
            {
               exit = true;
            }
         }
         fps = 1000.0d / (System.currentTimeMillis() - loopStart);
//         System.out.println("FPS " + fps);
      }
   }
   
   private MainFrame()
   {
      super("Star Frontiers: Night Hawks");
   }
}

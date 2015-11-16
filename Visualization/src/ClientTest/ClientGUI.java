package ClientTest;

import starvationevasion.simvis.controller.Visualizer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Tess Daughton on 11/15/15.
 * Class for testing purposes - simulate what a client's GUI might be like and ensure that our application
 * is function properly within their GUI
 */
public class ClientGUI extends JFrame implements ActionListener
{
  Timer gameTime;
  boolean beginningOfTurn = true;
  boolean full = false; //will be implemented to switch between full and small screen
  boolean clientToggle = false;
  Visualizer visualizer;
  public ClientGUI()
  {
    super();
    gameTime = new Timer(0, this);
    visualizer=new Visualizer(gameTime);
    add(visualizer);
  }
  @Override
  public void actionPerformed(ActionEvent e)
  {
    if(beginningOfTurn || clientToggle)
    {
      if(full) visualizer.updateFull();
      else visualizer.updateMini();
    }
  }
}

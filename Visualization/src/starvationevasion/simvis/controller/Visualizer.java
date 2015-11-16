package starvationevasion.simvis.controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Tess Daughton on 11/15/15.
 * Only class instantiated by Client
 * Handles data distribution to Visuals and Model
 */
public class Visualizer extends JPanel implements ActionListener
{
  public final Timer GAME_TIME;

  public Visualizer(Timer gameTime)
  {
    GAME_TIME=gameTime;
    GAME_TIME.addActionListener(this);
  }
  public void updateFull(){}
  public void updateMini(){}

  @Override
  public void actionPerformed(ActionEvent e)
  {
    //repaint GUI
  }
}

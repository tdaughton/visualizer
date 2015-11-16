package starvationevasion.simvis.visuals;

import org.lwjgl.glfw.GLFWCursorPosCallback;

/**
 * Created by L301126 on 11/15/15.
 */
public class MouseHandler extends GLFWCursorPosCallback //GLFWMouseButtonCallback
{

  @Override
  public void invoke(long l, double v, double v1)
  {
    System.out.println("X " + v + "Y : " + v1);
  }
}

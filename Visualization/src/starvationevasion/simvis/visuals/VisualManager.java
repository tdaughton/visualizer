package starvationevasion.simvis.visuals;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.nio.ByteBuffer;

import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWvidmode;
import org.lwjgl.opengl.*;

/**
 * Created by Tess Daughton on 11/15/15.
 * This class is kind of a patchwork piece at the moment.
 * Experimenting with LWJGL, trying to learn.
 * Disregard, unless you would also like to experiment with LWJGL!
 */
public class VisualManager implements Runnable
{
  private GLFWErrorCallback errorCallback;
  public long window;
  private boolean running;
  private GLFWCursorPosCallback mouseCallback;


  private void init()
  {
    this.running = true;

    errorCallback = Callbacks.errorCallbackPrint(System.err);
    glfwSetErrorCallback(errorCallback);

    if (glfwInit() != GL11.GL_TRUE)
    {
      System.err.println("GLFW Initializaion failed");
    }
    glfwDefaultWindowHints();
    glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
    glfwWindowHint(GLFW_VISIBLE, GL_FALSE);

    window = glfwCreateWindow(800, 600, "Endless Runner", NULL, NULL);
    if (window == NULL)
    {
      System.err.println("Window could not be created");

    }
    glfwSetCursorPosCallback(window, mouseCallback = new MouseHandler());
    ByteBuffer vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
    glfwSetWindowPos(window, (GLFWvidmode.width(vidmode) - 300) / 2, (GLFWvidmode.height(vidmode) - 300) / 2);
    glfwMakeContextCurrent(window);
    glfwSwapInterval(1);
    glfwShowWindow(window);
    GL.createCapabilities();
    glClearColor(1.0f, 0.0f, 0.0f, 0.0f);
    glEnable(GL_DEPTH_TEST);
  }


  private void update()
  {
    glfwPollEvents();
  }


  private void render()
  {
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    glfwSwapBuffers(window);
  }


  public void run()
  {
    init();
    //this is being used to limit FPS to 60
    long previousTime = System.nanoTime();
    double delta = 0.0;
    double ns = 1000000000.0 / 60.0;
    long timer = System.currentTimeMillis();
    int updates = 0;
    int frames = 0;
    while (running)
    {
      long currentTime = System.nanoTime();
      delta += (currentTime - previousTime) / ns;
      previousTime = currentTime;
      if (delta >= 1.0)
      {
        update();
        updates++;
        delta--;
      }
      render();
      frames++;
      if (System.currentTimeMillis() - timer > 1000)
      {
        timer += 1000;
        System.out.println(updates + "ups, " + frames + " fps");
        updates = 0;
        frames = 0;
      }
      if (glfwWindowShouldClose(window) == GL_TRUE)
      {
        running = false;
      }
    }
  }

  public static void main(String[] args)
  {
    new VisualManager().run();
  }
}
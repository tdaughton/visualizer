import java.nio.ByteBuffer;

import starvationevasion.simvis.visuals.ResourceLoader;
import org.lwjgl.Sys;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;
/**
 * Created by Tess Daughton on November 14, 2015
 * Test code to see if you have added LWJGL module correctly.
 * Should run and pop up a little red JPanel with the title "Hello World"
 * If anyone can't get this to run, please contact me ASAP so we can get it working.
 */


public class TestLWJGL
{

  // We need to strongly reference callback instances.
  private GLFWErrorCallback errorCallback = Callbacks.errorCallbackPrint(System.err);
  private GLFWKeyCallback keyCallback;
  private ByteBuffer buf;
  private ResourceLoader resourceLoader = new ResourceLoader();

  // The window handle
  private long window;

  public void run()
  {
    System.out.println("Hello LWJGL " + Sys.getVersion() + "!");

    try
    {
      init();
      loop();

      // Release window and window callbacks
      glfwDestroyWindow(window);
      keyCallback.release();
    } finally
    {
      // Terminate GLFW and release the GLFWErrorCallback
      glfwTerminate();
      errorCallback.release();
    }
  }

  private void init()
  {
    // Setup an error callback. The default implementation
    // will print the error message in System.err.
    glfwSetErrorCallback(errorCallback);
    // Initialize GLFW. Most GLFW functions will not work before doing this.
    if (glfwInit() != GL11.GL_TRUE)
      throw new IllegalStateException("Unable to initialize GLFW");

    // Configure our window
    glfwDefaultWindowHints(); // optional, the current window hints are already the default
    glfwWindowHint(GLFW_VISIBLE, GL_FALSE); // the window will stay hidden after creation
    glfwWindowHint(GLFW_RESIZABLE, GL_TRUE); // the window will be resizable

    int WIDTH = 300;
    int HEIGHT = 300;

    // Create the window
    window = glfwCreateWindow(WIDTH, HEIGHT, "Hello World!", NULL, NULL);
    if (window == NULL)
      throw new RuntimeException("Failed to create the GLFW window");

    // Setup a key callback. It will be called every time a key is pressed, repeated or released.
    glfwSetKeyCallback(window, keyCallback = new GLFWKeyCallback()
    {
      @Override
      public void invoke(long window, int key, int scancode, int action, int mods)
      {
        if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
          glfwSetWindowShouldClose(window, GL_TRUE); // We will detect this in our rendering loop
      }
    });


    ByteBuffer vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
    glfwSetWindowPos(window, (GLFWvidmode.width(vidMode) - WIDTH) / 2, (GLFWvidmode.height(vidMode) - HEIGHT) / 2);

    // Make the OpenGL context current
    glfwMakeContextCurrent(window);
    // Enable v-sync
    glfwSwapInterval(1);

    // Make the window visible
    glfwShowWindow(window);
  }

  private void loop()
  {
    // This line is critical for LWJGL's interoperation with GLFW's
    // OpenGL context, or any context that is managed externally.
    // LWJGL detects the context that is current in the current thread,
    // creates the GLCapabilities instance and makes the OpenGL
    // bindings available for use.

    buf = resourceLoader.imageToByteBuffer("Visualization/resources/visualize_resources/universe.png");

    GL.createCapabilities();
    glEnable(GL_TEXTURE_2D);
    int id = glGenTextures();
    glBindTexture(GL_TEXTURE_2D, id);
    // Set the clear color
   // glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);
    // Run the rendering loop until the user has attempted to close
    // the window or has pressed the ESCAPE key.
    while (glfwWindowShouldClose(window) == GL_FALSE)
    {
      glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
      glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, 1000, 1000, 0, GL_RGBA, GL_UNSIGNED_BYTE, buf);

      glfwSwapBuffers(window); // swap the color buffers

      // Poll for window events. The key callback above will only be
      // invoked during this call.
      glfwPollEvents();
    }
  }

  public static void main(String[] args)
  {
    new TestLWJGL().run();
  }
}


package starvationevasion.simvis.visuals;


import java.awt.*;
import java.io.*;

/**
 * Created by Tess Daughton 11/14/15.
 * This class will be used to load/parse any resources necessary for the Visualization rendering.
 */


public class ResourceLoader
{
  private final Image STAR;
  private final Image SPEC_MAP;
  private final Image NORM_MAP;
  private final Image DIFF_MAP;

  public ResourceLoader()
  {
    STAR = loadLargeImage("");
    SPEC_MAP = loadLargeImage("");
    NORM_MAP = loadLargeImage("");
    DIFF_MAP = loadLargeImage("");
  }

  public static Image loadLargeImage(String classPath)
  {
    try
    {
      DataInputStream dataInputStream = new DataInputStream(ResourceLoader.class.getResourceAsStream(classPath));
      byte STREAM[] = new byte[dataInputStream.available()];
      dataInputStream.readFully(STREAM);
      dataInputStream.close();
      return Toolkit.getDefaultToolkit().createImage(STREAM);

    } catch (IOException e)
    {
      e.printStackTrace();
    }
    return null;
  }
}

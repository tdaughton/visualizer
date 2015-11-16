package starvationevasion.simvis.visuals;

import de.matthiasmann.twl.utils.PNGDecoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * Created by Tess Daughton 11/14/15.
 * This class will be used to load/parse any resources necessary for the Visualization rendering.
 */

public class ResourceLoader
{
  private static InputStream input;
  private static PNGDecoder imageDecoder;
  private static ByteBuffer imageBytes;

  /**
   * Converts a URL
   *
   * @param imagePath
   * @return
   * @throws IOException
   */
  public ByteBuffer imageToByteBuffer(String imagePath)
  {
    try
    {
     // input = this.getClass().getResourceAsStream(imagePath);
      input = new FileInputStream((new File(imagePath)));
      imageDecoder = new PNGDecoder(input);
      imageBytes = ByteBuffer.allocateDirect(4 * imageDecoder.getWidth()*imageDecoder.getHeight());
      imageDecoder.decode(imageBytes, imageDecoder.getWidth() * 4, PNGDecoder.Format.RGBA);
      imageBytes.flip();
      input.close();
    } catch (IOException e)
    {
      e.printStackTrace();
    }
    return imageBytes;
  }
}

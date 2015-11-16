package starvationevasion.simvis.model;

import java.awt.*;

/**
 * Created by Tess Daughton on 11/15/15.
 * It might be good to extend this class. i.e. coordinates for the borders of countries versus coordinates of event sources
 */
public class Coordinate
{
  final double LATITUTE;
  final double LONGITUDE;

  public Coordinate(int x, int y)
  {
    LATITUTE = x;
    LONGITUDE = y;
  }
}

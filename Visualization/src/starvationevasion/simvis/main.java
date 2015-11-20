package starvationevasion.simvis;

import starvationevasion.simvis.visuals.EarthViewer;


/**
 * Created by Brett on 11/14/2015.
 * Edits Tess on November 14
 * Trying to make it so that we can embed our lwjgl app inside Swing component
 * Not there yet as you can see!
 */
public class main {
  public static void main(String[] args) {
    EarthViewer.launch(EarthViewer.class, args);
  }
}

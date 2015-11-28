package starvationevasion.simvis.visuals;

import javafx.animation.*;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;
import starvationevasion.simvis.model.Coordinate;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Stack;


/**
 * http://stackoverflow.com/questions/19621423/javafx-materials-bump-and-spec-maps
 * Original Author:jewelsea,http://stackoverflow.com/users/1155209/jewelsea
 * Modified by:Tess Daughton
 **/


public class EarthViewer extends Application //implements EventHandler
{

  private static final double EARTH_RADIUS = 400;
  private static final double VIEWPORT_SIZE = 800;
  private static final double ROTATE_SECS = 30;

  private static final double MAP_WIDTH = 8192 / 2d;
  private static final double MAP_HEIGHT = 4092 / 2d;
  private static final double SCALE_DELTA = 1.1;
  private double zoomPosition = 0;
  private final DoubleProperty angleX = new SimpleDoubleProperty(0);
  private final DoubleProperty angleY = new SimpleDoubleProperty(0);
  double anchorX, anchorY;
  private double anchorAngleX = 0;
  private double anchorAngleY = 0;

  private static final String DIFFUSE_MAP = "DIFFUSE_MAP.jpg";
  //"http://www.daidegasforum.com/images/22/world-map-satellite-day-nasa-earth.jpg";
  private static final String NORMAL_MAP = "NORMAL_MAP.jpg";
  //"http://planetmaker.wthr.us/img/earth_normalmap_flat_8192x4096.jpg";
  private static final String SPECULAR_MAP = "SPEC_MAP.jpg";
  //"http://planetmaker.wthr.us/img/earth_specularmap_flat_8192x4096.jpg";

  private final PerspectiveCamera CAMERA = new PerspectiveCamera();
  private static Scene scene;
  private static Group group;
  private static StackPane rootStack = new StackPane();

  private Group buildScene() {
    Sphere earth = new Sphere(EARTH_RADIUS);

    /* Material */
    PhongMaterial earthMaterial = new PhongMaterial();
    earthMaterial.setDiffuseMap
        (new Image(getClass().getResourceAsStream(DIFFUSE_MAP), MAP_WIDTH, MAP_HEIGHT, true, true));
    earthMaterial.setBumpMap
        (new Image(getClass().getResourceAsStream(NORMAL_MAP), MAP_WIDTH, MAP_HEIGHT, true, true));
    earthMaterial.setSpecularMap
        (new Image(getClass().getResourceAsStream(SPECULAR_MAP), MAP_WIDTH, MAP_HEIGHT, true, true));

    earth.setMaterial(earthMaterial);

    /* Lat Long handler */
    EventHandler<javafx.scene.input.MouseEvent> handler = event -> {
      PickResult pickResult = event.getPickResult();
      Point3D point = pickResult.getIntersectedPoint();

      double x = point.getX();
      double y = point.getY();
      double z = point.getZ();
      double lat = Math.toDegrees(Math.acos(y / EARTH_RADIUS) - Math.PI/2); //theta
      double lon = Math.toDegrees(Math.atan(x/z)); //phi
      if (z > 0) lon += (180 * Math.signum(-lon));
      Coordinate c = new Coordinate(lon, lat);
//      System.out.println(lon + " " + lat + " " + point);
    };
    earth.setOnMouseClicked(handler);
    return new Group(earth);
  }

  @Override
  public void start(Stage stage) {
    /* Init group */
    group = buildScene();
    Rotate groupXRotate, groupYRotate;
    group.getTransforms().setAll(
        groupXRotate = new Rotate(0, Rotate.X_AXIS),
        groupYRotate = new Rotate(0, Rotate.Y_AXIS)
    );
    groupXRotate.angleProperty().bind(angleX);
    groupYRotate.angleProperty().bind(angleY);

      /* init scene */
    scene = new Scene(new StackPane(group), VIEWPORT_SIZE, VIEWPORT_SIZE, false, SceneAntialiasing.BALANCED);
    scene.setOnMouseDragged((MouseEvent event) -> {
      angleX.set(anchorAngleX - (anchorY - event.getSceneY()));
      angleY.set(anchorAngleY + anchorX - event.getSceneX());
    });
    scene.setOnMousePressed((MouseEvent event) -> {
      anchorX = event.getSceneX();
      anchorY = event.getSceneY();
      anchorAngleX = angleX.get();
      anchorAngleY = angleY.get();
    });
    scene.setOnScroll(event ->
    {
      System.out.println("test)");
    });
    scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
      @Override
      public void handle(KeyEvent event) {
        switch (event.getCode()) {
          case P:
            rotateAroundYAxis(group).play();
        }
      }
    });

    stage.setScene(scene);
    scene.getStylesheets().add(EarthViewer.class.getResource("style.css").toExternalForm());
    scene.setCamera(CAMERA);

    /**setTranslate can be used to zoom in and out on the world*/
    scene.setOnScroll(new EventHandler<ScrollEvent>() {
      @Override
      public void handle(ScrollEvent me) {

        if (me.getDeltaY() < 0 && zoomPosition > -840) {
          group.setTranslateZ(zoomPosition -= 10);
        } else if (me.getDeltaY() > 0 && zoomPosition < 500) {
          group.setTranslateZ(zoomPosition += 10);
        }
        //System.out.println(String.format("deltaX: %.3f deltaY: %.3f", me.getDeltaX(), me.getDeltaY()));
        //System.out.println(zoomPosition);
      }
    });

    stage.show();
    stage.setFullScreen(false);

  }

  private RotateTransition rotateAroundYAxis(Node node) {
    RotateTransition rotate = new RotateTransition(Duration.seconds(ROTATE_SECS), node);
    rotate.setAxis(Rotate.Y_AXIS);
    rotate.setFromAngle(360);
    rotate.setToAngle(0);
    rotate.setInterpolator(Interpolator.LINEAR);
    rotate.setCycleCount(RotateTransition.INDEFINITE);

    return rotate;
  }


  //  @Override
  public void handle(Event event) {
    ScrollEvent e = (ScrollEvent) event;
    CAMERA.setTranslateZ(CAMERA.getTranslateZ() * 0.75f);
    event.consume();
    //event.fireEvent(scene, event);
  }

  public static void main(String[] args) {
    launch(args);
  }
}

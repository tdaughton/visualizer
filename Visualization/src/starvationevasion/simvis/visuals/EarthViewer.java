package starvationevasion.simvis.visuals;

import javafx.animation.*;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.ZoomEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.input.MouseEvent;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Stack;


/**
    *http://stackoverflow.com/questions/19621423/javafx-materials-bump-and-spec-maps
    *Original Author:jewelsea,http://stackoverflow.com/users/1155209/jewelsea
    *Modified by:Tess Daughton
    **/


public class EarthViewer extends Application implements EventHandler
{

  private static final double EARTH_RADIUS = 400;
  private static final double VIEWPORT_SIZE = 800;
  private static final double ROTATE_SECS = 30;

  private static final double MAP_WIDTH = 8192 / 2d;
  private static final double MAP_HEIGHT = 4092 / 2d;
  private static final double SCALE_DELTA = 1.1;
  private double mousePosX=0, mousePosY=0, mouseOldX=0, mouseOldY=0;
  private double zoomPosition = 0;

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

  private Group buildScene()
  {
    Sphere earth = new Sphere(EARTH_RADIUS);
    earth.setTranslateX(VIEWPORT_SIZE / 2d);
    earth.setTranslateY(VIEWPORT_SIZE / 2d);

    PhongMaterial earthMaterial = new PhongMaterial();
    earthMaterial.setDiffuseMap
      (new Image(getClass().getResourceAsStream(DIFFUSE_MAP), MAP_WIDTH, MAP_HEIGHT, true, true));
    earthMaterial.setBumpMap
      (new Image(getClass().getResourceAsStream(NORMAL_MAP), MAP_WIDTH, MAP_HEIGHT, true, true));
    earthMaterial.setSpecularMap
      (new Image(getClass().getResourceAsStream(SPECULAR_MAP), MAP_WIDTH, MAP_HEIGHT, true, true));

    earth.setMaterial(earthMaterial);

    return new Group(earth);
  }

  @Override
  public void start(Stage stage)
  {
    group = buildScene();
    scene = new Scene(new StackPane(group), VIEWPORT_SIZE, VIEWPORT_SIZE, true, SceneAntialiasing.BALANCED);

    stage.setScene(scene);
    scene.getStylesheets().add(EarthViewer.class.getResource("style.css").toExternalForm());
    scene.setCamera(CAMERA);
    scene.setOnScroll(this);

    stage.show();
    stage.setFullScreen(true);

    scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent me) {
        if (me.getSceneX() > mouseOldX) {
          mousePosX += 1;
          group.setRotationAxis(Rotate.Y_AXIS);
          group.setRotate(mousePosX);

        } else if (me.getSceneX() < mouseOldX) {
          mousePosX -= 1;
          group.setRotationAxis(Rotate.Y_AXIS);
          group.setRotate(mousePosX);
        } else if (me.getSceneY() > mouseOldY) {
          mousePosY += 1;
          group.setRotationAxis(Rotate.X_AXIS);
          group.setRotate(mousePosY);
        } else if (me.getSceneY() < mouseOldY) {
          mousePosY -= 1;
          group.setRotationAxis(Rotate.X_AXIS);
          group.setRotate(mousePosY);
        }

        mouseOldX = me.getSceneX();
        mouseOldY = me.getSceneY();
      }
    });
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



    scene.setOnMousePressed(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent me) {

      }
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



  }

  private RotateTransition rotateAroundYAxis(Node node)
  {
    RotateTransition rotate = new RotateTransition(Duration.seconds(ROTATE_SECS), node);
    rotate.setAxis(Rotate.Y_AXIS);
    rotate.setFromAngle(360);
    rotate.setToAngle(0);
    rotate.setInterpolator(Interpolator.LINEAR);
    rotate.setCycleCount(RotateTransition.INDEFINITE);

    return rotate;
  }


  @Override
  public void handle(Event event)
  {
    ScrollEvent e = (ScrollEvent) event;
    CAMERA.setTranslateZ(CAMERA.getTranslateZ() * 0.75f);
    event.consume();
    //event.fireEvent(scene, event);
  }
  public static void main(String [] args)
  {
    launch(args);
  }
}

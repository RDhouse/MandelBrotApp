package com.ivik.main;

import com.ivik.model.Julia;
import com.ivik.model.Mandelbrot;
import com.ivik.util.SaveWindow;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


/**
 * Created by Sir Royal Air Benny on 22-2-2016.
 */
public class MandelbrotApp extends Application {
    private static final int SCENE_WIDTH = 1000;
    private static final int SCENE_HEIGHT = 1000;

    private static final int CANVAS_WIDTH = 800;
    private static final int CANVAS_HEIGHT = 800;

    private double mouseStartX, mouseStartY;
    private double mouseCurrentX, mouseCurrentY;
    private double mouseEndX, mouseEndY;

    private final double DEFAULT_MB_START_X = -2;
    private final double DEFAULT_MB_START_Y = 2;
    private final double DEFAULT_MB_WIDTH = 4;
    private final double DEFAULT_MB_HEIGHT = 4;
    private final double DEFAULT_JULIA_START_X = -2;
    private final double DEFAULT_JULIA_START_Y = 2;
    private final double DEFAULT_JULIA_WIDTH = 4;
    private final double DEFAULT_JULIA_HEIGHT = 4;
    private final int DEFAULT_MAX_ITERATIONS = 500; //500

    private Mandelbrot mb;
    private Julia julia;

    private double newStartX;
    private double newStartY;
    private double newWidth;
    private double newHeight;

    private Canvas layer1; // UI layer
    private static Canvas layer2; // Image layer, made static to support the saveToDisk method.
    private GraphicsContext gc_layer1; // draw to UI layer
    private GraphicsContext gc_layer2; // draw to Image layer

    private Label selectFractalSet;
    private Label selectColorScheme;
    private ChoiceBox<String> selectSet;
    private ChoiceBox<String> selectColor;
    private CheckBox selectCoordinateGrid;
    private Button resetImage;
    private Button saveImage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        // Elements
        layer1 = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        layer2 = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        gc_layer1 = layer1.getGraphicsContext2D();
        gc_layer2 = layer2.getGraphicsContext2D();

        selectSet = createSetSelection();
        selectFractalSet = new Label("Select Fractal Set:");
        selectColor = createColorSelection();
        selectColorScheme = new Label("Select Color Scheme:");
        selectCoordinateGrid = createCoordinateGridSelection();
        resetImage = createResetButton();
        saveImage = createSaveButton();

        initEventHandling();
        setDefaultMandelbrotImage();
        //setDefaultJuliaImage();

        // Layout
        Pane pane = new Pane();
        pane.getChildren().add(layer1);
        pane.getChildren().add(layer2);
        layer1.toFront();

        // Gridlayout might be better.
        HBox topMenu = new HBox();
        topMenu.setPadding(new Insets(5,5,5,5));
        topMenu.setSpacing(10);
        topMenu.setAlignment(Pos.CENTER);
        topMenu.getChildren().addAll(selectFractalSet, selectSet, selectColorScheme, selectColor, selectCoordinateGrid, resetImage, saveImage);

        BorderPane root = new BorderPane();
        root.setTop(topMenu);
        root.setCenter(pane);

        // Scene
        Scene scene = new Scene(root);
        // Stage
        stage.setScene(scene);
        stage.setTitle("MandelbrotViewer by Rutger Dijkhuizen");
        stage.show();
    }


    private void setDefaultMandelbrotImage() {

        WritableImage image = new WritableImage(CANVAS_WIDTH, CANVAS_HEIGHT);
        mb = new Mandelbrot(DEFAULT_MB_START_X, DEFAULT_MB_START_Y, DEFAULT_MB_WIDTH, DEFAULT_MB_HEIGHT,CANVAS_WIDTH, CANVAS_HEIGHT, DEFAULT_MAX_ITERATIONS);
        SwingFXUtils.toFXImage(mb.getMandelbrotBufferedImage(), image);

        gc_layer2.drawImage(image, 0, 0);
    }

    private void createMandelbrotImage()  {

        translateCoordinates();

        WritableImage image = new WritableImage(CANVAS_WIDTH, CANVAS_HEIGHT);
        mb = new Mandelbrot(newStartX, newStartY, newWidth, newHeight, CANVAS_WIDTH, CANVAS_HEIGHT, DEFAULT_MAX_ITERATIONS);
        SwingFXUtils.toFXImage(mb.getMandelbrotBufferedImage(), image);

        gc_layer2.drawImage(image, 0, 0);
    }

    private void setDefaultJuliaImage() {
        WritableImage image = new WritableImage(CANVAS_WIDTH, CANVAS_HEIGHT);
        julia = new Julia(DEFAULT_JULIA_START_X, DEFAULT_JULIA_START_Y, DEFAULT_JULIA_WIDTH, DEFAULT_JULIA_HEIGHT, CANVAS_WIDTH, CANVAS_HEIGHT, DEFAULT_MAX_ITERATIONS);
        SwingFXUtils.toFXImage(julia.getImage(), image);

        gc_layer2.drawImage(image, 0, 0);
    }

    private void createJuliaImage() {

        translateCoordinates();

        WritableImage image = new WritableImage(CANVAS_WIDTH, CANVAS_HEIGHT);
        julia = new Julia(newStartX, newStartY, newWidth, newHeight, CANVAS_WIDTH, CANVAS_HEIGHT, DEFAULT_MAX_ITERATIONS);
        SwingFXUtils.toFXImage(julia.getImage(), image);

        gc_layer2.drawImage(image, 0, 0);
    }

    private void translateCoordinates() {

        double deltaX = Math.abs(mouseStartX - mouseEndX);
        //System.out.println(deltaX);
        double deltaY = Math.abs(mouseStartY - mouseEndY);
        //System.out.println(deltaY);

        double xScale = CANVAS_WIDTH / deltaX;
        double yScale = CANVAS_HEIGHT / deltaY; // what if the width and height are different. distorting the image..?

        if(selectSet.getValue().equals("Mandelbrot Set")) {
            newStartX = mb.getStartX() + mb.getdX() * mouseStartX;
            //System.out.println(newStartX);
            newStartY = mb.getStartY() - mb.getdY() * mouseStartY;
            //System.out.println(newStartY);

            newWidth = newHeight = mb.getWidth() / xScale; // need a better solution.
        }
        if(selectSet.getValue().equals("Julia Set")) {
            newStartX = julia.getStartX() + julia.getdX() * mouseStartX;
            newStartY = julia.getStartY() - julia.getdY() * mouseStartY;

            newWidth = newHeight = julia.getWidth() / xScale;
        }
    }


    private void initEventHandling() {
        layer1.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                mouseCurrentX = mouseEvent.getX();
                mouseCurrentY = mouseEvent.getY();

                //System.out.println(mouseCurrentX + ", " + mouseCurrentY);
            }
        });

        layer1.setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                mouseStartX = mouseEvent.getX();
                mouseStartY = mouseEvent.getY();

                //System.out.format("START - X:%.1f, Y:%.1f\n", mouseStartX, mouseStartY);

                // Start drawing selection box..?

            }
        });
        // setOnMouseDragReleased does not seem to be called.
        layer1.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(!mouseEvent.isDragDetect()) { //Results in desired behaviour so far..
                    mouseEndX = mouseEvent.getX();
                    mouseEndY = mouseEvent.getY();
                    //System.out.println("END: " + mouseEndX + ", " + mouseEndY);

                    // if mandelbrot is selected create mandel, if julia is selected create julia set.
                    if (selectSet.getValue().equals("Mandelbrot Set")) {
                        createMandelbrotImage();
                    }
                    if (selectSet.getValue().equals("Julia Set")) {
                        createJuliaImage();
                    }

                }
            }
        });

        // Extra feature, not needed anymore...
        layer1.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getClickCount() > 1) {
                    if(selectSet.getValue().equals("Mandelbrot Set")) {
                        setDefaultMandelbrotImage();
                    }
                    if(selectSet.getValue().equals("Julia Set")) {
                        setDefaultJuliaImage();
                    }
                }
            }
        });
    }

    private ChoiceBox createSetSelection() {
        ChoiceBox<String> cb = new ChoiceBox<>();
        cb.getItems().add("Mandelbrot Set");
        cb.getItems().add("Julia Set");
        cb.setValue("Mandelbrot Set");

        // Add change listener.
        cb.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue observableValue, String o, String t1) {
                if(t1.equals("Mandelbrot Set")) {
                    setDefaultMandelbrotImage();
                }
                if(t1.equals("Julia Set")) {
                    setDefaultJuliaImage();
                }
            }
        });

        return cb;
    }

    private ChoiceBox createColorSelection() {
        ChoiceBox<String> cb = new ChoiceBox<>();
        cb.getItems().add("Default");
        cb.getItems().add("Groovy");
        cb.getItems().add("Relax");
        cb.setValue("Default");

        return cb;
    }

    private CheckBox createCoordinateGridSelection() {
        CheckBox cb = new CheckBox();
        cb.setText("CoordinateGrid On/Off");
        cb.setSelected(false);

        return cb;
    }

    private Button createResetButton() {
        Button btn = new Button("Reset Image");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(selectSet.getValue().equals("Mandelbrot Set")) {
                    setDefaultMandelbrotImage();
                }
                if(selectSet.getValue().equals("Julia Set")) {
                    setDefaultJuliaImage();
                }
            }
        });

        return btn;
    }

    private Button createSaveButton() {
        Button btn = new Button("Save Image");

        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                SaveWindow.display();
            }
        });

        return btn;
    }

    public static void saveImageToDisk(String filePath) {
        WritableImage snapshot = new WritableImage(CANVAS_WIDTH, CANVAS_HEIGHT);
        layer2.snapshot(null, snapshot);
        BufferedImage image = SwingFXUtils.fromFXImage(snapshot, null);
        try {
            ImageIO.write(image, "png", new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

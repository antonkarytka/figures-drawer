import Shapes.*;
import Shapes.Point;
import Shapes.Rectangle;
import Shapes.Shape;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private ArrayList<Shape> shapesList = new ArrayList<Shape>();
    private boolean drawingMode = true;
    private Shape currentShape;

    private boolean implementsSelectable(Shape shape) {
        return (shape instanceof Selectable);
    }

    private boolean isSelected(Shape shape, Point point) {
        Selectable selectableFigure = (Selectable)shape;
        return selectableFigure.contains(point);
    }

    @FXML
    private Canvas canvasDraw;
    @FXML
    private AnchorPane drawingPanel;
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private MenuItem saveXML;
    @FXML
    private MenuItem openXML;

    @FXML
    public void buttonClicked(ActionEvent event) {
        Button button = (Button) event.getSource();
        ShapeFactory shapeFactory = new ShapeFactory();
        currentShape = shapeFactory.getShape(button.getText());
    }

    @FXML
    public void setDrawMode() {
        drawingMode = true;
    }

    @FXML
    public void setSelectMode() {
        drawingMode = false;
    }

    @FXML
    public void clearDrawingPanel() {
        drawingMode = true;
        currentShape = new Polyline();
        shapesList.clear();
        final GraphicsContext gc = canvasDraw.getGraphicsContext2D();
        gc.clearRect(0, 0, 950, 655);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        currentShape = new Polyline();
        colorPicker.setValue(Color.BLACK);
        final GraphicsContext gc = canvasDraw.getGraphicsContext2D();
        final GraphicsContext gcBorder = canvasDraw.getGraphicsContext2D();

        drawingPanel.setOnMousePressed(event -> {
            if (drawingMode) {
                currentShape.borderColor = colorPicker.getValue();
                currentShape.addPoint(new Point((int)event.getX(), (int)event.getY()));
            }
        });

        drawingPanel.setOnMouseDragged(event -> {
            if (drawingMode) {
                gc.clearRect(0, 0, 950, 655);
                for (Shape figure : shapesList) {
                    figure.draw(gc);
                }
                currentShape.refreshFigure(new Point((int)event.getX(), (int)event.getY()));
                currentShape.draw(gc);
            }
        });

        drawingPanel.setOnMouseReleased(event -> {
            shapesList.add(currentShape);
            currentShape = new Polyline();
        });

        // ELLIPSE SELECTION STILL DOESN'T WORK

        drawingPanel.setOnMouseClicked(event -> {
            if (!drawingMode) {
                for (Shape shape : shapesList) {
                    if (implementsSelectable(shape)) {
                        if (isSelected(shape, new Point((int)event.getX(), (int)event.getY()))) {
                            gc.clearRect(0, 0, 950, 655);
                            for (Shape figure : shapesList) {
                                figure.draw(gc);
                            }
                            currentShape.refreshFigure(new Point((int)event.getX(), (int)event.getY()));
                            currentShape.draw(gc);
                            shape.drawBorder(gcBorder);
                        }
                    }
                }
            }
        });

        saveXML.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML file", "*.xml"));
            File file = fileChooser.showSaveDialog(null);
            // because extension filter doesn't work
            if (!file.getName().contains(".")) {
                file = new File(file.getAbsolutePath() + ".xml");
            }

            try {
                XStream xstream = new XStream(new StaxDriver());
                xstream.processAnnotations(new Class[] {XMLDeserializer.class, Polyline.class, Rectangle.class, Square.class, Triangle.class, Ellipse.class, Circle.class, Line.class});

                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write(xstream.toXML(shapesList));
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        openXML.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML file", "*.xml"));
            File file = fileChooser.showOpenDialog(null);
            if (file != null) {
                XStream xstream = new XStream(new StaxDriver());
                xstream.processAnnotations(new Class[] {XMLDeserializer.class, Polyline.class, Rectangle.class, Square.class, Triangle.class, Ellipse.class, Circle.class, Line.class});

                XMLDeserializer deserializer = (XMLDeserializer)xstream.fromXML(file);
                shapesList.addAll(deserializer.deserializedFigures);

                for (Shape shape : shapesList) {
                    shape.draw(gc);
                }
            }
        });
    }
}


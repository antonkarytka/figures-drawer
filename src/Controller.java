import Shapes.Point;
import Shapes.Shape;
import Shapes.ShapeFactory;
import com.sun.deploy.util.BlackList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    public ArrayList<Shape> shapesList = new ArrayList<Shape>();
    public int index;

    @FXML
    private Canvas canvasDraw;
    @FXML
    private AnchorPane drawingPanel;
    @FXML
    private ColorPicker colorPicker;

    @FXML
    public void buttonClicked(ActionEvent event) {
        Button button = (Button) event.getSource();
        ShapeFactory shapeFactory = new ShapeFactory();
        shapesList.add(shapeFactory.getShape(button.getText()));
        index++;
    }

    @FXML
    public void clearDrawingPanel() {
        index = -1;
        shapesList.clear();
        final GraphicsContext gc = canvasDraw.getGraphicsContext2D();
        gc.clearRect(0, 0, 900, 550);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colorPicker.setValue(Color.BLACK);
        final GraphicsContext gc = canvasDraw.getGraphicsContext2D();

        index = -1;

        drawingPanel.setOnMousePressed(event -> {
            try {
                shapesList.get(index).borderColor = colorPicker.getValue();
                shapesList.get(index).addPoint(new Point((int)event.getX(), (int)event.getY()));
            } catch (IndexOutOfBoundsException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ooops...");
                alert.setHeaderText("Trying to draw a figure without choosing it");
                alert.setContentText("You should choose a figure first!");
                alert.show();
            }
        });

        drawingPanel.setOnMouseDragged(event -> {
            shapesList.get(index).refreshFigure(new Point((int)event.getX(), (int)event.getY()));
            gc.clearRect(0, 0, 900, 550);
            for (Shape figure: shapesList) {
                figure.draw(gc);
            }
        });
    }
}

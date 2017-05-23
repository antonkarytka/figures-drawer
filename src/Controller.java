import asbtract.Shape;
import com.thoughtworks.xstream.converters.reflection.AbstractReflectionConverter;
import com.thoughtworks.xstream.io.StreamException;
import interfaces.Editable;
import interfaces.Selectable;
import secondary.XMLDeserializer;
import shapes.*;
import shapes.Point;
import shapes.Rectangle;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private ArrayList<Shape> shapesList = new ArrayList<Shape>();
    private boolean drawingMode = true;
    private Shape currentShape;

    private boolean implementsSelectable(Shape shape) {
        return (shape instanceof Selectable);
    }

    private boolean implementsEditable(Shape shape) {
        return (shape instanceof Editable);
    }

    private boolean isSelected(Shape shape, Point point) {
        Selectable selectableFigure = (Selectable)shape;
        return selectableFigure.contains(point);
    }

    private void setColor(Shape shape, Color color) {
        Editable editedFigure = (Editable)shape;
        editedFigure.setColor(color);
    }

    private void setLineWidth(Shape shape, int width) {
        Editable editedFigure = (Editable)shape;
        editedFigure.setLineWidth(width);
    }

    private static Class[] pushClass(Class[] array, Class push) {
        Class[] longer = new Class[array.length + 1];
        for (int i = 0; i < array.length; i++)
            longer[i] = array[i];
        longer[array.length] = push;
        return longer;
    }

    @FXML
    private ChoiceBox<String> figureChoiceBox;
    @FXML
    private Canvas canvasDraw;
    @FXML
    private AnchorPane drawingPanel;
    @FXML
    private MenuItem saveXML;
    @FXML
    private MenuItem openXML;
    @FXML
    private MenuItem quit;
    @FXML
    private MenuItem howToAddOwnFigures;
    @FXML
    private MenuItem about;
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private ChoiceBox<Integer> lineWidthChoice;

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
        currentShape = new Pencil();
        shapesList.clear();
        final GraphicsContext gc = canvasDraw.getGraphicsContext2D();
        gc.clearRect(0, 0, 950, 655);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String modulesPath = "/home/anton/Programming/figures-drawer/out/production/figures-drawer/shapes/";
        ModuleLoader loader = new ModuleLoader(modulesPath, "shapes", ClassLoader.getSystemClassLoader());
        List<String> modules = new ArrayList<String>();
        File[] files = new File(modulesPath).listFiles();
        if (files != null) {
            for (File file: files) {
                if (file.isFile()) {
                    modules.add(file.getName().split(".class")[0]);
                }
            }
        }

        List<Class> loadedClasses = new ArrayList<>();
        loadedClasses.add(XMLDeserializer.class);
        List<String> validModules = new ArrayList<>();
        for (String module: modules) {
            try {
                Class figureClass = Class.forName("shapes." + module);
                if (figureClass.getSuperclass().equals(Shape.class)) {
                    try {
                        loadedClasses.add(figureClass);
                        validModules.add(module);
                        loader.loadClass(module);
                    } catch (Exception e) {
                        System.out.println(module + " class is corrupted.");
                    }
                }
            } catch (Error err) {
                System.out.println(module + " class is corrupted.");
            } catch (ClassNotFoundException e) {
                System.out.println(module + " class was not found.");
            }
        }

        figureChoiceBox.setItems(FXCollections.observableArrayList(validModules));
        if (modules.size() > 0) {
            figureChoiceBox.setValue(validModules.get(0));
        } else {
            figureChoiceBox.setValue("None");
        }
        figureChoiceBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            try {
                Class cl = Class.forName("shapes." + newValue);
                currentShape = (Shape) cl.newInstance();
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        });

        if (validModules.size() > 0) {
            try {
                Class<?> figureClass = Class.forName("shapes." + validModules.get(0));
                Constructor<?> constructor = figureClass.getConstructor();
                currentShape = (Shape) constructor.newInstance();
            } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        colorPicker.setValue(Color.BLACK);
        lineWidthChoice.setItems(FXCollections.observableArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        lineWidthChoice.setValue(5);
        final GraphicsContext gc = canvasDraw.getGraphicsContext2D();
        final GraphicsContext gcBorder = canvasDraw.getGraphicsContext2D();

        drawingPanel.setOnMousePressed(event -> {
            if (drawingMode) {
                if (implementsEditable(currentShape)) {
                    setColor(currentShape, colorPicker.getValue());
                    setLineWidth(currentShape, lineWidthChoice.getValue());
                } else {
                    currentShape.borderColor = Color.BLACK;
                    currentShape.lineWidth = 5;
                    colorPicker.setValue(Color.BLACK);
                    lineWidthChoice.setValue(5);
                }
                currentShape.addPoint(new Point((int) event.getX(), (int) event.getY()));
            }
        });

        drawingPanel.setOnMouseDragged(event -> {
            if (drawingMode) {
                gc.clearRect(0, 0, 950, 655);
                for (Shape figure : shapesList) {
                    figure.draw(gc);
                }
                currentShape.refreshFigure(new Point((int) event.getX(), (int) event.getY()));
                currentShape.draw(gc);
            }
        });

        drawingPanel.setOnMouseReleased(event -> {
            shapesList.add(currentShape);
            currentShape = new Pencil();
        });

        drawingPanel.setOnMouseClicked(event -> {
            if (!drawingMode) {
                for (Shape shape : shapesList) {
                    if (implementsSelectable(shape)) {
                        if (isSelected(shape, new Point((int) event.getX(), (int) event.getY()))) {
                            gc.clearRect(0, 0, 950, 655);
                            for (Shape figure : shapesList) {
                                figure.draw(gc);
                            }
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
                xstream.autodetectAnnotations(true);

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
                Class[] loadedClassesArray = new Class[0];
                loadedClassesArray = loadedClasses.toArray(loadedClassesArray);

                XStream xstream = new XStream(new StaxDriver());
                xstream.processAnnotations(loadedClassesArray);
                try {
                    XMLDeserializer deserializer = (XMLDeserializer) xstream.fromXML(file);
                    shapesList.addAll(deserializer.deserializedFigures);
                    for (Shape shape : shapesList) {
                        shape.draw(gc);
                    }
                } catch (AbstractReflectionConverter.UnknownFieldException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Oops...");
                    alert.setHeaderText("Class is not loaded");
                    alert.setContentText("Error occurred while opening XML file.\nFigure's class has not been loaded.");
                    alert.show();
                } catch (StreamException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Oops...");
                    alert.setHeaderText("Opened XML file is corrupted!");
                    alert.setContentText("Error occurred while opening XML file.\nProbably, it has been corrupted.");
                    alert.show();
                }
            }
        });

        quit.setOnAction(event -> {
            System.exit(0);
        });

        howToAddOwnFigures.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Own figures");
            alert.setHeaderText("How to add your own figures?");
            alert.setContentText("1. Write your figure's java class.\n2. Compile it.\n3. Put the compiled class into the following directory:\n    /out/production/figures-drawer/shapes\n4. Restart Figures Drawer.");
            alert.show();
        });

        about.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Figures Drawer");
            alert.setHeaderText("What is it?");
            alert.setContentText("Figures Drawer is a program for drawing pictures.\nYou can save them in XML for further usage and\nyou can also add your own figures!");
            alert.show();
        });
    }
}


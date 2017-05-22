public class ShapesLoader {

    public static void main(String[] args) {
        ClassLoader classLoader = ShapesLoader.class.getClassLoader();

        try {
            classLoader.loadClass("/shapes/shapes.Shape.java");
            classLoader.loadClass("/shapes/Pencil.java");
            classLoader.loadClass("/shapes/Line.java");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

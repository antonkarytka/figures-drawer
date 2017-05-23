package shapes;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("point")
public class Point {

    public int x, y;

    public Point() {}

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

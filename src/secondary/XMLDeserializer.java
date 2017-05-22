package secondary;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import asbtract.Shape;

import java.util.ArrayList;
import java.util.List;

@XStreamAlias("list")
public class XMLDeserializer {

    @XStreamImplicit
    public List<Shape> deserializedFigures;

    public XMLDeserializer() {
        deserializedFigures = new ArrayList<Shape>();
    }
}


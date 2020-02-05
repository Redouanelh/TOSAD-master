package define.business.domain;

public class TableAttribute {
    private String name;
    private String datatype;
    private int length;
    private int scale;

    public TableAttribute(String name) {
        this.name = name;
    }

    public void setDatatype(String datatype) {
        this.datatype = datatype;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public String getName() {
        return name;
    }

    public String getDatatype() {
        return datatype;
    }

    public int getLength() {
        return length;
    }

    public int getScale() {
        return scale;
    }
}

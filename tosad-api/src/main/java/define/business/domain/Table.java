package define.business.domain;

public class Table {
    private String name;
    private TableAttribute selectedTableAttribute;

    public Table(String name, TableAttribute selectedTableAttribute) {
        this.name = name;
        this.selectedTableAttribute = selectedTableAttribute;
    }

    public String getName() {
        return name;
    }

    public TableAttribute getSelectedTableAttribute() {
        return selectedTableAttribute;
    }

}

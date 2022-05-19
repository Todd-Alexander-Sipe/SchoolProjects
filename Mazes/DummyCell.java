import java.util.ArrayList;
import java.util.List;

public class DummyCell {
    private List<Cell> dummyList;

    public DummyCell() {
        dummyList = new ArrayList<>();
    }

    public void addToDummy(Cell cell) {
        dummyList.clear();
        dummyList.add(cell);
    }

    public Cell getFromDummy() {
        return dummyList.get(0);
    }
}

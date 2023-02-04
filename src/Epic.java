import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private ArrayList<Integer> epicSubtasks; //Модификатор достипа изменен на приватный

    public Epic(String title) {
        super(title);
        epicSubtasks =new ArrayList<>();
    }

    public ArrayList<Integer> getEpicSubtasks() {
        return epicSubtasks;
    }

    public void setEpicSubtasks(ArrayList<Integer> epicSubtasks) {
        this.epicSubtasks = epicSubtasks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Epic epic = (Epic) o;
        return Objects.equals(epicSubtasks, epic.epicSubtasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(epicSubtasks);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "epicSubtasks=" + epicSubtasks +
                '}';
    }
}

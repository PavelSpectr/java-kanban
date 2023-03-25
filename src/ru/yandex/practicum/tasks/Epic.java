package ru.yandex.practicum.tasks;

import ru.yandex.practicum.manager.TaskType;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private ArrayList<Integer> epicSubtasks;

    public Epic(String title) {
        super(title);
        epicSubtasks = new ArrayList<>();
    }

    public Epic(int id, String title, Status status) {
        super(id, title, status);
        epicSubtasks = new ArrayList<>();
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
    public String toString() { //Сделал вывод более информативным
        StringBuilder sb = new StringBuilder();
        return sb.append(getId()).append(",")
                .append(TaskType.EPIC).append(',')
                .append(getTitle()).append(',')
                .append(getStatus())
                .toString();
/*                "Epic{"+ super.toString() +
                "epicSubtasks=" + epicSubtasks +
                '}';*/
    } //Читал про StringBuffer - так было бы более информативно, возможно, в будущем переделаю
}

package ru.yandex.practicum.tasks;

import ru.yandex.practicum.manager.TaskType;

import java.util.Objects;

public class Subtask extends Task {
    private int epicId;

    public Subtask(String title, String description, int epicId) {
        super(title, description);
        this.epicId = epicId;
    }

    public Subtask(int id, String title, String description, Status status, int epicId) {
        super(id, title, description, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subtask subtask = (Subtask) o;
        return epicId == subtask.epicId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(epicId);
    }

    @Override
    public String toString() { //Переопределил метод вывода, сделал его информативным
        StringBuilder sb = new StringBuilder();
        return sb.append(getId()).append(',')
                .append(TaskType.SUBTASK).append(',')
                .append(getTitle()).append(',')
                .append(getDescription()).append(',')
                .append(getStatus()).append(',')
                .append(epicId)
                .toString();
                /*"Subtask{" + super.toString() +
                "epicId=" + epicId +
                '}';*/
    }
}

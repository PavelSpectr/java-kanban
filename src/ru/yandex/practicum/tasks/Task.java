package ru.yandex.practicum.tasks;

import jdk.jshell.Snippet;
import ru.yandex.practicum.manager.TaskType;

import java.util.Objects;

public class Task {

    private int id;
    private String title;
    private String description;
    private Status status = Status.NEW; //Добавил инициализацию статуса, чтобы не повторять сеттер при создании объекта
    // NEW — задача только создана, но к её выполнению ещё не приступили.
    // IN_PROGRESS — над задачей ведётся работа.
    // DONE — задача выполнена.

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public Task(String title) {
        this.title = title;
    }

    public Task(int id, String title, Status status) {
        this.id = id;
        this.title = title;
        this.status = status;
    }

    public Task(int id, String title, String description, Status status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
    }


    public String getTitle() { //Добавил геттеры и сеттеры для полей title и description
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(title, task.title) && Objects.equals(description, task.description) && Objects.equals(status, task.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, id, status);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        return sb.append(id).append(',')
                .append(TaskType.TASK).append(',')
                .append(title).append(',')
                .append(description).append(',')
                .append(status)
                .toString();
/*                "Task{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                '}';*/
    }
}

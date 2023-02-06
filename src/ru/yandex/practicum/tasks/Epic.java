package ru.yandex.practicum.tasks;

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
    public String toString() { //Сделал вывод более информативным
        return "ru.yandex.practicum.tasks.Epic{"+ super.toString() +
                "epicSubtasks=" + epicSubtasks +
                '}';
    } //Читал про StringBuffer - так было бы более информативно, возможно, в будущем переделаю
}

package ru.yandex.practicum.manager;

import ru.yandex.practicum.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final List<Task> taskHistory = new ArrayList<>();
    // Убрал static модификатор - Действительно, не подумал что это будет работать,
    // как общее для всех польователей,
    // но это довольно сложно понять, без реального теста приложения)
    // Но спасибо, постараюсь учитывать этот момент впредь)

    @Override
    public void add(Task task) {
        if (task != null) {
            taskHistory.add(task);
            if(taskHistory.size() > 10) {
                taskHistory.remove(0);
            }
        }
    }

    @Override
    public List<Task> getHistory() {
        return taskHistory;
    }

/*    @Override
    public void printHistory() { // А мне казалось, что так на порядок удобнее)
        for (Task task : getHistory()) { // Пока закоммичу и оставлю для наглядности.
            System.out.println(task); // Потом уберу, чтоб не засорять код)
        }
    }*/
}

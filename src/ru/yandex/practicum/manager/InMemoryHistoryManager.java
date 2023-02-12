package ru.yandex.practicum.manager;

import ru.yandex.practicum.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private static final List<Task> taskHistory = new ArrayList<>();

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

    @Override
    public void printHistory() {
        for (Task task : getHistory()) {
            System.out.println(task);
        }
    }
}

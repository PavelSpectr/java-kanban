package ru.yandex.practicum.manager;

import ru.yandex.practicum.tasks.Epic;
import ru.yandex.practicum.tasks.Subtask;
import ru.yandex.practicum.tasks.Task;

import java.util.List;

public interface TaskManager {
    // 2.1 Получение списка всех задач
    List<Task> getTasks();

    List<Subtask> getSubtasks();

    List<Epic> getEpics();

    // 2.2 Удаление всех задач
    void deleteAllTasks();

    void deleteAllSubtasks();

    void deleteAllEpics();

    // 2.3 Получение по идентификатору
    Task getTaskById(int id);

    Subtask getSubtaskById(int id);

    Epic getEpicById(int id);

    // 2.3.1 Получение истории просмотра всех задач, полученных по id
    //List<Objects> getHistory();

    // 2.4 Создание. Сам объект должен передаваться в качестве параметра
    void addTask(Task task);

    void addSubtask(Subtask subtask);

    void addEpic(Epic epic);

    // 2.5 Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра
    void updateTask(Task task);

    void updateSubtask(Subtask subtask);

    void updateEpic(Epic epic);

    // 2.6 Удаление по идентификатору
    void deleteTaskById(int id);

    void deleteSubtaskById(int id);

    void deleteEpicById(int id);

    List<Task> getHistory(); // Добавил метод для релизации в менеджере
}

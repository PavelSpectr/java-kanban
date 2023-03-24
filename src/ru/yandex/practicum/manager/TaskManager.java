package ru.yandex.practicum.manager;

import ru.yandex.practicum.exceptions.ManagerSaveException;
import ru.yandex.practicum.tasks.Epic;
import ru.yandex.practicum.tasks.Subtask;
import ru.yandex.practicum.tasks.Task;

import java.io.IOException;
import java.util.List;

public interface TaskManager {
    // 2.1 Получение списка всех задач
    List<Task> getTasks();

    List<Subtask> getSubtasks();

    List<Epic> getEpics();

    // 2.2 Удаление всех задач
    void deleteAllTasks() throws ManagerSaveException;

    void deleteAllSubtasks() throws ManagerSaveException;

    void deleteAllEpics() throws ManagerSaveException;

    // 2.3 Получение по идентификатору
    Task getTaskById(int id) throws ManagerSaveException;

    Subtask getSubtaskById(int id) throws ManagerSaveException;

    Epic getEpicById(int id) throws ManagerSaveException;

    // 2.3.1 Получение истории просмотра всех задач, полученных по id
    List<Task> getHistory(); // Добавил метод для релизации в менеджере

    // 2.4 Создание. Сам объект должен передаваться в качестве параметра
    void addTask(Task task) throws ManagerSaveException;

    void addSubtask(Subtask subtask) throws IOException;

    void addEpic(Epic epic) throws ManagerSaveException;

    // 2.5 Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра
    void updateTask(Task task) throws ManagerSaveException;

    void updateSubtask(Subtask subtask) throws ManagerSaveException;

    void updateEpic(Epic epic) throws ManagerSaveException;

    // 2.6 Удаление по идентификатору
    void deleteTaskById(int id) throws ManagerSaveException;

    void deleteSubtaskById(int id) throws ManagerSaveException;

    void deleteEpicById(int id) throws ManagerSaveException;
}

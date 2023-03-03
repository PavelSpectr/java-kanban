package ru.yandex.practicum.manager;

import ru.yandex.practicum.tasks.Epic;
import ru.yandex.practicum.tasks.Status;
import ru.yandex.practicum.tasks.Subtask;
import ru.yandex.practicum.tasks.Task;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private int id = 0;

    // 1. Хранить задачи всех типов
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HistoryManager taskHistory = Managers.getDefaultHistory();

    // 2.1 Получение списка всех задач
    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    // 2.2 Удаление всех задач
    @Override
    public void deleteAllTasks() {
        for (Map.Entry<Integer, Task> taskEntry : tasks.entrySet()) {
            taskHistory.removeFromHistory(taskEntry.getValue().getId());
        }
        tasks.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        for (Map.Entry<Integer, Subtask> subtaskEntry : subtasks.entrySet()) {
            taskHistory.removeFromHistory(subtaskEntry.getValue().getId());
        }
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.getEpicSubtasks().clear();
            updateStatusEpic(epic);
        }
    }

    @Override
    public void deleteAllEpics() {
        for (Map.Entry<Integer, Epic> epicEntry : epics.entrySet()) {
            taskHistory.removeFromHistory(epicEntry.getValue().getId());
        }
        for (Map.Entry<Integer, Subtask> subtaskEntry : subtasks.entrySet()) {
            taskHistory.removeFromHistory(subtaskEntry.getValue().getId());
        }
        epics.clear();
        subtasks.clear();
    }

    // 2.3 Получение по идентификатору
    @Override
    public Task getTaskById(int id) {
        taskHistory.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Subtask getSubtaskById(int id) {
        taskHistory.add(subtasks.get(id));
        return subtasks.get(id);
    }

    @Override
    public Epic getEpicById(int id) {
        taskHistory.add(epics.get(id));
        return epics.get(id);
    }

    // 2.4 Создание. Сам объект должен передаваться в качестве параметра
    @Override
    public void addTask(Task task) {
        task.setId(++id);
        tasks.put(id, task);
    }

    @Override
    public void addSubtask(Subtask subtask) {
        subtask.setId(++id);
        subtasks.put(id, subtask);
        epics.get(subtask.getEpicId()).getEpicSubtasks().add(id);
        updateStatusEpic(epics.get(subtask.getEpicId()));
    }

    @Override
    public void addEpic(Epic epic) {
        epic.setId(++id);
        epics.put(id, epic);
    }

    // 2.5 Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра
    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId())) {
            subtasks.put(subtask.getId(), subtask);
            updateStatusEpic(epics.get(subtask.getEpicId()));
        }
    }

    @Override
    public void updateEpic(Epic epic) { //Добавил обновление эпика, да действительно - моё упущение)
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
        }
    }

    // 2.6 Удаление по идентификатору
    @Override
    public void deleteTaskById(int id) {
        System.out.println("Задача с id# " + id +" удалена." + System.lineSeparator());
        tasks.remove(id);
        taskHistory.remove(id);
    }

    @Override
    public void deleteSubtaskById(int id) {
        System.out.println("Подзадача с id# " + id +" удалена." + System.lineSeparator());
        if (subtasks.containsKey(id)) {
            Epic epic = epics.get(subtasks.get(id).getEpicId());
            epic.getEpicSubtasks().remove((Integer) id);
            updateStatusEpic(epic);
            subtasks.remove(id);
            taskHistory.remove(id);
        }
    }

    @Override
    public void deleteEpicById(int id) {
        System.out.println("Эпик с id# " + id +" удален." + System.lineSeparator());
        if (epics.containsKey(id)) {
            Epic epic = epics.get(id);
            ArrayList<Integer> subtaskIdList = epic.getEpicSubtasks();
            for (int value : subtaskIdList) {
                subtasks.remove(value);
            }
            epics.remove(id);
            taskHistory.remove(id);
        }
    }

    // 3. Получение списка всех подзадач определённого эпика

    // 4.1 Менеджер сам не выбирает статус для задачи.
    //     Информация о нём приходит менеджеру вместе с информацией о самой задаче.
    //     По этим данным в одних случаях он будет сохранять статус, в других будет рассчитывать.

    // 4.2 Для эпиков:
    //     если у эпика нет подзадач или все они имеют статус NEW, то статус должен быть NEW.
    //     если все подзадачи имеют статус DONE, то и эпик считается завершённым — со статусом DONE.
    //     во всех остальных случаях статус должен быть IN_PROGRESS.
    private boolean checkStatus(Status status, Epic epic) {
        for (int subtaskId : epic.getEpicSubtasks()) {
            if (!Objects.equals(subtasks.get(subtaskId).getStatus(), status)) {
                return false;
            }
        }
        return true;
    }

    private void updateStatusEpic(Epic epic) {
        if (epic.getEpicSubtasks().isEmpty() || checkStatus(Status.NEW, epic)) {
            epic.setStatus(Status.NEW);

        } else if (checkStatus(Status.DONE, epic)) {
            epic.setStatus(Status.DONE);

        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    @Override
    public List<Task> getHistory() { // Реалзовал метод получения истории просмотров
        return taskHistory.getHistory();
    }
}

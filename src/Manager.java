// Мы можем пообщаться лично и познакомиться? Хотелось бы узнать Ваш путь программиста) @Titsubishi - мой телеграмм.
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Manager {
    private int id = 0;

    // 1. Хранить задачи всех типов
    public HashMap<Integer, Task> tasks = new HashMap<>();
    public HashMap<Integer, Epic> epics = new HashMap<>();
    public HashMap<Integer, Subtask> subtasks = new HashMap<>();

    // 2.1 Получение списка всех задач
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values()); //Убрал ключевое this
    } //Сделал отступы между всеми методами

    public List<Subtask> getSubtasks() {
        return new ArrayList<>(this.subtasks.values());
    }

    public List<Epic> getEpics() {
        return new ArrayList<>(this.epics.values());
    }

    // 2.2 Удаление всех задач
    public void deleteAllTasks() {
        tasks.clear();
    }

    public void deleteAllSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.getEpicSubtasks().clear();
            updateStatusEpic(epic);
        }
    }

    public void deleteAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    // 2.3 Получение по идентификатору
    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public Subtask getSubtaskById(int id) {
        return subtasks.get(id);
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    // 2.4 Создание. Сам объект должен передаваться в качестве параметра
    public void addTask(Task task) {
        task.setId(++id);
        //task.setStatus("NEW"); //Убрал повторяющиеся сеттеры в методах
        tasks.put(id, task);
    }

    public void addSubtask(Subtask subtask) {
        subtask.setId(++id);
        //subtask.setStatus("NEW");
        subtasks.put(id, subtask);
        epics.get(subtask.getEpicId()).getEpicSubtasks().add(id);
        updateStatusEpic(epics.get(subtask.getEpicId()));
    }

    public void addEpic(Epic epic) {
        epic.setId(++id);
        //epic.setStatus("NEW");
        epics.put(id, epic);
    }

    // 2.5 Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    public void updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId())) {
            subtasks.put(subtask.getId(), subtask);
            updateStatusEpic(epics.get(subtask.getEpicId()));
        }
    }

    public void updateEpic(Epic epic) { //Добавил обновление эпика, да действительно - моё упущение)
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
        }
    }

    // 2.6 Удаление по идентификатору
    public void deleteTaskById(int id) {
        System.out.println("Задача с id# " + id +" удалена." + System.lineSeparator());
        tasks.remove(id);
    }

    public void deleteSubtaskById(int id) {
        System.out.println("Подзадача с id# " + id +" удалена." + System.lineSeparator());
        if (subtasks.containsKey(id)) {
            Epic epic = epics.get(subtasks.get(id).getEpicId());
            epic.getEpicSubtasks().remove((Integer) id);
            updateStatusEpic(epic);
            subtasks.remove(id);
        }
    }

    public void deleteEpicById(int id) {
        System.out.println("Эпик с id# " + id +" удален." + System.lineSeparator());
        if (epics.containsKey(id)) {
            Epic epic = epics.get(id);
            ArrayList<Integer> subtaskIdList = epic.getEpicSubtasks();
            for (int value : subtaskIdList) {
                subtasks.remove(value);
            }
            epics.remove(id);
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
    private boolean checkStatus(String status, Epic epic) { //Благодарю) По-моему это было очень логичным решением
        for (int subtaskId : epic.getEpicSubtasks()) {
            if (!Objects.equals(subtasks.get(subtaskId).getStatus(), status)) {
                return false;
            }
        }
        return true;
    }

    private void updateStatusEpic(Epic epic) {
        if (epic.getEpicSubtasks().isEmpty() || checkStatus("NEW", epic)) {
            epic.setStatus("NEW");

        } else if (checkStatus("DONE", epic)) {
            epic.setStatus("DONE");

        } else {
            epic.setStatus("IN_PROGRESS");
        }
    }
}

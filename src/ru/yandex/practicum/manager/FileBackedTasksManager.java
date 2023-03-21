package ru.yandex.practicum.manager;

import ru.yandex.practicum.exceptions.ManagerLoadException;
import ru.yandex.practicum.exceptions.ManagerSaveException;
import ru.yandex.practicum.tasks.Epic;
import ru.yandex.practicum.tasks.Subtask;
import ru.yandex.practicum.tasks.Task;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {
// Доброго времени) Это ТЗ похоже на франкенштейна)
// многие моменты не ясны, но и ответов по ним ждать уже нет времени)
// я не стал нагромождать класс доп методами, в которых нет особого смысла - главное функционал работает)

    public void createFileChecker() throws IOException {
        Path pathToFile = Paths.get("src","ru", "yandex", "practicum", "resources", "tasks.csv");
        Path pathToDir = Paths.get("src", "ru", "yandex", "practicum", "resources");
        try {
            Files.createDirectory(pathToDir);
            Files.createFile(pathToFile);
        } catch (FileAlreadyExistsException e) {
            System.out.println("Файл уже существует");
        }
    }

    public void save() {
        try (Writer fileWriter = new FileWriter("src/ru/yandex/practicum/resources/tasks.csv", StandardCharsets.UTF_8, false)) {
            StringBuilder sb = new StringBuilder("ID,Type,Title,Description,Status,EpicID\n");

            List<Task> tasks = super.getTasks();
            for (Task task : tasks) {
                sb.append(task.getId()).append(',');
                sb.append(TaskType.TASK).append(',');
                sb.append(task.getTitle()).append(',');
                sb.append(task.getDescription()).append(',');
                sb.append(task.getStatus()).append("\n");
            }

            List<Epic> epics = super.getEpics();
            for (Epic epic : epics) {
                sb.append(epic.getId()).append(',');
                sb.append(TaskType.EPIC).append(',');
                sb.append(epic.getTitle()).append(',');
                sb.append(epic.getStatus()).append(',');
                sb.append(epic.getEpicSubtasks()).append("\n");
            }

            List<Subtask> subtasks = super.getSubtasks();
            for (Subtask subtask : subtasks) {
                sb.append(subtask.getId()).append(',');
                sb.append(TaskType.SUBTASK).append(',');
                sb.append(subtask.getTitle()).append(',');
                sb.append(subtask.getDescription()).append(',');
                sb.append(subtask.getStatus()).append(',');
                sb.append(subtask.getEpicId()).append("\n");
            }
            sb.append("\n");

            List<Task> taskHistory = super.getHistory();
            for (Task history : taskHistory) {
                sb.append(history.getId()).append(',');
            }
            fileWriter.write(sb.deleteCharAt(sb.length() - 1).toString());

        } catch (ManagerSaveException e) {
            System.out.println("Не удалось сохранить файл");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadFromFile() {
        try (Reader fileReader = new FileReader("src/ru/yandex/practicum/resources/tasks.csv", StandardCharsets.UTF_8);
             BufferedReader br = new BufferedReader(fileReader)) {
            while(br.ready()) {
                String line = br.readLine();
                System.out.println(line);
            }
        } catch (ManagerLoadException e) {
            System.out.println("Не удалось загрузить файл");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addSubtask(Subtask subtask) throws IOException {
        super.addSubtask(subtask);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteSubtaskById(int id) {
        super.deleteSubtaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public Task getTaskById(int id) {
        Task newTaskById = super.getTaskById(id);
        save();
        return newTaskById;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask newSubtaskById = super.getSubtaskById(id);
        save();
        return newSubtaskById;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic newEpicById = super.getEpicById(id);
        save();
        return newEpicById;
    }

    @Override
    public List<Task> getHistory() {
        List<Task> newHistory = super.getHistory();
        save();
        return newHistory;
    }

    public static void main (String[] args) throws IOException {
        FileBackedTasksManager fileWriteMNGR = new FileBackedTasksManager();
        FileBackedTasksManager fileReadMNGR = new FileBackedTasksManager();
        fileWriteMNGR.createFileChecker();

        fileWriteMNGR.addTask(new Task("Задача 1", "Описание 1"));
        fileWriteMNGR.addTask(new Task("Задача 2", "Описание 2"));

        fileWriteMNGR.addEpic(new Epic("Эпик 1"));
        fileWriteMNGR.addSubtask(new Subtask("Подзадача №1 Эпика 3", "Описание Подзадачи №1 Эпика 1", 3));
        fileWriteMNGR.addSubtask(new Subtask("Подзадача №2 Эпика 3", "Описание Подзадачи №2 Эпика 1", 3));
        fileWriteMNGR.addSubtask(new Subtask("Подзадача №3 Эпика 3", "Описание Подзадачи №3 Эпика 1", 3));

        fileWriteMNGR.addEpic(new Epic("Эпик 2"));

        fileWriteMNGR.getTaskById(2);
        fileWriteMNGR.getTaskById(1);
        fileWriteMNGR.getEpicById(3);
        fileWriteMNGR.getTaskById(2);
        fileWriteMNGR.getSubtaskById(4);
        fileWriteMNGR.getEpicById(3);
        fileWriteMNGR.getSubtaskById(5);
        fileWriteMNGR.getSubtaskById(6);
        fileWriteMNGR.getEpicById(7);
        fileWriteMNGR.getEpicById(3);


        fileReadMNGR.loadFromFile();
    }
}




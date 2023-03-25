package ru.yandex.practicum.manager;

import ru.yandex.practicum.exceptions.ManagerLoadException;
import ru.yandex.practicum.exceptions.ManagerSaveException;
import ru.yandex.practicum.tasks.Epic;
import ru.yandex.practicum.tasks.Status;
import ru.yandex.practicum.tasks.Subtask;
import ru.yandex.practicum.tasks.Task;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager { // Убрал имплементацию
/*
Доброго времени!)
Все поправил в соответствии с указаниями)
Вот только со статиком не разобрался.
В ТЗ нет такого чтобы метод save делать статичным.
Но есть про loadFromFile. Но тогда приходится все делать статичным, вплоть до интерфейса.
sudo apt update:
Подсказали ребята, как сделать метод статичным - сделал)
Низкий поклон, что теперь я понял, как это работает)
Но чет не помню, чтоб нас учили такому. С мейном еще возились, но чтоб объект класса в самом классе создавать...
Я бы скорее умом тронулся xD
Ладно, побегу 7й спринт - там жесть. Отстаю на 2 недели.
Хороших выходных)
*/
    private final File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    // Пока просто закомментирую этот метод, что есть еще такой вариант,
    // для наглядности и чтоб сам не забыл такую реализацию)
   /*public void createFileChecker() throws IOException {
        Path pathToFile = Paths.get("src","ru", "yandex", "practicum", "resources", "tasks.csv");
        Path pathToDir = Paths.get("src", "ru", "yandex", "practicum", "resources");
        try {
            Files.createDirectory(pathToDir);
            Files.createFile(pathToFile);
        } catch (FileAlreadyExistsException e) {
            System.out.println("Файл уже существует");
        }
    }*/

    public void save() throws ManagerSaveException { //
        try (Writer fileWriter = new FileWriter(file, StandardCharsets.UTF_8, false)) {
            StringBuilder sb = new StringBuilder("ID,Type,Title,Description,Status,EpicID\n");

            List<Task> tasks = getTasks(); // Теперь без super и вдвойне вкусней
            for (Task task : tasks) {
                sb.append(task.toString()).append("\n");
            }

            List<Epic> epics = getEpics();
            for (Epic epic : epics) {
                sb.append(epic.toString()).append("\n");
            }

            List<Subtask> subtasks = getSubtasks();
            for (Subtask subtask : subtasks) {
                sb.append(subtask.toString()).append("\n");
            }
            sb.append("\n");

            List<Task> taskHistory = getHistory();
            for (Task history : taskHistory) {
                sb.append(history.getId()).append(',');
            }
            fileWriter.write(sb.deleteCharAt(sb.length() - 1).toString());

        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось сохранить файл");
        }
    }

    public static FileBackedTasksManager loadFromFile(File file) throws ManagerLoadException {
        FileBackedTasksManager fileBacked = new FileBackedTasksManager(file);

        try (Reader fileReader = new FileReader(file, StandardCharsets.UTF_8);
             BufferedReader br = new BufferedReader(fileReader)) {
            br.readLine();
            String readLine;
            String[] line;
            int id;
            while (!(readLine = br.readLine()).isBlank()) {
                line = readLine.split(",");
                id = Integer.parseInt(line[0]);
                String type = line[1];
                String title = line[2];

                switch (TaskType.valueOf(type)) {
                    case TASK:
                        tasks.put(id, new Task(id, title, line[3], Status.valueOf(line[4])));
                        break;
                    case EPIC:
                        epics.put(id, new Epic(id, title, Status.valueOf(line[3])));
                        break;
                    case SUBTASK:
                        int idOfEpic = Integer.parseInt(line[5]);
                        subtasks.put(id, new Subtask(id, title, line[3], Status.valueOf(line[4]), idOfEpic));
                        List<Epic> epics = fileBacked.getEpics();
                        for (Epic epic : epics) {
                            epic.getEpicSubtasks().add(id);
                        }
                        break;
                    default:
                        break;
                }
            }

            String[] history = null;
            while (br.ready()) {
                readLine = br.readLine();
                history = readLine.split(",");
            }
            if (history != null) {
                for (String s : history) {
                    int number = Integer.parseInt(s);
                    taskHistory.add(fileBacked.getTaskById(number));
                    taskHistory.add(fileBacked.getEpicById(number));
                    taskHistory.add(fileBacked.getSubtaskById(number));
                }
            }

        } catch (IOException e) {
            throw new ManagerLoadException("Ошибка загрузки файла");
        }
        return fileBacked;
    }

    @Override
    public void deleteAllTasks() throws ManagerSaveException {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllSubtasks() throws ManagerSaveException {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public void deleteAllEpics() throws ManagerSaveException {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void addTask(Task task) throws ManagerSaveException {
        super.addTask(task);
        save();
    }

    @Override
    public void addSubtask(Subtask subtask) throws ManagerSaveException {
        super.addSubtask(subtask);
        save();
    }

    @Override
    public void addEpic(Epic epic) throws ManagerSaveException {
        super.addEpic(epic);
        save();
    }

    @Override
    public void updateTask(Task task) throws ManagerSaveException {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) throws ManagerSaveException {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void updateEpic(Epic epic) throws ManagerSaveException {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void deleteTaskById(int id) throws ManagerSaveException {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteSubtaskById(int id) throws ManagerSaveException {
        super.deleteSubtaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(int id) throws ManagerSaveException {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public Task getTaskById(int id) throws ManagerSaveException {
        Task newTaskById = super.getTaskById(id);
        save();
        return newTaskById;
    }

    @Override
    public Subtask getSubtaskById(int id) throws ManagerSaveException {
        Subtask newSubtaskById = super.getSubtaskById(id);
        save();
        return newSubtaskById;
    }

    @Override
    public Epic getEpicById(int id) throws ManagerSaveException {
        Epic newEpicById = super.getEpicById(id);
        save();
        return newEpicById;
    }

    public static void main (String[] args) {
        File file = new File("src/ru/yandex/practicum/resources/tasks.csv");

        FileBackedTasksManager fileWriteMNGR = new FileBackedTasksManager(file);
        //FileBackedTasksManager fileReadMNGR = new FileBackedTasksManager(file);

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


        FileBackedTasksManager fileReadMNGR = FileBackedTasksManager.loadFromFile(file);

        // А принты почему убрать надо? Пока оставлю, до ответа))
        // Просто мне кажется, что я чего-то не знаю)
        System.out.println("************************************************");
        for (Task task : fileReadMNGR.getTasks()) {
            System.out.println(task);
        }
        for (Epic epic : fileReadMNGR.getEpics()) {
            System.out.println(epic);
        }
        for (Subtask subtask : fileReadMNGR.getSubtasks()) {
            System.out.println(subtask);
        }
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        for (Task task : fileReadMNGR.getHistory()) {
            System.out.println(task);
        }
    }
}



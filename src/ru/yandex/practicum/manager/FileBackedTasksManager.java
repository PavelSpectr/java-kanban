package ru.yandex.practicum.manager;

import ru.yandex.practicum.exceptions.ManagerLoadException;
import ru.yandex.practicum.exceptions.ManagerSaveException;
import ru.yandex.practicum.tasks.Epic;
import ru.yandex.practicum.tasks.Status;
import ru.yandex.practicum.tasks.Subtask;
import ru.yandex.practicum.tasks.Task;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager { // Убрал имплементацию
/*
Доброго времени!)
По порядку:
1) loadFromFile работает - но, мне не удалось привязать к epics список с id сабтасков, как я не старался.
Сделал, конструктор файл, чтобы все работало через new File, хотя не совсем понимаю это требование,
потому что метод createFileChecker, на мой взгляд, гораздо практичнее. Но я могу ошибаться, тем более,
что есть указание в ТЗ, но не все что есть в ТЗ поддается логике.))
2) Не совсем понимаю, как реализовать метод toString и таким образом сократить "достаточно много лишних строк кода".)
Просто вынести все в отдельный метод? Ctrl+X -> Ctrl+V ))
3) С исключениями разобрался.
По проверке вроде все GOOD.

*/
    private final File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
    }


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

    public void save() throws ManagerSaveException {
        try (Writer fileWriter = new FileWriter(file, StandardCharsets.UTF_8, false)) {
            StringBuilder sb = new StringBuilder("ID,Type,Title,Description,Status,EpicID\n");

            List<Task> tasks = getTasks(); // Теперь без super и вдвойне вкусней
            for (Task task : tasks) {
                sb.append(task.getId()).append(',');
                sb.append(TaskType.TASK).append(',');
                sb.append(task.getTitle()).append(',');
                sb.append(task.getDescription()).append(',');
                sb.append(task.getStatus()).append("\n");
            }

            List<Epic> epics = getEpics();
            for (Epic epic : epics) {
                sb.append(epic.getId()).append(',');
                sb.append(TaskType.EPIC).append(',');
                sb.append(epic.getTitle()).append(',');
                sb.append(epic.getStatus()).append(',');
                sb.append(epic.getEpicSubtasks()).append("\n");
            }

            List<Subtask> subtasks = getSubtasks();
            for (Subtask subtask : subtasks) {
                sb.append(subtask.getId()).append(',');
                sb.append(TaskType.SUBTASK).append(',');
                sb.append(subtask.getTitle()).append(',');
                sb.append(subtask.getDescription()).append(',');
                sb.append(subtask.getStatus()).append(',');
                sb.append(subtask.getEpicId()).append("\n");
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

    public void loadFromFile() throws ManagerLoadException {

        try (Reader fileReader = new FileReader(file, StandardCharsets.UTF_8);
             BufferedReader br = new BufferedReader(fileReader)) {
            br.readLine();
            String readLine;
            String[] line;
            int id;
            while (!(readLine = br.readLine()).isBlank()) {
                line = readLine.split(",");
                id = Integer.parseInt(line[0]);

                switch (TaskType.valueOf(line[1])) {
                    case TASK:
                        tasks.put(id, new Task(line[2], line[3], id, Status.valueOf(line[4])));
                        break;
                    case EPIC:
                        //По идее, сюда нужно добавить еще ArrayList, но не вышло, как я не морочился.
                        //В ТЗ этого нет, но по логике он должен быть, если мы делаем все в точности, как было.)
                        epics.put(id, new Epic(line[2], id, Status.valueOf(line[3])));
                        break;
                    case SUBTASK:
                        subtasks.put(id, new Subtask(line[2], line[3], id, Status.valueOf(line[4]), Integer.parseInt(line[5])));
                        break;
                    default:
                        break;
                }

                if(tasks.containsKey(id)) {
                    System.out.println(tasks.get(id));
                } else if (epics.containsKey(id)) {
                    System.out.println(epics.get(id));
                } else if (subtasks.containsKey(id)) {
                    System.out.println(subtasks.get(id));
                } else {
                    System.out.println("Веселья приносит и чувств бодрящих - шквал BugReport'ов такой бесящий");
                }
            }
            System.out.println("------------------------------------------------");
            String[] history = null;
            while (br.ready()) {
                readLine = br.readLine();
                history = readLine.split(",");
            }
            if (history != null) {
                for (String s : history) {
                    int n = Integer.parseInt(s);
                    if(tasks.containsKey(n)) {
                        System.out.println(tasks.get(n));
                    } else if (epics.containsKey(n)) {
                        System.out.println(epics.get(n));
                    } else if (subtasks.containsKey(n)) {
                        System.out.println(subtasks.get(n));
                    } else {
                        System.out.println("Арабская но-о-о-очь, от кода восто-о-о-орг!... Но еще не совсем xD");
                    }
                    taskHistory.add(getTaskById(n));
                    taskHistory.add(getEpicById(n));
                    taskHistory.add(getSubtaskById(n));
                }
            }
        } catch (IOException e) {
            throw new ManagerLoadException("Ошибка загрузки файла");
        }
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

    public static void main (String[] args) throws IOException {
        FileBackedTasksManager fileWriteMNGR = new FileBackedTasksManager(new File("src/ru/yandex/practicum/resources/tasks.csv"));
        FileBackedTasksManager fileReadMNGR = new FileBackedTasksManager(new File("src/ru/yandex/practicum/resources/tasks.csv"));
        //fileWriteMNGR.createFileChecker();

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
/*
Спросил друга как дела, получил такой ответ:
У меня все ок. Спасибо!
Курсы.Learn(English)
Дом.Learn(Java)

Ну и иногда
Улица.goForAWalk()
Или
Дом.Relax(Beer), Дом.Relax(TV)...

Надо бы конечно
Дом.Learn(English), но у меня часто выскакивает EnglishDisgustException, пока не смог победить...

А в остальном
Do
System.out.println(Life.Day++)
While Life.Day < Life.Day.MaxValue
*/



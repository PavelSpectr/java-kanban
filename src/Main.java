import logic.Managers;
import logic.TaskManager;
import logic.TaskStatus;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

public class Main {
    public static void main(String[] args) {

        TaskManager manager = Managers.getDefault();

        Task task1 = new Task(10, "Задача №1", "Описание задачи 1");
        manager.taskCreator(task1);
        Task task2 = new Task(20, "Задача №2", "Описание задачи 2");
        task2.setStatus(TaskStatus.IN_PROGRESS); // Тест изменения статуса
        manager.taskCreator(task2);

        Epic epic1 = new Epic(100, "Эпик №1", "С тремя подзадачами");
        manager.epicCreator(epic1);

        Subtask subtask1 = new Subtask("Подзадача № 1", "Описание подзадачи 1", epic1); //1
        manager.subtaskCreator(subtask1);
        Subtask subtask2 = new Subtask("Подзадача № 2", "Описание подзадачи 2", epic1); //2
        manager.subtaskCreator(subtask2);
        Subtask subtask3 = new Subtask("Подзадача № 3", "Описание подзадачи 3", epic1); //3
        manager.subtaskCreator(subtask3);

        Epic epic2 = new Epic(200, "Эпик №2", "Без подзадач"); //7
        manager.epicCreator(epic2);

        //Обращение к задачам
        System.out.println("\n----------Первое обращение к задачам (10,20,100,200,1,2,3):");
        manager.getTaskById(10);
        manager.getTaskById(20);
        manager.getEpicById(100);
        manager.getEpicById(200);
        manager.getSubtaskById(1);
        manager.getSubtaskById(2);
        manager.getSubtaskById(3);

        System.out.println("Список обращений к задачам:");
        for (Task taskFor : manager.history())
            System.out.println("#" + taskFor.getId() + " - " + taskFor.getTitle() + " " + taskFor.getDescription() + " (" + taskFor.getStatus() + ")");

        System.out.println("\n----------Второе обращение к задачам (20,10,200,100,3,2,1):");
        manager.getTaskById(20);
        manager.getTaskById(10);
        manager.getEpicById(200);
        manager.getEpicById(100);
        manager.getSubtaskById(3);
        manager.getSubtaskById(2);
        manager.getSubtaskById(1);

        System.out.println("Список обращений к задачам:");
        for (Task taskFor : manager.history())
            System.out.println("#" + taskFor.getId() + " - " + taskFor.getTitle() + " " + taskFor.getDescription() + " (" + taskFor.getStatus() + ")");

        System.out.println();
        System.out.println("Удалим задачу #1, которая есть в истории.");
        manager.deleteTaskById(10);
        System.out.println("Проверим не осталась ли она в истории: ");
        for (Task taskFor : manager.history())
            System.out.println("#" + taskFor.getId() + " - " + taskFor.getTitle() + " " + taskFor.getDescription() + " (" + taskFor.getStatus() + ")");

        System.out.println();
        System.out.println("Удалим эпик с тремя подзадачами: ");
        manager.deleteEpicById(100); // При удалении Эпика, удалились и Подзадачи к нему

        System.out.println("Список обращений к задачам после удаления Эпика #1000:");
        for (Task taskFor : manager.history())
            System.out.println("#" + taskFor.getId() + " - " + taskFor.getTitle() + " " + taskFor.getDescription() + " (" + taskFor.getStatus() + ")");
    }
}
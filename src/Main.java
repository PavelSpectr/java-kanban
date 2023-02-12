import ru.yandex.practicum.manager.*;
import ru.yandex.practicum.tasks.Epic;
import ru.yandex.practicum.tasks.Subtask;
import ru.yandex.practicum.tasks.Task;

public class Main {

    public static void main(String[] args) {
        TaskManager mngr = Managers.getDefault();
        HistoryManager taskHistory = Managers.getDefaultHistory();

        Task task;
        Subtask subtask;
        Epic epic;

        mngr.addTask(new Task("Смокинг", "Забрать из химчистки"));
        mngr.addTask(new Task("Стрижка", "Заехать к парикмахеру"));

        mngr.addEpic(new Epic("Поезка на море"));
        mngr.addSubtask(new Subtask("Обменять валюту", "Нужны доллары и местная валюта", 3));
        mngr.addSubtask(new Subtask("Определиться с котом", "Сдать полосатика в котдом или родне", 3));

        mngr.addEpic(new Epic("Собрать студию"));
        mngr.addSubtask(new Subtask("Электрика и звукоизоляция", "Сначала смонтировать всю электрику" +
                " и освещение, только потом делать аккустическое пространство",6));

        System.out.println(mngr.getTasks());
        System.out.println(mngr.getTaskById(2));
        System.out.println(mngr.getEpics());
        System.out.println(mngr.getEpicById(6));
        System.out.println(mngr.getSubtasks());
        System.out.println(mngr.getSubtaskById(5));

        task = mngr.getTaskById(1);
        task.setStatus(Status.DONE);
        mngr.updateTask(task);
        System.out.println(mngr.getTasks());

        subtask = mngr.getSubtaskById(4);
        subtask.setStatus(Status.DONE);
        mngr.updateSubtask(subtask);
        subtask = mngr.getSubtaskById(5);
        subtask.setStatus(Status.DONE);
        mngr.updateSubtask(subtask);

        System.out.println(mngr.getEpicById(6));

        mngr.deleteEpicById(6);

        subtask = mngr.getSubtaskById(4);
        subtask.setStatus(Status.IN_PROGRESS);
        mngr.updateSubtask(subtask);
        subtask = mngr.getSubtaskById(5);
        subtask.setStatus(Status.DONE);
        mngr.updateSubtask(subtask);

        System.out.println(mngr.getEpics());
        System.out.println(mngr.getSubtasks());

        mngr.deleteAllTasks();
        mngr.deleteAllEpics();
        mngr.deleteAllSubtasks();
        System.out.println(mngr.getTasks());
        System.out.println(mngr.getSubtasks());
        System.out.println(mngr.getEpics());



        taskHistory.printHistory();
    }
}

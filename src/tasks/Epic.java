package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private ArrayList<Subtask> subtaskIdList = new ArrayList<>();;
    private LocalDateTime endTime; //Окончание последней задачи

    //Конструктор
    public Epic(Integer id, String title, String descriptions) {
        super(id, title, descriptions, TaskType.EPIC);
    }

    public Epic(String title, String descriptions) {
        super(title, descriptions, TaskType.EPIC);
    }

    public ArrayList<Subtask> getSubtaskIdList() {
        return subtaskIdList;
    }

    public void setSubtaskIdList(ArrayList<Subtask> subtaskIdList) {
        this.subtaskIdList = subtaskIdList;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {

        return super.toString();
    }

}
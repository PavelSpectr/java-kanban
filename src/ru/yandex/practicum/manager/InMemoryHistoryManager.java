package ru.yandex.practicum.manager;

import ru.yandex.practicum.tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    //private final List<Task> taskHistory = new LinkedList<>(); //Удаляем не используемый список (пока так оставлю, чтобы наглядно было)
    private final Map<Integer, Node> nodeMap = new HashMap<>(); //Благодарю, правда я ужасно запутался поначалу)

    private Node head;
    private Node tail;
    //private int size = 0; // Удаляем поле размера за ненадобностью

    public void linkLast(Task data) { //
        int id = data.getId();

        if (nodeMap.containsKey(id)) {
            removeNode(nodeMap.get(id)); //Удалил закомментированный метод
        }

        //final Node oldNode = tail;
        final Node newNode = new Node(tail, data, null); //Сделал) Сейчас смотрю на этот код и понимаю, что писал его войдя в "поток")
        //tail = newNode; //Сейчас смотрю на этот код и понимаю, что писал его войдя в поток)
        nodeMap.put(id, newNode);
        if (tail == null) {
            head = newNode;
        } else {
            tail.next = newNode;
        }
    }

    private void removeNode(Node node) {
        Node prevNode = node.prev;
        Node nextNode = node.next;

        if (prevNode == null && nextNode == null) {
            head = null;
            tail = null;
        } else if (prevNode == null) { //Была такая мысль, но почему-то решил, что и так должно нормально сработать)
            head = nextNode;
            head.prev = null;
        } else if (nextNode == null) {
            tail = prevNode;
            tail.next = null;
        } else {
            prevNode.next = nextNode;
            nextNode.prev = prevNode;
        }
        nodeMap.remove(node.data.getId());
    }


    @Override
    public void add(Task task) {
        linkLast(task);
    }

    @Override
    public void remove(int id) {
        nodeMap.remove(id);
    }

    @Override
    public List<Task> getHistory() { //Благодарю) Тут тоже можно через head сразу все было сделать
        List<Task> taskHistory = new ArrayList<>(); //Но у меня каша с переменными случилась уже)
        Node tempNode = head;
        while (tempNode != null) {
            taskHistory.add(tempNode.data);
            tempNode = tempNode.next;
        }
        return taskHistory;
    }

/*    @Override
    public void removeFromHistory(int id) { //Удаляю метод удаления
        if(nodeMap.containsKey(id)) {
            removeNode(nodeMap.get(id));
        }
    }*/
}

class Node { //Сделал форматирование - спасибо за подсказку) Класс стандартный)
    public Task data; //Правда я сначала сделал с дженериками, но из-за этого у меня что-то не работало
    public Node next; //В обсуждениях увидел, что лучше без дженериков делать
    public Node prev;

    public Node(Node prev, Task data, Node next) {
        this.data = data;
        this.next = next;
        this.prev = prev;
    }
}


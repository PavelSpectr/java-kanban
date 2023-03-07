package ru.yandex.practicum.manager;

import ru.yandex.practicum.tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Integer, Node> nodeMap = new HashMap<>();

    private Node head;
    private Node tail;

    public void linkLast(Task data) { //
        removeNode(nodeMap.get(data.getId())); // Теперь метод удаления ноды будет выполняться здесь

        final Node newNode = new Node(tail, data, null);
        nodeMap.put(data.getId(), newNode);
        if (tail == null) {
            head = newNode;
        } else {
            tail.next = newNode;
        }
    }

    private void removeNode(Node node) {
        if (nodeMap.containsKey(node.data.getId())) { // перенес удаление ноды
            removeNode(nodeMap.get(node.data.getId()));
        }

        Node prevNode = node.prev;
        Node nextNode = node.next;

        if (prevNode == null && nextNode == null) {
            head = null;
            tail = null;
        } else if (prevNode == null) {
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
        if (task != null) { //Добавил проверку ан null
            linkLast(task);
        }
    }

    @Override
    public void remove(int id) {
        nodeMap.remove(id);
    }

    @Override
    public List<Task> getHistory() {
        List<Task> taskHistory = new ArrayList<>();
        Node tempNode = head;
        while (tempNode != null) {
            taskHistory.add(tempNode.data);
            tempNode = tempNode.next;
        }
        return taskHistory;
    }
}

class Node {
    public Task data;
    public Node next;
    public Node prev;

    public Node(Node prev, Task data, Node next) {
        this.data = data;
        this.next = next;
        this.prev = prev;
    }
}


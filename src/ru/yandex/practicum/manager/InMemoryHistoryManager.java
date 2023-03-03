package ru.yandex.practicum.manager;

import ru.yandex.practicum.tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private final List<Task> taskHistory = new LinkedList<>();
    private final Map<Integer, Node> nodeMap = new HashMap<>();

    private Node head;
    private Node tail;
    private int size = 0;

    public void linkLast(Task data) {
        int id = data.getId();

        if(nodeMap.containsKey(id)) {
            //nodeMap.remove(id);
            removeNode(nodeMap.get(id));
        }

        final Node oldNode = tail;
        final Node newNode = new Node(oldNode, data, null);
        tail = newNode;
        nodeMap.put(id, newNode);
        if(oldNode == null) {
            head = newNode;
        } else {
            oldNode.next = newNode;
        }
    }

    private void removeNode(Node node) {
        Node prevNode = node.prev;
        Node nextNode = node.next;

        if (prevNode == null  && nextNode == null) {
            head = null;
            tail = null;
        } else if (prevNode == null) {
            head = nextNode;
        } else if (nextNode == null) {
            tail = prevNode;
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
    public List<Task> getHistory() {
        List<Task> taskHistory = new ArrayList<>();
        Node tempNode = head;
        while (tempNode != null) {
            taskHistory.add(tempNode.data);
            tempNode = tempNode.next;
        }
        return taskHistory;
    }

    @Override
    public void removeFromHistory(int id) {
        if(nodeMap.containsKey(id)) {
            removeNode(nodeMap.get(id));
        }
    }

/*    @Override
    public void printHistory() {
        for (Task task : getHistory()) {
            System.out.println(task);
        }
    }*/
}

class Node{
    public Task data;
    public Node next;
    public Node prev;

    public Node(Node prev, Task data, Node next) {
        this.data = data;
        this.next = next;
        this.prev = prev;
    }
}


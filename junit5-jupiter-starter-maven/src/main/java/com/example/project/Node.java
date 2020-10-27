package com.example.project;

public class Node {
    Integer data;
    Node link;

    public Node() {
    }

    public Node(Integer data, Node link) {
        this.data = data;
        this.link = link;
    }

    public Integer getData() {
        return data;
    }

    public void setData(Integer data) {
        this.data = data;
    }

    public Node getLink() {
        return link;
    }

    public void setLink(Node link) {
        this.link = link;
    }
}


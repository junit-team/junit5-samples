package com.example.project;

public class LinkList {

    Node head = null;

    public void createList(Integer num) {
        if (head == null) {
            Node node = new Node();
            node.setData(num);
            node.setLink(null);
            head=node;
        }
    }

    public void insertAtEnd(Integer num){

        Node p = head;
        while(p.getLink()!=null){
            p=p.getLink();
        }

        Node node = new Node();
        node.setData(num);
        node.setLink(null);
        p.setLink(node);
    }

    public int listLength(){
        int counter=0;
        Node p = head;
        if(p!=null){
            while(p.getLink()!=null){
                counter++;
                p=p.getLink();
            }
            counter++;
        }
        return counter;
    }
}

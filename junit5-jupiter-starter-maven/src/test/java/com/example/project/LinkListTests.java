package com.example.project;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TestLinkList {

    private static LinkList linkList= new LinkList();

    @BeforeAll
    public static void testCreateList(){
        linkList.createList(5);
        linkList.insertAtEnd(6);
        linkList.insertAtEnd(8);
    }

    @Test
    @DisplayName("LinkList Size = 3")
    public void testListSize(){
        Assertions.assertEquals(3,linkList.listLength());
    }
}

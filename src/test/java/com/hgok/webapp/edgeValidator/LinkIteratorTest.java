package com.hgok.webapp.edgeValidator;

import org.junit.jupiter.api.Test;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LinkIteratorTest {

    @Test
    void hasNext() {
        LinkIterator<Integer> linkIterator = new LinkIterator<>(initLinks());
        assertTrue(linkIterator.hasNext());
        linkIterator.next();
        assertTrue(linkIterator.hasNext());
        linkIterator.next();
        assertTrue(linkIterator.hasNext());
        linkIterator.next();
        assertFalse(linkIterator.hasNext());
    }

    @Test
    void next() {
        LinkIterator<Integer> linkIterator = new LinkIterator<>(initLinks());
        List<Integer> temp = iterateToListEnd(linkIterator);
        assertEquals(3,temp.size());
    }

    private List<Integer> iterateToListEnd(LinkIterator<Integer> linkIterator) {
        List<Integer> temp = new ArrayList<>();
        while (linkIterator.hasNext()){
            temp.add(linkIterator.next());
        }
        return temp;
    }

    @Test
    void current() {
        LinkIterator<Integer> linkIterator = new LinkIterator<>(initLinks());
        assertEquals(1, linkIterator.current());
        assertEquals(1, linkIterator.current());
        assertEquals(2, linkIterator.next());
        assertEquals(2, linkIterator.current());
    }

    @Test
    void hasPrevious() {
        LinkIterator<Integer> linkIterator = new LinkIterator<>(initLinks());
        assertFalse(linkIterator.hasPrevious());
        linkIterator.next();
        assertTrue(linkIterator.hasPrevious());
        linkIterator.previous();
        assertFalse(linkIterator.hasPrevious());
    }


    @Test
    void previous() {
        LinkIterator<Integer> linkIterator = new LinkIterator<>(initLinks());
        iterateToListEnd(linkIterator);
        assertEquals(3, linkIterator.previous());
        assertEquals(2, linkIterator.previous());
        assertEquals(1, linkIterator.previous());
    }



    private List<Integer> initLinks() {
        List<Integer> links = new ArrayList<>();
        links.add(1);
        links.add(2);
        links.add(3);
        links.add(4);
        return links;
    }
}
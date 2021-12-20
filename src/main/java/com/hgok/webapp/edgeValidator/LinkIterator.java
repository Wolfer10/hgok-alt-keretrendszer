package com.hgok.webapp.edgeValidator;

import lombok.Getter;
import lombok.Setter;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

@Getter
@Setter
public class LinkIterator<T> implements Iterator<T> {
    private List<T> list;
    private int size;
    private int currentPointer = 0;

    public LinkIterator(List<T> list) {
        this.list = list;
        this.size = list.size();
    }

    @Override
    public boolean hasNext() {
        return currentPointer < size - 1;
    }

    @Override
    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        currentPointer += 1;
        return list.get(currentPointer);
    }

    public T current(){
        return list.get(currentPointer);
    }

    public T previous(){
        if (!hasPrevious()) {
            throw new NoSuchElementException();
        }
        currentPointer -= 1;
        return list.get(currentPointer);
    }

    public boolean hasPrevious(){
        return currentPointer > 0;
    }
}


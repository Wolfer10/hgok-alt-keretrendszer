package com.hgok.webapp.validator;

import lombok.Getter;
import lombok.Setter;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

@Getter
public class LinkIterator<T> implements Iterator<T> {

    private final List<T> list;
    private final int size;
    @Setter
    private int currentPointer = 0;
    private int stepSize = 1;

    public void setStepSize(int stepSize){
        this.stepSize = stepSize > 0 ? stepSize : 1;
    }

    public LinkIterator(List<T> list) {
        this.list = list;
        this.size = list.size();
    }

    @Override
    public boolean hasNext() {
        return currentPointer + stepSize < size;
    }

    @Override
    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        currentPointer += stepSize;
        return list.get(currentPointer);
    }

    public T current(){
        return list.get(currentPointer);
    }

    public T previous(){
        if (!hasPrevious()) {
            throw new NoSuchElementException();
        }
        currentPointer -= stepSize;
        return list.get(currentPointer);
    }

    public boolean hasPrevious(){
        return currentPointer - stepSize >= 0;
    }
}


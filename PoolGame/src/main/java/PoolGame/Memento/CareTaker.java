package PoolGame.Memento;

import java.util.ArrayList;
import java.util.List;

/** The CareTaker class for safekeeping memento */
public class CareTaker {
    private final List<Memento> mementoList = new ArrayList<>();

    /**
     * Add state to the mementoList from Memento
     * @param state The `Memento` instance
     */
    public void add(Memento state) {
        mementoList.add(state);
    }

    /**
     * Get from mementoList
     * @param index the index of list
     * @return Memento
     */
    public Memento get(int index) {
        return mementoList.get(index);
    }

    /**
     * Get the length of mementoList
     * @return the number of size
     */
    public int getLength() {
        return mementoList.size();
    }
}

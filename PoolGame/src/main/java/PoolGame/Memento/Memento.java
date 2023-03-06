package PoolGame.Memento;

import java.util.ArrayList;

/** The Memento class for solving state */
public class Memento {
    private final ArrayList<String> state;

    /**
     * Initialize Memento using the state arraylist
     * @param state The arraylist store states
     */
    public Memento(ArrayList<String> state) {
        this.state = state;
    }

    /**
     * Get the state list
     * @return the arraylist state
     */
    public ArrayList<String> getState() {
        return state;
    }
}

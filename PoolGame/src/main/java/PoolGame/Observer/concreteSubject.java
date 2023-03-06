package PoolGame.Observer;

import PoolGame.Game;

/** The concreteSubject class for observer the score */
public class concreteSubject {
    private int score;
    private final Game game;

    /**
     * Initialise the concrete subject with the game
     * @param game The `Game` instance
     */
    public concreteSubject(Game game) {
        this.game = game;
    }

    /**
     * Get score
     * @return The score
     */
    public int getScore() {
        return score;
    }

    /**
     * Set score if ball fall into pockets
     * @param state The color of the ball
     */
    public void setScore(String state) {
//        System.out.println(state);
        score = game.getUU();
        if (state.equalsIgnoreCase("0xffffffff")) {             // white
            score = 0;
        } else if(state.equalsIgnoreCase("0xff0000ff")) {       // red
            score += 1;
        } else if(state.equalsIgnoreCase("0xffff00ff")) {       // yellow
            score += 2;
        } else if(state.equalsIgnoreCase("0x008000ff")) {       // green
            score += 3;
        } else if(state.equalsIgnoreCase("0xa52a2aff")) {       // brown
            score += 4;
        } else if(state.equalsIgnoreCase("0x0000ffff")) {       // blue
            score += 5;
        } else if(state.equalsIgnoreCase("0x800080ff")) {       // purple
            score += 6;
        } else if(state.equalsIgnoreCase("0x000000ff")) {       // black
            score += 7;
        } else if(state.equalsIgnoreCase("0xffa500ff")) {       // orange
            score += 8;
        }
        game.update(String.valueOf(score));
    }
}

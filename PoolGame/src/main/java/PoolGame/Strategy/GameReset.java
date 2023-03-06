package PoolGame.Strategy;

import PoolGame.Game;
import PoolGame.Items.Ball;

/** Resets game when the method of this instance is called */
public class GameReset implements BallPocketStrategy {
//    Subject subject = new Subject();
    public void fallIntoPocket(Game game, Ball ball) {
//        subject.setScore("reset");
        game.reset();
    }
}

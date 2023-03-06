package PoolGame.Items;

import PoolGame.Config.TableConfig;
import PoolGame.Drawable;
import PoolGame.Game;
import PoolGame.Items.Ball.BallType;
import PoolGame.Memento.CareTaker;
import PoolGame.Memento.Memento;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/** A pool table */
public class PoolTable implements Drawable {
    private long[] dim;
    private String colourName;
    private Color colour;
    private double friction;
    private Rectangle shape;
    private List<Ball> balls;
    private List<Pocket> pockets;
    private final CareTaker careTaker = new CareTaker();
    private ArrayList<String> state = new ArrayList<>();

    /**
     * Offset of pockets on the table.
     */
    public static final double POCKET_OFFSET = 5;

    /**
     * Build the pool table with the provided values
     *
     * @param colourName The colour of the table in String
     * @param friction   The friction of the table
     * @param dimX       The dimension of the table in the x-axis
     * @param dimY       The dimension of the table in the y-axis
     */
    public PoolTable(String colourName, double friction, long dimX, long dimY) {
        this.init(colourName, friction, dimX, dimY);
    }

    /**
     * Build the pool table using a `TableConfig` instance
     * @param config The `TableConfig` instance
     */
    public PoolTable(TableConfig config) {
        this.init(config.getColour(),
            config.getFriction(),
            config.getSizeConfig().getX(),
            config.getSizeConfig().getY());
    }

    private void init(String colourName, double friction, long dimX, long dimY) {
        this.colourName = colourName;
        this.colour = Color.valueOf(this.colourName);
        this.friction = friction;
        this.dim = new long[2];
        this.dim[0] = dimX;
        this.dim[1] = dimY;
        this.shape = new Rectangle(this.dim[0], this.dim[1], this.colour);
        this.balls = new LinkedList<>();
        this.pockets = new LinkedList<>();
        this.pockets.add(new Pocket(POCKET_OFFSET, POCKET_OFFSET));
        this.pockets.add(new Pocket(dimX / 2, POCKET_OFFSET));
        this.pockets.add(new Pocket(dimX - POCKET_OFFSET, POCKET_OFFSET));
        this.pockets.add(new Pocket(POCKET_OFFSET, dimY - POCKET_OFFSET));
        this.pockets.add(new Pocket(dimX / 2, dimY - POCKET_OFFSET));
        this.pockets.add(new Pocket(dimX - POCKET_OFFSET, dimY - POCKET_OFFSET));
    }

    /**
     * Save the current state when clicking "Save"
     * @param state The arraylist of states
     */
    public void setState(ArrayList<String> state) {
        if (state.size() > 0) {
            state.clear();
        }
        state.add(String.valueOf(Game.tt));
        state.add(String.valueOf(Game.uu));
        for (Ball b: balls) {
            state.add(String.valueOf(b.isDisabled()));
            state.add(String.valueOf(b.getXPos()));
            state.add(String.valueOf(b.getYPos()));
            state.add(String.valueOf(b.getFallCounter()));
        }
    }

    /**
     * Get the current state arraylist
     * @return The state arraylist
     */
    public ArrayList<String> getState() {
        return state;
    }

    /**
     * Save state to memento
     * @return new instance of Memento with state
     */
    public Memento saveStateToMemento() {
        return new Memento(state);
    }

    /**
     * Get state from memento
     * @param memento The `Memento` instance
     */
    public void getStateFromMemento(Memento memento) {
        state = memento.getState();
    }

    /**
     * Save state and keep memento
     */
    public void Save() {
        setState(state);
        careTaker.add(saveStateToMemento());
    }

    /**
     * Load the game when clicking "Load" using a `TableConfig` instance
     * @param game The `Game` instance
     */
    public void Load(Game game) {
        if (getState().size() > 0) {
            getStateFromMemento(careTaker.get(careTaker.getLength()-1));
            game.updateTime(String.valueOf(getState().get(0)));
            game.updateScore(String.valueOf(getState().get(1)));
            Game.uu = Integer.parseInt(getState().get(1));
            int n = 2;
            for (Ball b: balls) {
                b.setSave(Boolean.parseBoolean(getState().get(n)), Double.parseDouble(getState().get(n+1)), Double.parseDouble(getState().get(n+2)), Integer.parseInt(getState().get(n+3)));
                n += 4;
            }
        }

    }

    /**
     * Clean the same color balls when clicking "Cheat" using a `TableConfig` instance
     * @param g The `Game` instance
     */
    public void Cheat(Game g) {
        StringBuilder builder = new StringBuilder();
        ArrayList<String> dis = new ArrayList<>();
        for (Ball ball: balls) {
            if (builder.indexOf(","+ball.getColor()+",") > -1) {
                dis.add(ball.getColor());
            } else {
                builder.append(",").append(ball.getColor()).append(",");
            }
        }
        for (Ball ba: balls) {
            for (String s: dis) {
                if (ba.getColor().equals(s) && !ba.isDisabled()) {
                    ba.fallIntoPocket(g);
                }
            }
        }
    }

    /**
     * Get the x dimension of the table.
     * @return The dimension of the table for the x axis.
     */
    public long getDimX() {
        return this.dim[0];
    }

    /**
     * Get the y dimension of the table.
     * @return The dimension of the table for the y axis.
     */
    public long getDimY() {
        return this.dim[1];
    }

    /**
     * Get the friction of the table.
     * @return The friction value of the table.
     */
    public double getFriction() {
        return this.friction;
    }

    public Node getNode() {
        return this.shape;
    }

    /**
     * Add a ball onto the pool table
     * @param ball The ball to be added
     */
    public void addBall(Ball ball) {
        if (!this.balls.contains(ball)) {
            this.balls.add(ball);
        }
    }

    /**
     * Get all balls on table.
     * @return List of ball on the table.
     */
    public List<Ball> getBalls() {
        return this.balls;
    }

    /**
     * Set up table with the list of balls, which includes the CueBall.
     * @param balls The list of balls to be added to the table
     */
    public void setupBalls(List<Ball> balls) {
        for (Ball ball : balls) {
            // if (ball.getBallType() == Ball.BallType.CUEBALL) {
            //     this.setCueBall(ball);
            // }
            this.addBall(ball);
        }
    }

    /**
     * Add the table and the balls to the JavaFX group so they can be drawn.
     * @param groupChildren The list of `Node` obtained from the JavaFX Group.
     */
    @Override
    public void addToGroup(ObservableList<Node> groupChildren) {
        groupChildren.add(this.shape);
        for (Pocket pocket : this.pockets) {
            pocket.addToGroup(groupChildren);
        }
        for (Ball ball : this.balls) {
            ball.addToGroup(groupChildren);
        }
    }

    /**
     * Apply friction to all the balls
     */
    public void applyFrictionToBalls() {
        for (Ball ball : this.balls) {
            ball.applyFriction(this.getFriction());
        }
    }

    /**
     * Check if any of the balls is in a pocket and handle the ball in the 
     * pocket
     * @param game The instance of the game
     */
    public void checkPocket(Game game) {
        for (Pocket pocket : this.pockets) {
            for (Ball ball : this.balls) {
                if (ball.isDisabled()) {
                    continue;
                }
                Point2D ballCenter = new Point2D(ball.getXPos(), ball.getYPos());
                if (pocket.isInPocket(ballCenter)) {
                    ball.fallIntoPocket(game);
                }
            }
        }
    }

    /**
     * Handle the collision between the balls and table and between balls.
     */
    public void handleCollision() {
        Bounds tableBounds = this.shape.getBoundsInLocal();
        for (Ball ball : this.balls) {
            if (ball.isDisabled()) {
                continue;
            }
            Bounds ballBound = ball.getLocalBounds();
            if (!tableBounds.contains(ballBound)) {
                if (ballBound.getMaxX() >= tableBounds.getMaxX()) {
                    ball.setXVel(-ball.getXVel());
                    ball.setXPos(tableBounds.getMaxX() - ball.getRadius());
                } else if (ballBound.getMinX() <= tableBounds.getMinX()){
                    ball.setXVel(-ball.getXVel());
                    ball.setXPos(tableBounds.getMinX() + ball.getRadius());
                }
                if (ballBound.getMaxY() >= tableBounds.getMaxY()) {
                    ball.setYVel(-ball.getYVel());
                    ball.setYPos(tableBounds.getMaxY() - ball.getRadius());
                } else if (ballBound.getMinY() <= tableBounds.getMinY()) {
                    ball.setYVel(-ball.getYVel());
                    ball.setYPos(tableBounds.getMinY() + ball.getRadius());
                }
            }
            for (Ball ballB : this.balls) {
                if (ballB.isDisabled()) {
                    continue;
                }
                // if (ball.getBallType() == BallType.CUEBALL && ball.isColliding(ballB)) {
                //     System.out.printf("%f, %f, %s\n", ballB.getXVel(), ballB.getYVel(), ballB.isColliding(ball));
                // }
                if (ball.isColliding(ballB)) {
                    ball.handleCollision(ballB);
                }
            }
            
        }
    }

    /**
     * If all the balls has been disabled except the cue ball, the game has ended
     * and the player won.
     * @return The win status of the game.
     */
    public boolean hasWon() {
        boolean won = true;
        for (Ball ball : this.balls) {
            if (ball.getBallType() == BallType.CUEBALL) {
                continue;
            }
            if (!ball.isDisabled()) {
                won = false;
                break;
            }
        }
        return won;
    }

    /**
     * Reset the game.
     */
    public void reset() {
        for (Ball ball : this.balls) {
            ball.reset();
        }
    }
}

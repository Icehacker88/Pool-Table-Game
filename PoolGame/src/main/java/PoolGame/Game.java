package PoolGame;

import PoolGame.Builder.BallBuilderDirector;
import PoolGame.Config.BallConfig;
import PoolGame.Items.Ball;
import PoolGame.Items.PoolTable;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.text.Text;

import javax.swing.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/** The game class that runs the game */
public class Game {
    PoolTable table;
    private boolean shownWonText = false;
    /** The win text when player wins */
    public final Text winText = new Text(50, 50, "Win and Bye");
    private final Text levelText = new Text("Levels:");
    private final Text undoText = new Text("Undo:");
    private final Text time = new Text("Time:");
    private final Text scoreText = new Text("Score:");
    private final Text counter = new Text("0 : 00");
    private final Text scoreCounter = new Text("0");
    Timer timer;
    private int second;
    private int minute;
    private String dSecond;
    private String dMinute;
    DecimalFormat decimalFormatM = new DecimalFormat("0");
    DecimalFormat decimalFormatS = new DecimalFormat("00");
    /** A global variable for score */
    public static int uu;   // score
    /** A global variable for time */
    public static String tt;    // time

    /**
     * Initialise the game with the provided config
     * @param config The config parser to load the config from
     */
    public Game(ConfigReader config) {
        this.setup(config);
    }

    private void setup(ConfigReader config) {
        this.table = new PoolTable(config.getConfig().getTableConfig());
        List<BallConfig> ballsConf = config.getConfig().getBallsConfig().getBallConfigs();
        List<Ball> balls = new ArrayList<>();
        BallBuilderDirector builder = new BallBuilderDirector();
        builder.registerDefault();
        for (BallConfig ballConf: ballsConf) {
            Ball ball = builder.construct(ballConf);
            if (ball == null) {
                System.err.println("WARNING: Unknown ball, skipping...");
            } else {
                balls.add(ball);
            }
        }
        this.table.setupBalls(balls);
        this.time.setX(50);
        this.time.setY(450);
        this.counter.setX(90);
        this.counter.setY(450);
        this.scoreText.setX(50);
        this.scoreText.setY(500);
        this.scoreCounter.setX(90);
        this.scoreCounter.setY(500);
        this.levelText.setX(250);
        this.levelText.setY(465);
        this.undoText.setX(250);
        this.undoText.setY(565);
        this.winText.setVisible(false);
        this.winText.setX(table.getDimX() / 2);
        this.winText.setY(table.getDimY() / 2);
        nTimer();
        second = 0;
        minute = 0;
        timer.start();
    }

    /**
     * Update score and time
     * @param u The score from observer
     */
    public void update(String u) {
        uu = Integer.parseInt(u);
        if (uu == 0) {
            counter.setText("0 : 00");
            second = 0;
            minute = 0;
        }
        scoreCounter.setText(u);
    }

    /**
     * Update score
     * @param s The score
     */
    public void updateScore(String s) {
        scoreCounter.setText(s);
    }

    /**
     * Update time
     * @param t The time
     */
    public void updateTime(String t) {
        counter.setText(t);
        String[] temp = t.split(" : ");
        minute = Integer.parseInt(temp[0]);
        second = Integer.parseInt(temp[1]);
    }

    /**
     * Get global variable of score
     * @return The score
     */
    public int getUU() {
        return uu;
    }

    /**
     * Realize the time counter
     */
    public void nTimer() {
        timer = new Timer(1000, e -> {
            second++;
            dSecond = decimalFormatS.format(second);
            dMinute = decimalFormatM.format(minute);
            counter.setText(dMinute + " : " +  dSecond);
            if (second == 60) {
                second = 0;
                minute++;
                dSecond = decimalFormatS.format(second);
                dMinute = decimalFormatM.format(minute);
                counter.setText(dMinute + " : " + dSecond);
            }
            tt = String.valueOf(counter.getText());
        });
    }

    /**
     * Get the window dimension in the x-axis
     * @return The x-axis size of the window dimension
     */
    public double getWindowDimX() {
        return this.table.getDimX();
    }

    /**
     * Get the window dimension in the y-axis
     * @return The y-axis size of the window dimension
     */
    public double getWindowDimY() {
        return this.table.getDimY();
    }

    /**
     * Get the pool table associated with the game
     * @return The pool table instance of the game
     */
    public PoolTable getPoolTable() {
        return this.table;
    }

    /** Add all drawable object to the JavaFX group
     * @param root The JavaFX `Group` instance
    */
    public void addDrawables(Group root) {
        ObservableList<Node> groupChildren = root.getChildren();
        table.addToGroup(groupChildren);
        groupChildren.add(this.winText);
        groupChildren.add(this.levelText);
        groupChildren.add(this.undoText);
        groupChildren.add(this.time);
        groupChildren.add(this.scoreText);
        groupChildren.add(this.counter);
        groupChildren.add(this.scoreCounter);

    }

    /** Reset the game */
    public void reset() {
        this.winText.setVisible(false);
        this.shownWonText = false;
        this.table.reset();
    }

    /** Code to execute every tick. */
    public void tick() {
        if (table.hasWon() && !this.shownWonText) {
            System.out.println(this.winText.getText());
            this.winText.setVisible(true);
            this.shownWonText = true;
        }
        table.checkPocket(this);
        table.handleCollision();
        this.table.applyFrictionToBalls();
        for (Ball ball : this.table.getBalls()) {
            ball.move();
        }
    }
}

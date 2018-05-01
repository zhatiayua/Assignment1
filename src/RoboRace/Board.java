package RoboRace;

import COSC3P40.xml.*;
import java.awt.*;
import java.io.*;

public class Board implements XMLObject {

    private Factory factory;
    private int numberRobots;
    private Robot[] robots;

    public static Board read(Reader input) {
        XMLReader<Board> reader = new XMLReader<Board>();
        reader.setXMLSchema("board.xsd");
        reader.setXMLNodeConverter(new BoardReader());
        return reader.readXML(input);
    }

    public Board(Factory factory, int numberRobots, Robot[] robots) {
        this.factory = factory;
        this.numberRobots = numberRobots;
        this.robots = robots;
    }

    public Location getLocation(int x, int y) {
        return factory.getLocation(x, y);
    }

    public Location getLocation(Point p) {
        return factory.getLocation(p);
    }

    public Robot robotAt(int x, int y) {
        for (Robot robot : robots) {
            if (x == robot.getLocation().x && y == robot.getLocation().y) {
                return robot;
            }
        }
        return null;
    }

    public void step(EventCounter counter, EventList events, Robot robot, Direction direction) {
        Point location = robot.getLocation();
        int x = location.x;
        int y = location.y;
        switch (direction) {
            case North:
                y--;
                break;
            case South:
                y++;
                break;
            case West:
                x--;
                break;
            case East:
                x++;
                break;
        }
        if (factory.hasWall(location, direction)) {
            events.add(new BumpEvent(counter, location, direction));
            return;
        }
        if (x < 0 || y < 0 || x >= factory.getXSize() || y >= factory.getYSize() || getLocation(x, y).isPit()) {
            events.add(new DestroyedEvent(counter, x, y));
            robot.destroyed();
            return;
        }
        if (robotAt(x, y) != null) {
            // robot.move(direction);
            step(counter, events, robotAt(x, y), direction);
            if (robotAt(x, y) != null) {
                events.add(new BumpEvent(counter, location, direction));
            } else {
                events.add(new MoveEvent(counter, location, direction));
                robot.move(direction);
            }
            return;
        }
        events.add(new MoveEvent(counter, location, direction));
        robot.move(direction);
    }

    public void revitalize() {
        for (Robot robot : robots) {
            if (!robot.isAlive() && robotAt(0, robot.getStart()) == null) {
                robot.revitalize();
            }
        }
    }

    public String toXMLString() {
        String result = "<board>\n";
        result += "<robots number=\"" + numberRobots + "\">\n";
        for (Robot robot : robots) {
            result += robot.toXMLString() + "\n";
        }
        result += "</robots>\n";
        result += factory.toXMLString() + "\n";
        return result + "</board>";
    }

    public Dimension getDisplaySize() {
        return factory.getDisplaySize();
    }

    public void start() {
        factory.start();
    }

    public void update(long delta) {
        factory.update(delta);
        for (Robot robot : robots) {
            robot.update(delta);
        }
    }

    public void draw(Graphics graphics) {
        factory.draw(graphics);
        for (Robot robot : robots) {
            graphics.drawImage(robot.getImage(), robot.getScreenX(), robot.getScreenY(), null);
        }
    }

    public void waitOnRobots() {
        for (Robot robot : robots) {
            robot.waitOnRobot();
        }

    }

}

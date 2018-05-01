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
        Point point = robot.getLocation();
        Location location = getLocation(point);
        if (location.hasWall(direction)) {
            events.add(new BumpEvent(counter, point, direction));
            return;
        }
        int x, y;
        switch (direction) {
            case North:
                x = point.x;
                y = point.y - 1;
                break;
            case East:
                x = point.x + 1;
                y = point.y;
                break;
            case South:
                x = point.x;
                y = point.y + 1;
                break;
            case West:
                x = point.x - 1;
                y = point.y;
                break;
            default:
                x = point.x;
                y = point.y;
                break;
        }
        if (x < 0 || x >= factory.getXSize() || y < 0 || y >= factory.getYSize() || getLocation(x, y).isPit()) {
            return;
        }
        Robot siblingRobot = robotAt(x, y);
        if (siblingRobot != null) {
            step(counter, events, siblingRobot, direction);
        }
        else robot.setLocation(x, y);
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

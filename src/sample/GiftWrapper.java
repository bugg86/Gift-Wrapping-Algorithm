package sample;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.util.Random;

public class GiftWrapper {
    private Circle[] points = new Circle[100];  //Array stores the points/Circles.
    private Random rand = new Random();
    private Circle leftmost;
    private Line[] lines  = new Line[points.length];
    private int lineIndex = 0;

    public GiftWrapper() {
        for (int i = 0; i < points.length; i++) {
            points[i] = new Circle();
            points[i].setCenterX(rand.nextInt(501));
            points[i].setCenterY(rand.nextInt(501));
            points[i].setRadius(2);
            points[i].setFill(Color.WHITE);
        }
        findLeftMost();
        for (int i = 0; i < lines.length; i++) {
            lines[i] = new Line();
        }
    }

    //Method wraps up the gift of lots of Circles.
    public void wrapGift() {
        new Thread(() -> {
            boolean done = false;   //Variable keeps track of if the hull is done.
            Circle currentPoint = leftmost; //Initializes the variable that keeps track of the current point.
            Circle nextPoint = points[1];   //Initialize the variable that keeps track of the next point in the hull.
            int index = 2;
            Circle checking;

            //Create the line connecting the current point and the current next point in the hull
            createLine(currentPoint, nextPoint, lineIndex);
            drawFinalLine(lineIndex);
            lineIndex++;

            while(!done) {
                try {
                    //Update the point that needs to be checked and visualize a line connecting the points
                    checking = points[index];
                    createLine(currentPoint, checking, lineIndex);
                    drawNewLine();
                    Thread.sleep(50);

                    //Calculate the cross product of the two lines/vectors
                    double cross = cross(subtract(currentPoint, nextPoint), subtract(currentPoint, checking));

                    //Update the next point in the hull if it is outside of the current one
                    if(cross < 0) {
                        nextPoint = checking;

                        //Updates the visualization of the current next line on the hull.
                        createLine(currentPoint, nextPoint, lineIndex - 1);
                        drawFinalLine(lineIndex - 1);
                    }

                    index++;

                    if (index == points.length) {
                        lineIndex++;

                        //Checks if the algorithm has made it all the way back to the start
                        if (nextPoint == leftmost) {
                            done = true;

                            //Draw the final line
                            createLine(currentPoint, leftmost, lineIndex - 1);
                            drawFinalLine(lineIndex - 1);
                        }
                        else {
                            //System.out.println("Final line between: " + currentPoint.getCenterX() + ", " + currentPoint.getCenterY() + " and " + nextPoint.getCenterX() + ", " + nextPoint.getCenterY());

                            //Move to the next point on the edge
                            currentPoint = nextPoint;
                            nextPoint = leftmost;
                            index = 0;
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //Method finds the leftmost Circle in the array and moves it to the front of the array.
    public void findLeftMost() {
        Circle currentMin = points[0];
        int minIndex = 0;
        for (int i = 0; i < points.length; i++) {
            if (points[i].getCenterX() < currentMin.getCenterX()) {
                currentMin = points[i];
                minIndex = i;
            }
        }
        //Swap the first Circle in the array with the minimum Circle in the araray.
        Circle temp = points[0];
        points[0] = currentMin;
        points[minIndex] = temp;

        leftmost = currentMin;
    }

    //Method creates a Line.
    public void createLine(Circle a, Circle b, int index) {
        //Create the line based upon the points it is connecting
        //If statement determines where the line needs to be created in the array
        if (index == lineIndex - 1) {
            lines[lineIndex - 1].setStartX(a.getCenterX());
            lines[lineIndex - 1].setStartY(a.getCenterY());
            lines[lineIndex - 1].setEndX(b.getCenterX());
            lines[lineIndex - 1].setEndY(b.getCenterY());
        } else {
            lines[lineIndex].setStartX(a.getCenterX());
            lines[lineIndex].setStartY(a.getCenterY());
            lines[lineIndex].setEndX(b.getCenterX());
            lines[lineIndex].setEndY(b.getCenterY());
        }
    }

    //Method draws the lines
    public void drawFinalLine(int index) {
        //System.out.println("Drew a final line!");

        //If statement determines which line in the array needs to be visualized.
        if (index == lineIndex - 1) {
            //Visualize the line with color
            lines[index].setStroke(Color.GREEN);
            lines[index].setStrokeWidth(1);
            lines[index].setFill(Color.GREEN);
        } else {
            //Visualize the line with color
            lines[lineIndex].setStroke(Color.BLUE);
            lines[lineIndex].setStrokeWidth(1);
            lines[lineIndex].setFill(Color.BLUE);
        }
    }

    public void drawNewLine() {
        //System.out.println("Drew a new line!");

        //Visualize the line with color
        lines[lineIndex].setStroke(Color.RED);
        lines[lineIndex].setStrokeWidth(1);
        lines[lineIndex].setFill(Color.RED);
    }

    //Getter methods
    public Circle[] getPoints() { return points; }
    public int getPointsLength() { return points.length; }
    public Line[] getLines() { return lines; }

    //Method subtracts the vectors created from connecting Circles
    public Circle subtract(Circle a, Circle b) {
        Circle c = new Circle();
        c.setCenterX(a.getCenterX() - b.getCenterX());
        c.setCenterY(a.getCenterY() - b.getCenterY());
        return c;
    }

    //Method does the cross product of the two Circles/points.
    public double cross(Circle a, Circle b) {
        return (a.getCenterX() * b.getCenterY()) - (a.getCenterY() * b.getCenterX());
    }
}
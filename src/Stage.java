import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JLabel;

public class Stage {
    Grid grid;
    Actor train;
    Actor car;
    Actor boat;
    JLabel label1;

    public Stage() {
        grid = new Grid();
        train = new Train(grid.cellAtColRow(0, 0));
        car = new Car(grid.cellAtColRow(0, 15));
        boat = new Boat(grid.cellAtColRow(12, 9));
    }

    public void paint(Graphics g, Point mouseLoc) {
        grid.paint(g, mouseLoc);
        
        
        if (mouseLoc != null) {
            if (mouseLoc.x > 10 && mouseLoc.x < 730 && mouseLoc.y > 10 && mouseLoc.y < 730) {
                Point newPoint = divideCellRange(mouseLoc);
                System.out.println(newPoint);
                g.drawString("Type: " + grid.cells[newPoint.x][newPoint.y].getType(), 760, 40);
                g.drawString("Elevation: " + grid.cells[newPoint.x][newPoint.y].elevation, 760, 60);
            } else {
                g.drawString("<No Cell Selected>", 760, 40);
            }
        }
        train.paint(g);
        car.paint(g);
        boat.paint(g);
    }

    public Point divideCellRange(Point p) {
        if (p != null){
            return new Point((p.x-10)/35, (p.y-10)/35);
        } else {
            return null;
        }
    }
}

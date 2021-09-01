import java.awt.Graphics;
import java.awt.Point;
//import java.util.Map;
import java.util.Random;
import java.util.stream.IntStream;
//import java.awt.Color;

//import jdk.internal.jshell.tool.resources.l10n;

class Grid {
    Cell[][] cells = new Cell[20][20];

    public Grid(){
        for(int i = 0; i < cells.length; i++){
            for(int j = 0; j < cells[i].length; j++){
                cells[i][j] = new Cell(10+35*i,10+35*j, randomType());
            }
        }
    }
    public int randomType() {
        int[] colorPercentages = {10, 20, 40, 25, 5};

        int sum = IntStream.of(colorPercentages).sum();
        Random r = new Random();
        int randomChance = r.nextInt(sum);

        int total = 0;

        for (int i = 0; i < colorPercentages.length; i++) {
            total += colorPercentages[i];
            if (randomChance < total) {
                return i;
            }
        }

        return -1;

    }

    public void paint(Graphics g, Point mousePos){
        for(int i = 0; i < cells.length; i++){
            for(int j = 0; j < cells[i].length; j++){
                cells[i][j].paint(g, mousePos);
            }
        }
    }

    public Cell cellAtColRow(int c, int r) {
        return cells[c][r];
    }
}
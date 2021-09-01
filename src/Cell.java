import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Random;

class Cell extends Rectangle {
    static int size = 35;
    Color color;
    int type;
    int elevation;

    public Cell(int x, int y, int type){
        super(x, y, size, size);
        this.type = type;
        if (type != 4) {    //road
            this.elevation = randomElevation();
            this.color = mergeColorAndElevation();
        } else {
            this.elevation = 0;
            this.color = getColor(type);
        }
        
    }

    private int randomElevation() {
        return (new Random().nextInt(6500))-500;
    }

    private Color getColor(int i) {
        Color[] colors = {  new Color(122, 122, 122), 
                            new Color(0, 255, 0), 
                            new Color(0, 255, 0), 
                            new Color(0, 255, 255), 
                            new Color(140, 60, 20)};

        return colors[i];
    }

    public String getType() {
        String[] types = {"Road", "Water", "Grass", "Mountain", "Buildings"};
        return types[this.type];
    }

    private Color mergeColorAndElevation() {
        int rgbc = (int)((255.0/6500.0)*(this.elevation+500));
        return blend(getColor(this.type), new Color(rgbc, rgbc, rgbc, 255), 0.5);
    }

    public static Color blend (Color color1, Color color2, double ratio)
    {
        float r  = (float) ratio;
        float ir = (float) 1.0 - r;

        float rgb1[] = new float[3];
        float rgb2[] = new float[3];    

        color1.getColorComponents(rgb1);
        color2.getColorComponents(rgb2);    

        Color color = new Color(rgb1[0] * r + rgb2[0] * ir, 
                                rgb1[1] * r + rgb2[1] * ir, 
                                rgb1[2] * r + rgb2[2] * ir);
    
        return color;
    }

    void paint(Graphics g, Point mousePos){
        if(contains(mousePos)){
            g.setColor(Color.GRAY);
        } else {
            g.setColor(this.color);
        }
        g.fillRect(x,y,size,size);
        g.setColor(Color.BLACK);
        g.drawRect(x,y,size,size);
    }

    @Override
    public boolean contains(Point p){
        if (p != null){
            return(super.contains(p));
        } else {
            return false;
        }
    }
}
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Stage {
    Grid grid;
    List<Actor> actors;
    List<Cell> cellOverlay;
    Optional<Actor> actorInAction;

    enum State {ChoosingActor, SelectingNewLocation, CPUMoving}
    State currentState;

    public Stage() {
        grid = new Grid();
        actors = new ArrayList<Actor>();
        cellOverlay = new ArrayList<Cell>();
        actorInAction = Optional.empty();
        currentState = State.ChoosingActor;
    }

    public void paint(Graphics g, Point mouseLoc) {
        // do we have AI moves to make
        if (currentState == State.CPUMoving) {
            for(Actor a: actors) {
                if (!a.isTeamRed()) {
                    List<Cell> possibleLocs = getClearRadius(a.loc, a.moves);
                    int moveCPUChooses = (new Random()).nextInt(possibleLocs.size());
                    a.setLocation(possibleLocs.get(moveCPUChooses));
                }
            }
            currentState = State.ChoosingActor;
            for(Actor a: actors) {
                a.turns = 1;
            }
        }

        grid.paint(g, mouseLoc);
        grid.paintOverlay(g, cellOverlay, new Color(0f, 0f, 1f, 0.5f));

        for(Actor a: actors) {
            a.paint(g);
        }

        // where to draw text in the information area
        final int hTab = 10;
        final int blockVT = 35;
        final int margin = 21*blockVT;
        int yLoc = 20;

        // state display
        g.setColor(Color.DARK_GRAY);
        g.drawString(currentState.toString(), margin, yLoc);
        yLoc = yLoc + blockVT;
        Optional<Cell> underMouse = grid.cellAtPoint(mouseLoc);
        if(underMouse.isPresent()) {
            Cell hoverCell = underMouse.get();
            g.setColor(Color.DARK_GRAY);
            String coord = String.valueOf(hoverCell.col) + String.valueOf(hoverCell.col);
            g.drawString(coord, margin, yLoc);
        }

        // agent display
        final int vTab = 15;
        final int labelIndent = margin + hTab;
        final int valueIndent = margin + 3*blockVT;
        yLoc = yLoc + 2*blockVT;
        for(int i = 0; i < actors.size(); i++){
            Actor a = actors.get(i);
            yLoc = yLoc + 2*blockVT;
            g.drawString(a.getClass().toString(), margin, yLoc);
            g.drawString("location:", labelIndent, yLoc+vTab);
            g.drawString(Character.toString(a.loc.col) + Integer.toString(a.loc.row), valueIndent, yLoc+vTab);
            g.drawString("redness:", labelIndent, yLoc+2*vTab);
            g.drawString(Float.toString(a.redness), valueIndent, yLoc+2*vTab);
        }
    }

    public List<Cell> getClearRadius(Cell from, int size) {
        List<Cell> init = grid.getRadius(from, size);
        for(Actor a: actors) {
            init.remove(a.loc);
        }
        return init;
    }

    public void mouseClicked(int x, int y ){
        switch (currentState) {
            case ChoosingActor:
                actorInAction = Optional.empty();
                for (Actor a : actors) {
                    if (a.loc.contains(x, y) && a.isTeamRed() && a.turns > 0) {
                        cellOverlay = grid.getRadius(a.loc, a.moves);
                        actorInAction = Optional.of(a);
                        currentState = State.SelectingNewLocation;
                    }
                }
                break;
            case SelectingNewLocation:
                Optional<Cell> clicked = Optional.empty();
                for (Cell c : cellOverlay) {
                    if (c.contains(x, y)) {
                        clicked = Optional.of(c);
                    }
                }
                cellOverlay = new ArrayList<Cell>();
                if (clicked.isPresent() && actorInAction.isPresent()) {
                    actorInAction.get().setLocation(clicked.get());
                    actorInAction.get().turns--;
                    int redsWithMovesLeft = 0;
                    for(Actor a: actors){
                        if (a.isTeamRed() && a.turns > 0) {
                            redsWithMovesLeft++;
                        }
                    }
                    if (redsWithMovesLeft > 0) {
                        currentState = State.ChoosingActor;
                    } else {
                        currentState = State.CPUMoving;
                    }
                } 
                break;
            default:
                System.out.println(currentState);
                break;
        }

    }
}

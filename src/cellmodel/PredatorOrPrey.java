package cellmodel;

import java.awt.font.ShapeGraphicAttribute;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import javafx.scene.paint.Color;

public class PredatorOrPrey extends Rules {
  public static final Color WATER_COLOR = Color.BLUE;
  public static final Color FISH_COLOR = Color.GREEN;
  public static final Color SHARK_COLOR = Color.YELLOW;
  private static final int WATER = 0;
  private static final int FISH = 1;
  private static final int SHARK = 2;
  private static final Color[] STATE_COLORS = {WATER_COLOR, FISH_COLOR, SHARK_COLOR};
  private float fishBreed;
  private float sharkBreed;
  private float sharkDie;
  private HashSet<Cell> blacklist;


  /**
   * Initialize variables, get probability of various global parameters
   * @param setupParameters
   */
  public PredatorOrPrey(HashMap<String, String> setupParameters){
    fishBreed = Float.parseFloat(setupParameters.get("fishBreed"));
    sharkBreed = Float.parseFloat(setupParameters.get("sharkBreed"));
    sharkDie = Float.parseFloat(setupParameters.get("sharkDie"));
    blacklist = new HashSet<>();

  }
//change the state and put that cell in the previous cells spot

  @Override
  /**
   * Given a cell, change its state and color based on its current status & neighbor status
   * @param cell cell to be updated
   */
  public void changeState(Cell cell, Cell cloneCell) {
    if(blacklist.contains(cell)){
      blacklist.remove(cell);
    }
    else{
      int state = cell.getState();
      if(state == SHARK){
        sharkAct(cell);
      }
      else if(state == FISH){
        fishAct(cell);
      }
    }


  }

  private void fishAct(Cell fish) {
    if(fish.numNeighborsWithGivenState(WATER) > 0){
      determineMove(fish, fish.getNeighborsWithState(WATER));
    }
  }

  private void sharkAct(Cell shark){
    if(shark.numNeighborsWithGivenState(FISH) > 0){
      determineMove(shark, shark.getNeighborsWithState(FISH));
    }
    else if(shark.numNeighborsWithGivenState(WATER)>0){
      determineMove(shark, shark.getNeighborsWithState(WATER));
    }
  }

  private void determineMove(Cell mover, List<Cell> potentialCells){
    int random = getRandomIndex(potentialCells);
    moveIntoCell(mover, potentialCells.get(random));
  }


  private void moveIntoCell(Cell source, Cell target){
      if(source.getState() == SHARK){
        if(target.getState() == FISH){
          source.setMoves(0);
        }
        else{
          source.setMoves(source.getMoves()+1);
        }
      }
      if(target.getY() > source.getY()){
        blacklist.add(target);
      }
      else if(target.getY() == source.getY()){
        if(target.getX() > source.getX()){
          blacklist.add(target);
        }
      }
      target.changeStateAndView(source.getState(), STATE_COLORS[source.getState()]);
      target.setMoves(source.getMoves());
      target.setTurnsSinceStateChange(source.numberOfStateChanges()+1);
      source.changeStateAndView(WATER, STATE_COLORS[WATER]);
      source.setMoves(0);

      checkSharkDeath(target);
      checkSharkBirth(target);
      checkFishBirth(target);
  }

  private void checkFishBirth(Cell fish) {
    if(fish.getState() == FISH && fish.numberOfStateChanges() == fishBreed){
      createFish(fish);
      fish.setTurnsSinceStateChange(0);
    }
  }

  private void createFish(Cell fish) {
    Cell newfish = new Cell(FISH);
    newfish.setX(fish.getX());
    newfish.setY(fish.getY());
    newfish.setTurnsSinceStateChange(-1);
    if(fish.numNeighborsWithGivenState(WATER)>0) {
      determineMove(newfish, fish.getNeighborsWithState(WATER));
    }
  }

  private void checkSharkBirth(Cell shark) {
    if(shark.getState() == SHARK && shark.numberOfStateChanges() > sharkBreed){
      createShark(shark);
      shark.setTurnsSinceStateChange(0);
    }
  }

  private void createShark(Cell shark) {
    Cell newShark = new Cell(SHARK);
    newShark.setX(shark.getX());
    newShark.setY(shark.getY());
    newShark.setMoves(-1);
    newShark.setTurnsSinceStateChange(-1);
    if(shark.numNeighborsWithGivenState(WATER)>0) {
      determineMove(newShark, shark.getNeighborsWithState(WATER));
    }
  }

  private void checkSharkDeath(Cell shark) {
    if(shark.getState() == SHARK && shark.getMoves() > sharkDie){
      organismGone(shark);
    }
  }

  private void organismGone(Cell cell) {
    cell.changeStateAndView(WATER, STATE_COLORS[WATER]);
  }


  private int getRandomIndex(List<Cell> givenStateNeighbors) {
    int random = 0;
    if(givenStateNeighbors.size()!=1) {
      random = (int) (Math.random() * givenStateNeighbors.size());
    }
    return random;
  }



  @Override
  /**
   * returns whether or not a corner of a cell is a neighbor
   * @return true; in predator or prey, they are
   */
  public  boolean areCornersNeighbors(){
    return true;
  }

  @Override
  /**
   * gets the color for a cell that is created with a certain state
   * so that the board can be created
   * @param state
   * @return color of the state, or if it's not a valid state white
   */
  public Color getStateColor(int state){
    if(state >=0 && state <=3)
      return STATE_COLORS[state];
    else return Color.WHITE;
  }
}
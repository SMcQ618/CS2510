
import java.util.ArrayList;

import tester.*;
import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;

//to represent a valid cell
interface Cells {
  //to draw a Cells on a given scene 
  WorldScene drawAt(int row, int col, WorldScene scene);
  
  //to give values to a Cells
  void give(Cells up, Cells down, Cells left, Cells right);
  
  //get the color of a Cells, if it has one 
  Color getColor();
  
  //change color of a Cells, if it has one 
  void changeColor(Color color);
  
  //to check whether there is a path
  boolean check();
  
  //to determine whether it should continue to check 
  boolean continueCheck(Color col);
  
  //to change statue of a Cells 
  void visit();
  
}

//to represent an empty space 
class None implements Cells {

  @Override
  public WorldScene drawAt(int row, int col, WorldScene scene) {
    return scene;
  }

  @Override
  public void give(Cells up, Cells down, Cells left, Cells right) {
    //do not give value when the Cells is none 
  }

  @Override
  public Color getColor() {
    return Color.blue;
  }

  @Override
  public void changeColor(Color color) {
    //do nothing if the type is None
  }

  @Override
  public boolean check() {
    return true;
  }

  @Override
  public boolean continueCheck(Color col) {
    return true;
  }

  @Override
  public void visit() {
    //do nothing if the type is None
    
  }
  
}

//to represent a cell
class Cell implements Cells {
  Color col;
  Cells up;
  Cells down;
  Cells left;
  Cells right;
  boolean visited;
  
  
  //the constructor
  Cell(Color col) {
    this.col = col;
    this.visited = false;
  }
  
  //the constructor for testing 
  Cell(Color col, Cells up, Cells down, Cells left, Cells right) {
    this.col = col;
    this.up = up;
    this.down = down;
    this.left = left;
    this.right = right;
  }

  @Override
  public WorldScene drawAt(int row, int col, WorldScene scene) {
    scene.placeImageXY(new RectangleImage(50, 50, "solid", this.col)
        , (row - 1) * 50 + 25, (col - 1) * 50 + 25);
    return scene;
  }

  @Override
  public void give(Cells up, Cells down, Cells left, Cells right) {
    this.up = up;
    this.down = down;
    this.left = left;
    this.right = right;
  }

  @Override
  public Color getColor() {
    return this.col;
  }

  @Override
  public void changeColor(Color color) {
    this.col = color;
    
  }

  @Override
  public boolean check() {
    this.visited = true;
    boolean answer = false;
    if (this.up.continueCheck(this.col)) {
      answer = answer || this.up.check();
    }
    if (this.down.continueCheck(this.col)) {
      answer = answer || this.down.check();
    }
    if (this.left.continueCheck(this.col)) {
      answer = answer || this.left.check();
    }
    if (this.right.continueCheck(this.col)) {
      answer = answer || this.right.check();
    }
    this.visited = false;
    return answer;
  }

  @Override
  public boolean continueCheck(Color col) {
    return col.equals(this.col) && !this.visited;
  }

  @Override
  public void visit() {
    this.visited = true;
  }
}



//utility class 
class Utility {
  
  //to check whether the size input is valid 
  int valid(int input, String msg) {
    if (input >= 3 && input % 2 == 1) {
      return input;
    }
    else {
      throw new IllegalArgumentException(msg);
    }
  }
}

//to represent a Bridglt game 
class Bridglt extends World {
  int size;
  int clicktime;
  ArrayList<ArrayList<Cells>> cells;
  
  //the constructor for testing 
  Bridglt() {
    
  }
  
  //the constructor 
  Bridglt(int size) {
    this.size = size;
    this.clicktime = 0;
    int input = new Utility().valid(size, "should be grater than 3 and odd");
    this.setup(input);
  }
  
  // to assign each cell its neighbor 
  void assign() {
    for (int row = 1; row < this.size + 1; row++) {
      for (int col = 1; col < this.size + 1; col++) {
        Cells cell = this.cells.get(row).get(col);
        Cells upcell = this.cells.get(row - 1).get(col);
        Cells downcell = this.cells.get(row + 1).get(col);
        Cells leftcell = this.cells.get(row).get(col - 1);
        Cells rightcell = this.cells.get(row).get(col + 1);
        cell.give(upcell, downcell, leftcell, rightcell);
      }
    }
  }
  
  //to set up the scene 
  void setup(int input) {
    this.cells = new ArrayList<ArrayList<Cells>>();
    None none = new None();
    ArrayList<Cells> blank = new ArrayList<Cells>();
    
    for (int k = 0; k < input + 2; k++) {

      blank.add(none);
    }
    
    this.cells.add(blank);
    
    for (int i = 0; i < input; i++) {
      ArrayList<Cells> row = new ArrayList<Cells>();
      row.add(none);
      if (i % 2 == 0) {
        for (int j = 0; j < input; j++) {
          if (j % 2 == 0) {
            row.add(new Cell(Color.white));
          }
          else {
            row.add(new Cell(Color.pink));
          }
        }
      }
      else {
        for (int j = 0; j < input; j++) {
          if (j % 2 == 0) {
            row.add(new Cell(Color.magenta));
          }
          else {
            row.add(new Cell(Color.white));
          }
        }
      }
      row.add(none);
      this.cells.add(row);
    }
    
    this.cells.add(blank);
    this.assign();
  }

  @Override
  public WorldScene makeScene() {
    WorldScene scene = new WorldScene(50 * this.size, 50 * this.size);
    
    for (int row = 1; row < this.size + 1; row++) {
      for (int col = 1; col < this.size + 1; col++) {
        Cells cell = this.cells.get(row).get(col);
        cell.drawAt(row, col, scene);
      }
    }
    return scene;
  }
  
  //handle mouse click 
  public void onMouseClicked(Posn pos) {
    int col = Double.valueOf(pos.y / 50 + 1).intValue();
    int row = Double.valueOf(pos.x / 50 + 1).intValue();
    
    if (col != 1 && row != 1 && col != this.size && row != this.size) {
      if (this.cells.get(row).get(col).getColor().equals(Color.white)
          && this.clicktime % 2 == 0) {
        this.cells.get(row).get(col).changeColor(Color.magenta);
        this.clicktime++;
      }
      else if (this.cells.get(row).get(col).getColor().equals(Color.white)) {
        this.cells.get(row).get(col).changeColor(Color.pink);
        this.clicktime++;
      }
    }
    
    this.win();
  }
 
  
  // to check whether there is a existing path
  void win() {
    Bridglt copy = this;
    for (int i = 1; i < this.size + 1; i++) {
      copy.cells.get(i).get(1).visit();
      copy.cells.get(1).get(i).visit();
    }

    // check for a path in the game from top to bottom
    for (int col = 2; col < this.size; col += 2) {
      if (copy.cells.get(col).get(2).getColor().equals(Color.magenta)) {
        if (copy.cells.get(col).get(2).check()) {
          copy.endOfWorld("player 1 has won!");
        }
      }
    }

    // check for a path from the left to the right
    for (int row = 2; row < this.size; row += 2) {
      if (copy.cells.get(2).get(row).getColor().equals(Color.pink)) {
        if (copy.cells.get(2).get(row).check()) {
          copy.endOfWorld("player 2 has won!");
        }
      }
    }
  }
  
  
  // to make the final scene
  public WorldScene lastScene(String msg) {
    int length = this.size * 50;
    WorldScene winscene = new WorldScene(length, length);
    winscene.placeImageXY(new TextImage(msg, 30, Color.black), length / 2,
        length / 2);
    return winscene;
  }
}

class ExamplesBridglt {
  
  ExamplesBridglt() {}
  
  // create the world and start it
  void testGame(Tester t) {
    Bridglt g = new Bridglt(11);
    g.bigBang(50 * g.size, 50 * g.size);
  }
  
  Bridglt eg = new Bridglt(3);
  Cells none = new None();
  Cells c1 = new Cell(Color.white);
  Cells c2 = new Cell(Color.pink);
  Cells c3 = new Cell(Color.magenta);
  WorldScene bg = new WorldScene(150, 150);
  
  
  //to test DrawAt 
  void testDrawAt(Tester t) {
    t.checkExpect(none.drawAt(1, 1, bg), bg);
    bg.placeImageXY(new RectangleImage(50, 50, "solid", Color.white)
        , (1 - 1) * 50 + 25, (1 - 1) * 50 + 25);
    t.checkExpect(c1.drawAt(0, 0, bg)
        , bg);
    bg = new WorldScene(150, 150);
    bg.placeImageXY(new RectangleImage(50, 50, "solid", Color.pink)
        , (2 - 1) * 50 + 25, (2 - 1) * 50 + 25);
    t.checkExpect(c2.drawAt(2, 2, bg), bg);
  }
  
  //to test makeScene
  void testMakeScene(Tester t) {
    WorldScene scene = new WorldScene(50 * 3, 50 * 3);
    
    for (int row = 1; row < 3 + 1; row++) {
      for (int col = 1; col < 3 + 1; col++) {
        Cells cell = eg.cells.get(row).get(col);
        cell.drawAt(row, col, scene);
      }
    }
    t.checkExpect(eg.makeScene(), scene);
  }
  
  //to test setup
  void testSetup(Tester t) {
    Bridglt ex = new Bridglt();
    ex.size = 3;
    ex.setup(3);
    t.checkExpect(ex, eg);
  }
  
  
  
  //to test give 
  void testGive(Tester t) {
    Cells sp = new Cell(Color.white);
    Cells noneeg = new None();
    noneeg.give(c1, c1, c2, c3);
    t.checkExpect(noneeg, none);
    sp.give(c1, c1, c2, c3);
    t.checkExpect(sp, new Cell(Color.white, c1, c1, c2, c3));
    
  }
  
  //to test valid 
  void testValid(Tester t) {
    t.checkExpect(new Utility().valid(3, ""), 3);
    t.checkExpect(new Utility().valid(5, ""), 5);
  }
  
  //to test getColor method 
  void testGetColor(Tester t) {
    t.checkExpect(c1.getColor(), Color.white);
    t.checkExpect(c2.getColor(), Color.pink);
    t.checkExpect(c3.getColor(), Color.magenta);
  }
  
  //to test changeColor 
  void testChangeColor(Tester t) {
    Cell t1 = new Cell(Color.white);
    Cell t2 = new Cell(Color.blue);
    t1.changeColor(Color.blue);
    t.checkExpect(t1, t2);
  }
  
  //to test last scene 
  void testLastScene(Tester t) {
    WorldScene winscene = new WorldScene(150, 150);
    winscene.placeImageXY(new TextImage("msg", 30, Color.black), 75,
        75);
    t.checkExpect(eg.lastScene("msg"), winscene);
  }
  
}

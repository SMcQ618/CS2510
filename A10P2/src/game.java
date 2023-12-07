import java.util.ArrayList;
import java.util.Arrays;
import java.util.*;
import tester.*;
import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;

//to represent a valid cell
interface Cells {
  // to draw a Cells on a given scene
  WorldScene drawAt(int row, int col, WorldScene scene);

  // boolean equals(Cell obj);

  // EFFECT: to give values to a Cells
  void give(Cells up, Cells down, Cells left, Cells right);

  // check if the cell is valid
  boolean isValidCell();

  // get the up direction
  Cells getUp();

  // get the down direction
  Cells getDown();

  // get the right direction
  Cells getRight();

  // get the left direction
  Cells getLeft();

  // set the color of the cell
  void setColor(Color color);

  // gets the colr
  Color getColor();
}

//to represent an empty space 
class None implements Cells {
  // to draw a Cells on a given scene
  @Override
  public WorldScene drawAt(int row, int col, WorldScene scene) {
    return scene;
  }

  // EFFECT: to give values to a Cells
  @Override
  public void give(Cells up, Cells down, Cells left, Cells right) {
    // do not give value when the Cells is none
  }

  // check if the cell is valid
  public boolean isValidCell() {
    return false;
  }

  @Override
  public Cells getUp() {
    throw new UnsupportedOperationException("None has no neighbor");
  }

  @Override
  public Cells getDown() {
    throw new UnsupportedOperationException("None has no neighbor");
  }

  @Override
  public Cells getRight() {
    throw new UnsupportedOperationException("None has no neighbor");
  }

  @Override
  public Cells getLeft() {
    throw new UnsupportedOperationException("None has no neighbor");
  }

  @Override
  public void setColor(Color color) {
    // TODO Auto-generated method stub

  }

  @Override
  public Color getColor() {
    throw new UnsupportedOperationException("None has no color");
  }
}

//to represent a cell
class Cell implements Cells {
  Color col;
  Posn loc;
  Cells up;
  Cells down;
  Cells left;
  Cells right;

  // the constructor
  Cell(Color col) {
    this.col = col;
  }

  // the constructor for testing
  Cell(Color col, Cells up, Cells down, Cells left, Cells right) {
    this.col = col;
    this.up = up;
    this.down = down;
    this.left = left;
    this.right = right;
  }

  @Override
  public WorldScene drawAt(int row, int col, WorldScene scene) {
    int x = (col - 1) * 50 + 50 / 2;
    int y = (row - 1) * 50 + 50 / 2;

    scene.placeImageXY(new RectangleImage(50, 50, "solid", this.col), x, y);
    return scene;
  }

  @Override
  public void give(Cells up, Cells down, Cells left, Cells right) {
    this.up = up;
    this.down = down;
    this.left = left;
    this.right = right;
  }

  // to give values to a Cells
  public boolean isValidCell() {
    return this.col != null;
  }

  // check if the given posn is in teh same
  boolean isClicked(Posn pos) {
    return (pos.x > this.loc.x - 20) && (pos.x < this.loc.x + 20) && (pos.y > this.loc.y - 20)
        && (pos.y < this.loc.y + 20);
  }

  @Override
  public Cells getUp() {
    return this.up;
  }

  @Override
  public Cells getDown() {
    return this.down;
  }

  @Override
  public Cells getRight() {
    return this.right;
  }

  @Override
  public Cells getLeft() {
    return this.left;
  }

  // set color of cell
  public void setColor(Color color) {
    this.col = color;
  }

  @Override
  public Color getColor() {
    return this.col;
  }
}

//utility class 
class Utility {

  // to check whether the size input is valid
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
  ArrayList<ArrayList<Cells>> cells;
  Color currentPlayer;
  Color currentColor;

  // the constructor for testing
  Bridglt() {

  }

  // the constructor
  Bridglt(int size) {
    this.size = size;
    this.currentColor = Color.magenta;
    int input = new Utility().valid(size, "should be grater than 3 and odd");
    this.setup(input);
  }

  // EFFECT: to assign each cell its neighbor
  void assign() {
    ArrayList<ArrayList<Cells>> copy = new ArrayList<>();
    // iterate over the row
    for (ArrayList<Cells> row : this.cells) {
      // create a new array list
      ArrayList<Cells> newRow = new ArrayList<>();

      for (Cells cell : row) {
        if (cell instanceof None) {
          newRow.add(new None());
        }
        else if (cell instanceof Cell) {
          Color col = ((Cell) cell).col;
          newRow.add(new Cell(col));
        }
      }
      copy.add(newRow);
    }

    // iterate through the copy to set the neighbors
    for (int row = 1; row <= this.size - 1; row++) {
      for (int col = 1; col <= this.size - 1; col++) {
        Cells cell = copy.get(row).get(col);
        Cells upcell = copy.get(row - 1).get(col);
        Cells downcell = copy.get(row + 1).get(col);
        Cells leftcell = copy.get(row).get(col - 1);
        Cells rightcell = copy.get(row).get(col + 1);
        cell.give(upcell, downcell, leftcell, rightcell);
      }
    }

    // update the original cell with the copy
    this.cells = copy;
  }

  // EFFECT:to set up the scene
  void setup(int input) {
    this.cells = new ArrayList<ArrayList<Cells>>();
    Cell white = new Cell(Color.white);
    Cell pink = new Cell(Color.pink);
    Cell magenta = new Cell(Color.magenta);
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
            row.add(white);
          }
          else {
            row.add(pink);
          }
        }
      }
      else {
        for (int j = 0; j < input; j++) {
          if (j % 2 == 0) {
            row.add(magenta);
          }
          else {
            row.add(white);
          }
        }
      }
      row.add(none);
      this.cells.add(row);
    }

    this.cells.add(blank);
    this.assign();
  }

  // makes a scene for the world
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

  // EFFECT: changes the colors and expands the cells
  public void onMousePressed(Posn pos, String buttonName) {
    if (buttonName.equals("LeftButton")) {
      Posn cellPos = this.pixelToCell(pos);

      if (this.isValidCell(cellPos.x, cellPos.y)) {
        Cells clicked = this.cells.get(cellPos.x).get(cellPos.y);
        if (clicked instanceof Cell) {
          Color currentColor = clicked.getColor();
          clicked.setColor(this.getNextColor(currentColor));
        }
      }
    }
  }

  // helper method to get the next color in the sequence
  Color getNextColor(Color currentColor) {
    if (currentColor.equals(Color.white)) {
      return Color.pink;
    }
    else if (currentColor.equals(Color.pink)) {
      return Color.magenta;
    }
    else {
      return Color.white;
    }
  }

  // convert pixel location to cell
  public Posn pixelToCell(Posn pixelPos) {
    int cellRow = (pixelPos.y - 25) / 50 + 1;
    int cellCol = (pixelPos.x - 25) / 50 + 1;
    return new Posn(cellRow, cellCol);
  }

  // is the given cell within the bounds?
  boolean isValidCell(int row, int col) {
    return row >= 1 && row <= size && col >= 1 && col <= size;

  }

  // returns true if a plater has won
  boolean isGameWon() {
    // check for a path in the game from left to right
    for (int row = 1; row < size; row += 2) {
      if (hasPathHep(row, 1, 0, 1, new ArrayList<Posn>())) {
        return true;
      }
    }

    // check for a path from the bottom to the top
    for (int col = 1; col < size; col += 2) {
      if (hasPathHep(1, col, 1, 0, new ArrayList<Posn>())) {
        return true;
      }
    }
    return false;
  }

  // helper to get the path from start
  boolean hasPathHep(int startRow, int startCol, int drow, int dcol, ArrayList<Posn> visited) {
    Cells current = this.cells.get(startRow).get(startCol);

    // Check if the current position has already been visited
    Posn currentPosition = new Posn(startRow, startCol);
    if (visited.contains(currentPosition)) {
      return false;
    }

    // check is the cell is valid
    if (!current.isValidCell()) {
      return true;
    }

    // note the current position as visited
    visited.add(currentPosition);

    // check if we've reached an edge
    return hasPathHep(startRow + drow, startCol + dcol, drow, dcol, visited);
  }

  // return a list of neightboyrs for a given cell
  ArrayList<Posn> makeNeighbors(int row, int col) {
    ArrayList<Posn> neighbors = new ArrayList<>();

    // check the surrounding area
    if (row - 1 >= 1) {
      neighbors.add(new Posn(row - 1, col));
    }
    if (row + 1 <= size) {
      neighbors.add(new Posn(row + 1, col));
    }
    if (col - 1 >= 1) {
      neighbors.add(new Posn(row, col - 1));
    }
    if (col + 1 <= size) {
      neighbors.add(new Posn(row, col + 1));
    }

    return neighbors;
  }
}

class ExamplesBridglt {
  ExamplesBridglt() {
  }

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

  // to test DrawAt
  void testDrawAt(Tester t) {
    t.checkExpect(none.drawAt(1, 1, bg), bg);
    bg.placeImageXY(new RectangleImage(50, 50, "solid", Color.white), (1 - 1) * 50 + 25,
        (1 - 1) * 50 + 25);
    t.checkExpect(c1.drawAt(0, 0, bg), bg);
    bg = new WorldScene(150, 150);
    bg.placeImageXY(new RectangleImage(50, 50, "solid", Color.pink), (2 - 1) * 50 + 25,
        (2 - 1) * 50 + 25);
    t.checkExpect(c2.drawAt(2, 2, bg), bg);
  }

  // to test makeScene
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

  // to test setup
  void testSetup(Tester t) {
    Bridglt ex = new Bridglt();
    ex.size = 3;
    ex.setup(3);
    t.checkExpect(ex, eg);
  }

  // to test give
  void testGive(Tester t) {
    Cells sp = new Cell(Color.white);
    Cells noneeg = new None();
    noneeg.give(c1, c1, c2, c3);
    t.checkExpect(noneeg, none);
    sp.give(c1, c1, c2, c3);
    t.checkExpect(sp, new Cell(Color.white, c1, c1, c2, c3));

  }

  // to test valid
  void testValid(Tester t) {
    t.checkExpect(new Utility().valid(3, ""), 3);
    t.checkExpect(new Utility().valid(5, ""), 5);
  }

  // test hasPathHep
  void testHasPathHep1(Tester t) {
    Bridglt test = new Bridglt(3);
    ArrayList<Posn> visited = new ArrayList<Posn>();
    t.checkExpect(test.hasPathHep(1, 1, 1, 0, visited), true);
    t.checkExpect(test.hasPathHep(1, 1, 1, 1, visited), false);
    t.checkExpect(test.hasPathHep(3, 1, -1, 0, visited), false);
  }

  // test assign
  void testAssign(Tester t) {
    Bridglt eg = new Bridglt(3);

    // asume the test if for the cell at row 2 and column 2
    int rowtest = 2;
    int coltest = 2;

    t.checkExpect(eg.cells.get(rowtest).get(coltest).isValidCell(), true);
    eg.assign();

    Cells celldummy = eg.cells.get(rowtest).get(coltest);
    t.checkExpect(celldummy.getUp(), eg.cells.get(rowtest - 1).get(coltest));
    t.checkExpect(celldummy.getDown(), eg.cells.get(rowtest + 1).get(coltest));
    t.checkExpect(celldummy.getLeft(), eg.cells.get(rowtest).get(coltest - 1));
    t.checkExpect(celldummy.getRight(), eg.cells.get(rowtest).get(coltest + 1));
  }

  // test for game won with losss
  void testIsGameWon(Tester t) {
    Bridglt game = new Bridglt(5);
    // test a empty grid
    t.checkExpect(game.isGameWon(), false);
    t.checkExpect(game.isGameWon(), true);

  }

  // test construcotr
  void testConstructor(Tester t) {
    t.checkConstructorException(
        new IllegalArgumentException("Invalid Argument: Size should be greater than 3 and odd."),
        "eg", 2);
  }

  // test formake neighbors
  void testMakeNeigh(Tester t) {
    Bridglt eg = new Bridglt(5);

    // test the corner of board
    t.checkExpect(eg.makeNeighbors(1, 1), new ArrayList<>(List.of(new Posn(2, 1), new Posn(1, 2))));

    // test for the inside of the cell
    t.checkExpect(eg.makeNeighbors(2, 2),
        new ArrayList<>(List.of(new Posn(1, 2), new Posn(3, 2), new Posn(2, 1), new Posn(2, 3))));

    // test for edge cell
    t.checkExpect(eg.makeNeighbors(3, 2),
        new ArrayList<>(List.of(new Posn(2, 2), new Posn(4, 2), new Posn(3, 1), new Posn(3, 3))));
  }
  
  //test for pixel to cell corner
  void testPixel(Tester t) {
    
  }
}

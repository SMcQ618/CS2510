import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import tester.Tester;
import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;

/*need to work on having the tiles be 
recognized when they are in their corect place and 
change colors or just solve the entire board
*/

//represents the constants of the game
interface IConstants {
  int BOARD_SIZE = 4;
  int HEIGHT = 200;
  int WIDTH = 200;
  WorldScene background = new WorldScene(WIDTH, HEIGHT);
}

// Represents an individual tile
class Tile {
  // The number on the tile. Use 0 to represent the space
  int value;

  Tile(int value) {
    this.value = value;
  }

  // Draws this tile onto the background at the specified logical coordinates
  WorldScene drawAt(int col, int row, WorldScene background) {
    if (this.value == 0) {
      // return empty when value of tile is zero
    }
    else if (this.value == 4 * row + 1 + col) {
      background.placeImageXY(new TextImage(Integer.toString(this.value), 24, Color.black),
          col * 50 + 25, row * 50 + 25);
    }
    else {
      // this will help set up the numbers
      background.placeImageXY(new TextImage(Integer.toString(this.value), 24, Color.gray),
          col * 50 + 25, row * 50 + 25);
    }
    return background;
  }
}

// represents the game/behind the scenes that the user does not see
class FifteenGame extends World implements IConstants {
  // represents the rows of tiles
  ArrayList<ArrayList<Tile>> tiles;
  ArrayList<ArrayList<Tile>> history;
  Random rand;
  boolean gameWon;

  // constructor for the game
  FifteenGame() {
    this.rand = new Random(); // actual random
    this.setup();
    this.history = new ArrayList<>();
    this.gameWon = false;
  }

  // constructor for tests
  FifteenGame(Random rand) {
    this.rand = rand;
    this.setup();
    this.history = new ArrayList<>();
    this.gameWon = false;
  }

  // get an empty scene for rendering
  WorldScene emptyScene() {
    return new WorldScene(WIDTH, HEIGHT);
  }

  // EFFECT: construct the 2d array tiles
  void setup() {
    ArrayList<Integer> some = new ArrayList<>(
        Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15));
    // need to create anew instance of tile
    this.tiles = new ArrayList<ArrayList<Tile>>();

    for (int i = 0; i < 4; i++) {
      ArrayList<Tile> row = new ArrayList<Tile>();
      // some sort of action in this row
      for (int j = 0; j < 4; j++) {
        int someIndex = this.rand.nextInt((some.size()));
        row.add(new Tile(some.get(someIndex))); // randomizing the size of the index
        some.remove(someIndex);
      }

      this.tiles.add(row);
    }
  }

  // draws the game/ is the model view of the game
  public WorldScene makeScene() {
    // create a new scene for the winning state
    if (this.gameWon) {
      WorldScene winscene = this.getEmptyScene();
      winscene.placeImageXY(new TextImage("Congratulations! You've Won", 14, Color.RED), WIDTH / 2,
          HEIGHT / 2);
      return winscene;
    }
    else {
      WorldScene scene = this.emptyScene();

      for (int row = 0; row < BOARD_SIZE; row++) {
        for (int col = 0; col < BOARD_SIZE; col++) {
          Tile tile = this.tiles.get(row).get(col);
          tile.drawAt(col, row, scene);
          // take the tile and make it a world image to be seen and positioned
        }
      }
      return scene;
    }
  }

  // EFFECT: Store the current state of the game in the history tab
  void storeMove() {
    ArrayList<Tile> currentState = new ArrayList<>(this.tiles.size());
    for (ArrayList<Tile> row : this.tiles) {
      // for each row have it a tile
      ArrayList<Tile> newrow = new ArrayList<>(row.size());
      for (Tile tile : row) {
        newrow.add(new Tile(tile.value));
      }
      currentState.addAll(newrow);
    }
    this.history.add(currentState);
  }

  // EEFECT: Will undo the previous move that the user typed
  void undoMove() {
    if (!this.history.isEmpty()) {
      // get the tile of the previous state
      ArrayList<Tile> prevState = this.history.remove(this.history.size() - 1);
      // create a array to hold the current state
      this.tiles = new ArrayList<>(IConstants.BOARD_SIZE);

      for (int i = 0; i < prevState.size(); i += IConstants.BOARD_SIZE) {
        ArrayList<Tile> newRow = new ArrayList<>(prevState.subList(i, i + IConstants.BOARD_SIZE));
        this.tiles.add(newRow);
      }
    }
  }

  // EEFECT: Handles keystrokes that the user makes when playing the game
  public void onKeyEvent(String k) {
    // needs to handle up, down, left, right to move the space
    // extra: handle "u" to undo moves
    int emptyrw = -1;
    int emptycol = -1;

    // find the empty space
    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 4; j++) {
        if (this.tiles.get(i).get(j).value == 0) {
          // use .get().get() to get a specific index value
          emptyrw = i;
          emptycol = j;
        }
      }
    }

    int targetRow = emptyrw;
    int targetCol = emptycol;

    // storeMove();
    // key movements
    if ("u".equals(k) && !this.history.isEmpty()) {
      undoMove();
    }
    if ("down".equals(k) && emptyrw > 0) {
      targetRow--;
      storeMove();

    }
    else if ("up".equals(k) && emptyrw < 3) {
      targetRow++;
      storeMove();
    }
    else if ("right".equals(k) && emptycol > 0) {
      targetCol--;
      storeMove();
    }
    else if ("left".equals(k) && emptycol < 3) {
      targetCol++;
      storeMove();
    }

    // check if the move works
    if ((emptyrw == targetRow && Math.abs(emptycol - targetCol) == 1)
        || (emptycol == targetCol && Math.abs(emptyrw - targetRow) == 1)) {
      Tile targetTile = this.tiles.get(targetRow).get(targetCol);
      this.tiles.get(emptyrw).set(emptycol, targetTile);
      this.tiles.get(targetRow).set(targetCol, new Tile(0));
      // set the empty space to the target position
    }

    // check if the game has been won
    if (isGameWon()) {
      this.gameWon = true;
    }
  }

  // check if the game has been won
  boolean isGameWon() {
    int count = 1;
    for (ArrayList<Tile> row : this.tiles) {
      for (Tile tile : row) {
        if (count == IConstants.BOARD_SIZE * IConstants.BOARD_SIZE && tile.value == 0) {
          return true;
        }
        else if (tile.value != count) {
          return false;
        }
        count++;
      }
    }
    return true;
  }
}

//Examples and tests
class ExampleFifteenGame {

  FifteenGame game;

  // initialize the world
  void initData() {
    this.game = new FifteenGame(new Random(123));
    this.game.setup();
    // set up for specific sceneerio
    this.game.tiles.get(0).get(0).value = 0;
    this.game.tiles.get(0).get(1).value = 1;
    this.game.tiles.get(1).get(0).value = 1;
    this.game.tiles.get(1).get(1).value = 1;
  }

  // create the world and start it
  void testGame(Tester t) {
    FifteenGame g = new FifteenGame();
    g.bigBang(IConstants.HEIGHT, IConstants.WIDTH);
  }

  // test the makescene
  void testMakeScene(Tester t) {
    initData();
    FifteenGame game = new FifteenGame(new Random(123));
    WorldScene scene = game.makeScene();
    // test dimensions
    t.checkExpect(scene.width, IConstants.WIDTH);
    t.checkExpect(scene.height, IConstants.HEIGHT);
    // test the set up of the game
    t.checkExpect(this.game.tiles.get(0).get(0).value, 0);
    t.checkExpect(this.game.tiles.get(0).get(1).value, 1);
  }
  
  //test draw at
  void testDrawAt(Tester t) {
    WorldScene background1 = new WorldScene(200, 200);
    WorldScene ex = new WorldScene(200, 200);
    Tile t1 = new Tile(4);
    ex.placeImageXY(new TextImage(Integer.toString(4), 24, Color.gray),
        0 * 50 + 25, 0 * 50 + 25);
    t.checkExpect(t1.drawAt(0, 0, background1), ex);
    WorldScene background2 = new WorldScene(200, 200);
    WorldScene ex1 = new WorldScene(200, 200);
    ex1.placeImageXY(new TextImage(Integer.toString(4), 24, Color.black),
        3 * 50 + 25, 0 * 50 + 25);
    t.checkExpect(t1.drawAt(3, 0, background2), ex1);
  }

  // test onKeyEvents for moving down
  void testKeyEvent(Tester t) {
    initData();
    t.checkExpect(this.game.tiles.get(0).get(0).value, 0);
    // test move down
    this.game.onKeyEvent("down");
    t.checkExpect(this.game.tiles.get(0).get(1).value, 1);
    // test up movement
    initData();
    t.checkExpect(this.game.tiles.get(0).get(0).value, 0);
    this.game.onKeyEvent("up");
    t.checkExpect(this.game.tiles.get(0).get(1).value, 1);
    // test move left
    initData();
    t.checkExpect(this.game.tiles.get(0).get(0).value, 0);
    this.game.onKeyEvent("left");
    t.checkExpect(this.game.tiles.get(1).get(0).value, 1);
    // test move right
    initData();
    t.checkExpect(this.game.tiles.get(0).get(0).value, 0);
    this.game.onKeyEvent("right");
    t.checkExpect(this.game.tiles.get(1).get(0).value, 1);

    // test move back
    initData();
    t.checkExpect(this.game.tiles.get(0).get(0).value, 0);
    this.game.onKeyEvent("up");
    t.checkExpect(this.game.tiles.get(0).get(1).value, 1);
    this.game.undoMove();
    this.game.onKeyEvent("u");
    t.checkExpect(this.game.tiles.get(0).get(0).value, 0);
  }

  // test is the game won
  void testIsGameWon(Tester t) { //
    initData(); // create a winning situation //
    for (int i = 0; i < IConstants.BOARD_SIZE; i++) { //
      for (int j = 0; j < IConstants.BOARD_SIZE; j++) { //
        this.game.tiles.get(i).get(j).value = i * IConstants.BOARD_SIZE + j + 1;
      }
    }
    this.game.tiles.get(IConstants.BOARD_SIZE - 1).get(IConstants.BOARD_SIZE - 1).value = 0; //
    t.checkExpect(this.game.isGameWon(), true);
  }

}
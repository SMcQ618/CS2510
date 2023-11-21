import tester.*; // The tester library
import javalib.worldimages.*; // images, like RectangleImage or OverlayImages
import javalib.funworld.*; // the abstract World class and the big-bang library
import java.awt.Color; // general colors (as triples of red,green,blue values)
                       // and predefined colors (Red, Green, Yellow, Blue, Black, White)
import java.awt.*;
import java.util.Random;

// represents a list of fishes 
interface ILoFish {
  // draws fishes from this list onto the given scene
  WorldScene draw(WorldScene acc);

  // allow world to move by tick
  ILoFish onTick();

  // handle the movements
  ILoFish onKeyEvent(String key);
}

//represents all the fishes 
abstract class Fish {
  int x;
  int y;
  int speed;
  int size;
  Color c;

  // the constructor
  Fish(int x, int y, int speed, int size, Color c) {
    this.x = x;
    this.y = y;
    this.speed = speed;
    this.size = size;
    this.c = c;
  }

  // abstract method to hanfle key events
  abstract Fish onKeyEvent(String key);

  // draw this fish with its size and color
  WorldImage draw() {
    return new CircleImage(this.size, "solid", this.c);
  }

  abstract Fish onTick();

  // check if the player is ni contact with anotehr fish
  boolean isContact(Fish other) {
    int dx = this.x - other.x;
    int dy = this.y - other.y;
    int minDist = (this.size / 2) + (other.size / 2);
    return dx < minDist && dy < minDist;
  }

  abstract boolean canEat(Fish fish);
}

// to represent a world class to animate a list of fishes 
//on a scene
class FishWorld extends World {
  ILoFish fishes;

  // the constructor
  FishWorld(ILoFish fishes) {
    this.fishes = fishes;
  }

  // draws the fishes onto the background
  public WorldScene makeScene() {
    return this.fishes.draw(new WorldScene(600, 400));
  }

  // when key pressed action occurs
  public World onKeyEvent(String key) {
    return new FishWorld(this.fishes.onKeyEvent(key));
  }

  // by each tick movement occurs
  public World onTick() {
    ILoFish updtfsh = this.fishes.onTick();
    return new FishWorld(updtfsh);
  }
}

// represents an empty list of fishes 
class MtLoFish implements ILoFish {
  // draws fishes from this empty list onto the accumulated
  // image of the scene so far
  public WorldScene draw(WorldScene acc) {
    return acc;
  }

  @Override
  // an empty list would have no action
  public ILoFish onKeyEvent(String key) {
    return this;
  }

  @Override
  public ILoFish onTick() {
    return this;
  }
}

// represents a non-empty list of fishes
class ConsLoFish implements ILoFish {
  Fish first;
  ILoFish rest;

  // the constructor
  ConsLoFish(Fish first, ILoFish rest) {
    this.first = first;
    this.rest = rest;
  }

  // draws fishes from this non-empty list onto the accumulated
  // image of the scene so far
  public WorldScene draw(WorldScene acc) {
    return this.rest.draw(acc.placeImageXY(this.first.draw(), this.first.x, this.first.y));
  }

  // the first fish should move
  public ILoFish onKeyEvent(String key) {
    return new ConsLoFish(this.first.onKeyEvent(key), this.rest.onKeyEvent(key));
  }

  // the rest of the fish should be moving
  public ILoFish onTick() {
    // need the first fish to move then it goes on in a line so everyone moves
    return new ConsLoFish(this.first.onTick(), this.rest.onTick());
  }
}

//to represent player's fish 
class PlayerFish extends Fish {

  int life;

  // the constructor
  PlayerFish(int x, int y, int speed, int size, Color c, int life) {
    super(x, y, speed, size, c);
    this.life = life;
  }

  // when an arrow key is pressed the fish will move up or down
  Fish onKeyEvent(String key) {
    int step = 3;

    // depending on the button clicked the movement changes
    if (key.equals("up")) {
      // so every moment you go up by one the step subtracts by the value to move that value
      this.y -= step;
    }
    else if (key.equals("down")) {
      this.y += step;
    }
    else if (key.equals("right")) {
      this.x += step;
    }
    else if (key.equals("left")) { // assuming this is left
      this.x -= step;
    }
    else {
      // default would be do nothing
      return this;
    }

    // need to have them wrap around
    this.x = (this.x + 600) % 600;
    // doing this basically resets the position to 0 so it goes back to the other
    // side
    this.y = (this.y + 400) % 400;

    return new PlayerFish(this.x, this.y, this.speed, this.size, this.c, this.life);
  }

  @Override
  Fish onTick() {
    int step = 1;
    return new PlayerFish(this.x + step, this.y, this.speed - 10, this.size, this.c, this.life);
  }

  @Override
  boolean canEat(Fish fish) {
    // TODO Auto-generated method stub
    return false;
  }
}

//to represent a background fishes going from left to right
class BackGroundFish extends Fish {
  public Random rand; // create the random generator

//  //the constructor with random for the testing
//  BackGroundFish(int x, int y, int speed, int size, Color c) {
//    super(new Random().nextInt(600), new Random().nextInt(400), speed, size, c);
//  }
  BackGroundFish() {
    super(0, 0, 0, 0, Color.blue);
    // randomize the color and other trats of the fish
    rand = new Random();

    // Randomize the color and other traits of the fish
    this.x = rand.nextInt(600);
    this.y = rand.nextInt(400);
    this.speed = (rand.nextInt(20) + 1) - 10; // Random speed between 1 and 20
    this.size = rand.nextInt(20) + 5; // Random size between 5 and 24

    // Generate a random color
    this.c = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
  }

  // handle the movement for the background fish
  Fish onTick() {
    int newX = this.x + this.speed;

    if (newX >= 600) {
      newX = 0;
    } // this is warping the fish so they show up on the other side regardless
      // return the background fish with the positions
    return new BackGroundFish();
  }

  @Override
  Fish onKeyEvent(String key) {
    return this;
  }

  @Override
  boolean canEat(Fish fish) {
    // TODO Auto-generated method stub
    return false;
  }
}

//to represent speed snacks
class Speeds extends Fish {
  int duration;

  // the constructor
  Speeds(int x, int y, int speed, int size, Color c, int duration) {
    super(x, y, speed, size, c);
    this.duration = duration;
  }

  // draw this speed snacks with its size and color
  WorldImage draw() {
    return new EquilateralTriangleImage(this.size, "solid", this.c);
  }

  // handle the event for speed
  Fish onKeyEvent(String key) {
    return this;
  }

  // the speed is the tick
  Fish onTick() {
    return this;
  }

  // not really needed
  boolean canEat(Fish fish) {
    // TODO Auto-generated method stub
    return false;
  }
}

//to represent size snacks 
class Sizes extends Fish {
  int growth;

  // the constructor
  Sizes(int x, int y, int speed, int size, Color c, int growth) {
    super(x, y, speed, size, c);
    this.growth = growth;
  }

  // draw this size snacks with its size and color
  WorldImage draw() {
    return new CircleImage(this.size, "solid", this.c);
  }

  // handle the event for speed
  Fish onKeyEvent(String key) {
    return this;
  }

  @Override
  Fish onTick() {
    return this;
  }

  @Override
  boolean canEat(Fish fish) {
    // TODO Auto-generated method stub
    return false;
  }}

  //Examples of world program

class ExamplesMyWorldProgram {
  //example of main charachter  
  Fish pf1 = new PlayerFish(300, 200, 5, 10/*2*/, Color.blue, 2);
  Fish pf2 = new PlayerFish(301, 200, 5, 10, Color.blue, 2);
  Fish pf3 = new PlayerFish(352, 241, 5, 14, Color.red, 3);
  
  //examples of background fishes
  Fish bf1 = new BackGroundFish();
  Fish bf2 = new BackGroundFish(); //new BackGroundFish(300, 300, 15, 1, Color.green);
  Fish bf3 = new BackGroundFish(); //new BackGroundFish(254, 300, 5, 18, Color.yellow);
  Fish bf4 = new BackGroundFish(); //new BackGroundFish(300, 465, 7, 13, Color.magenta);
  Fish bf5 = new BackGroundFish(); //new BackGroundFish(195, 300, 25, 8, Color.orange);
  Fish bf6 = new BackGroundFish(); // new BackGroundFish(300, 300, 8, 12, Color.PINK);
  
  ILoFish mt = new MtLoFish();
  ILoFish lof1 = new ConsLoFish(this.pf1, this.mt);
  ILoFish lof2 = new ConsLoFish(this.bf2, this.lof1);
  ILoFish lof3 = new ConsLoFish(this.bf1, this.lof2);
  
  
  ILoFish lof4 = new ConsLoFish(this.bf1, new ConsLoFish(this.bf2, 
      new ConsLoFish(this.bf3,new ConsLoFish(this.bf4, 
          new ConsLoFish(this.pf1, new ConsLoFish(this.pf3, this.mt))))));

  
  Color c1 = Color.cyan;
  Color c2 = Color.GREEN;
  
  //example of fish worlds
  FishWorld init = new FishWorld(this.mt);
  FishWorld w1 = new FishWorld(new ConsLoFish(this.pf1, this.mt));
  FishWorld w2 = new FishWorld(mt);
  FishWorld w3 = new FishWorld(lof1);
  
  boolean testPlayerMove(Tester t) {
    return t.checkExpect(this.pf1.onKeyEvent("right").x, 303)
        && t.checkExpect(this.pf2.onKeyEvent("left").x, 298);
  }
  
  boolean testonTick(Tester t) {
    return t.checkExpect(pf3.onTick().x, 353);
  }
 //test for the background fishes
  boolean testBackMove(Tester t) {
    return t.checkExpect((this.bf1.onTick().x >= 0 && this.bf1.onTick().x < 600), true)
        && t.checkExpect((this.bf2.onTick().x >= 0 && this.bf2.onTick().x < 600), true);
  }
  
  //test an empty scene
  //and it should return true cause the scene is empty
  boolean testInitialWorld(Tester t) {
    return t.checkExpect(this.init.fishes.equals(this.mt), true);
  }
  
  //test with 1 background
    boolean testBigBang(Tester t) {
    FishWorld w = new FishWorld(this.lof3);
    int worldWidth = 600;
    int worldHeight = 400;
    double tickRate = 0.1;
    return w.bigBang(worldWidth, worldHeight, tickRate);
  }
  
  //testing bigbang with multiple fishes
  boolean testBigBang2(Tester t) {
    FishWorld w = new FishWorld(this.lof4);
    int worldWidth = 600;
    int worldHeight = 400;
    int tickrate = 1;
    return w.bigBang(worldWidth, worldHeight, tickrate);
  }
}

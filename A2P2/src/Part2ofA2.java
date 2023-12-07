import tester.Tester;

interface ILoFeature {
  String restaurantInfo();

  double calculateAvgR(); // foodinessRatingHelper
}

interface IFeature {
  String restaurantInfo();

  double foodinessRating();
}

// a class representing a place
class Place {
  String name;
  ILoFeature features;

  Place(String name, ILoFeature features) {
    this.name = name;
    this.features = features;
  }
  /*
   * fields: this.name ... String this.feature ... ILoFeature
   * 
   * methods: foodinessRating() ... double restaurantInfo() ... String
   * calcualteAvgR ... double
   */

  // calculates the average rating of all restaurants
  double foodinessRating() {
    /*
     * fields: this.name ... String this.feature ... ILoFeature
     */
    return this.features.calculateAvgR();
  }

  // produces string that basically says what the restaurant is
  String restaurantInfo() {
    return this.features.restaurantInfo();
  }
}

//represents the empty list of features
class MtLoFeature implements ILoFeature {
  // produces string that basically says what the restaurant is
  @Override
  // gives an empty info
  public String restaurantInfo() {
    return "";
  }

  /*
   * methods: foodinessRating() ... double restaurantInfor() ... String
   * ILoFeature.restaurantInfo() ... ILoFeature
   */
  @Override
  public double calculateAvgR() {
    return 0.0;
  }
}

//way of adding features to the list of features
class ConsLoFeature implements ILoFeature {
  IFeature first;
  ILoFeature rest;

  public ConsLoFeature(IFeature first, ILoFeature rest) {
    this.first = first;
    this.rest = rest;
  }

  /*
   * fields: this.name ... String this.feature ... ILoFeature methods:
   * foodinessRating() ... double restaurantInfo() ... String
   * restaurantInfoHelper() ... String
   */
  @Override
  // represents an restaurant information
  public String restaurantInfo() {
    if (rest.restaurantInfo().isEmpty()) {
      return this.first.restaurantInfo();
    }
    else {
      return this.first.restaurantInfo() + ", " + this.rest.restaurantInfo();
    }
  }

  @Override
  public double calculateAvgR() {
    return this.first.foodinessRating() + this.rest.calculateAvgR();
  }
}

//class that represents a restaurant and all it entails
class Restaurant implements IFeature {
  String name;
  String type;
  double averageRating;

  Restaurant(String name, String type, double averageRating) {
    this.name = name;
    this.type = type;
    this.averageRating = averageRating;
  }

  /*
   * fields: this.name ... String this.type ... String this.averageRating ...
   * double methods: foodinessRating() ... double restaurantInfor() ... String
   */

  @Override
  // calculates the average rating of all restaurants
  public double foodinessRating() {
    return this.averageRating;
  }

  @Override
  // produces string that basically says what the restaurant is
  public String restaurantInfo() {
    return this.name + " (" + this.type + ")";
  }
}

//represents venue that is apart of a feature
class Venue implements IFeature {
  String name;
  String type;
  int capacity;

  Venue(String name, String type, int capacity) {
    this.name = name;
    this.type = type;
    this.capacity = capacity;
  }

  /*
   * fields: this.name ... String this.type ... String this.capacity ... int
   * methods: foodinessRating() ... double restaurantInfor() ... String
   * 
   */

  // calculates the average rating of all restaurants
  @Override
  public double foodinessRating() {
    return 0.0; // venues do not have any ratings
  }

  // produces string that basically says what the restaurant is
  @Override
  public String restaurantInfo() {
    return this.name + " (" + this.type + ")";
  }
}

//represents shuttle busses that run in/out of places
class ShuttleBus implements IFeature {
  String name;
  Place destination; // references the place class

  ShuttleBus(String name, Place destination) {
    this.name = name;
    this.destination = destination;
  }

  /*
   * fields: this.name ... String this.destination ... Place methods:
   * foodinessRating() ... double restaurantInfor() ... String
   */

  // calculates the average rating of all restaurants
  @Override
  public double foodinessRating() {
    return 0.0; // shuttle busses do not have ratings
  }

  // produces string that basically says what the restaurant is
  @Override
  public String restaurantInfo() {
    return this.name + " to " + this.destination.name;
  }
}

//represents the examples and tests
class ExamplesPlaces {
  // Examples
  // examples of basically when you think of Cambridge Side Galleria these are
  // places that are featured
  Place cambridgeSide = new Place("CambridgeSide Galleria",
      new ConsLoFeature(new Restaurant("Sarku Japan", "teriyaki", 3.9), // stub
          new ConsLoFeature(new Restaurant("Starbucks", "coffee", 4.1), new MtLoFeature())));
  // examples of basically when you think of South Stationthese are places that
  // are featured
  Place southStation = new Place("South Station",
      new ConsLoFeature(
          new ShuttleBus("Little Italy Express", new Place("North End", new MtLoFeature())),
          new ConsLoFeature(new Restaurant("Regina's Pizza", "pizza", 4.0),
              new ConsLoFeature(
                  new ShuttleBus("Crimson Cruiser",
                      new Place("Harvard", new ConsLoFeature(
                          new Venue("Boston Common", "public", 150000), new MtLoFeature()))),
                  new MtLoFeature()))));
  // examples of basically when you think of North End these are places that are
  // featured
  Place northEnd = new Place("North End", new ConsLoFeature(
      new Venue("TD Garden", "stadium", 19580),
      new ConsLoFeature(new Restaurant("The Daily Catch", "Sicilian", 4.4), new MtLoFeature())));
  // examples of basically when you think of Harvard these are places in the area
  Place harvard = new Place("Harvard", new ConsLoFeature(
      new ShuttleBus("Freshman-15", new Place("North End", new MtLoFeature())),
      new ConsLoFeature(new Restaurant("Border Cafe", "Tex-Mex", 4.5),
          new ConsLoFeature(new Venue("Harvard Station", "football", 30323), new MtLoFeature()))));
  // example of a place with no restaurants
  Place Jesup = new Place("Jesup",
      new ConsLoFeature(new ShuttleBus("County-Bus", new Place("Poppell Farms", new MtLoFeature())),
          new MtLoFeature()));

  // checks to make sure the rating of restaurants matchese
  boolean testfoodinessRating(Tester t) {
    double expectedCambridge = 8.0; // this is the avg from cambridgeside
    double expectedSouthStation = 4.0;
    double expectedNorthE = 4.4;
    double expectedHarvard = 4.5;

    // these tests are to say is the value of the ratings the same to the ones above
    return t.checkInexact(this.cambridgeSide.foodinessRating(), expectedCambridge, 0.001)
        && t.checkInexact(this.southStation.foodinessRating(), expectedSouthStation, 0.001)
        && t.checkInexact(this.northEnd.foodinessRating(), expectedNorthE, 0.001)
        && t.checkInexact(this.harvard.foodinessRating(), expectedHarvard, 0.001);
  }

  // tests for restaurant info
  boolean testrestaurantInfo(Tester t) {
    return t.checkExpect(this.cambridgeSide.restaurantInfo(),
        "Sarku Japan (teriyaki), Starbucks (coffee), The Daily Catch (Sicilian), "
        + "Regina's Pizza (pizza), The Daily Catch (Sicilian), Border Cafe (Tex-Mex)")
        && t.checkExpect(this.southStation.restaurantInfo(),
            "Little Italy Express to North End, Regina's Pizza (pizza), Crimson Cruiser to Harvard")
        && t.checkExpect(this.northEnd.restaurantInfo(),
            "TD Garden (stadium), The Daily Catch (Sicilian)")
        && t.checkExpect(this.harvard.restaurantInfo(),
            "Freshman-15 to North End, Border Cafe (Tex-Mex), Harvard Station (football)");
  }

  // test for a place with no restaurants
  boolean testNoRestaurantsPlace(Tester t) {
    return t.checkInexact(this.Jesup.foodinessRating(), 0.0, 0.001)
        && t.checkExpect(this.Jesup.restaurantInfo(), "County-Bus to Poppell Farms");
    // this should check if Jesup has any restaurants and the food rating of places
    // which is does not in the examples so it should return where the bus is going
  }

  ExamplesPlaces() {}
}

/* Some methods double count information because when a piece of information is inluded
 * multiple times in a method, which can make the final output incorrect if it is not checked
 * correctly. this can be such as counting, added, and doing other math functions
 * this is similar to pointers. where a variable is pointed to then if somethign is done to it
 * that it changes the value it can permanently change the final value/output of the value it was
 * pointed to.
 * 
*/
// all the main choices overlap into a resource
interface IResource {
}

// the two main actions are an action
interface IAction {
}

// One of the resources a player can choose
class Denial implements IResource {
  String type = "Denial";
  String subject;
  int believability;

  Denial(String subject, int believability) {
    this.subject = subject;
    this.believability = believability;
  }
}

// One of the resources a player can choose
class Bribe implements IResource {
  String type = "Bribe";
  String target;
  int amount;

  Bribe(String target, int amount) {
    this.target = target;
    this.amount = amount;
  }
}

//One of the resources a player can choose
class Apology implements IResource {
  String type = "Apology";
  String excuse;
  boolean reusable; // does this need to be set to some value?

  Apology(String excuse, boolean reusable) {
    this.excuse = excuse;
    this.reusable = reusable;
  }
}

//One of the 2 actions a player can choose
class Purchase implements IAction {
  int cost;
  IResource resource; // this will take from the resource class

  Purchase(int cost, IResource resource) {
    this.cost = cost;
    this.resource = resource;
  }
}

// The second action you can use
class Swap implements IAction {
  IResource consumed;
  IResource received;

  Swap(IResource consumed, IResource received) {
    this.consumed = consumed;
    this.received = received;
  }
}

//represents examples and tests for the Game
class ExamplesGame {
  Denial iDidntKnow = new Denial("knowledge", 51);
  Bribe witness = new Bribe("innocent witness", 49);
  Apology iWontDoItAgain = new Apology("I won't do it again", false);
  Apology reusableApology = new Apology("Sorry my bad", true);
  Bribe anotherBribe = new Bribe("corrupt official", 75);
  Denial anotherDenial = new Denial("innocence", 60);

  Purchase purchase1 = new Purchase(10, iDidntKnow);
  Purchase purchase2 = new Purchase(15, witness);
  Swap swap1 = new Swap(anotherBribe, iDidntKnow);
  Swap swap2 = new Swap(anotherDenial, witness);

  ExamplesGame() {
  }
}
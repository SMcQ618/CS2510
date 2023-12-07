interface IWaffle {
}

class Plain implements IWaffle {
  String flour;

  Plain(String flour) {
    this.flour = flour;
  }
}

class Topped implements IWaffle {
  IWaffle below;
  String topping;

  Topped(IWaffle below, String topping) {
    this.below = below;
    this.topping = topping;
  }
}

class ExamplesWaffle {
  IWaffle order1 = new Topped(new Topped(
      new Topped(new Topped(new Plain("almond"), "chocolate chips"), "whipped cream"),
      "strawberries"), "walnuts");
  IWaffle order2 = new Topped(new Topped(new Plain("buckwheat"), "chicken"), "gravy");
  IWaffle order3 = new Topped(new Plain("all-purpose"), "Syrup");
  IWaffle order4 = new Topped(
      new Topped(new Topped(new Plain("all-purpose"), "Bacon"), "Sausage"), "Syrup");
  IWaffle order5 = new Topped(new Plain("all-purpose"), "Syrup");

  ExamplesWaffle() {
  }
}
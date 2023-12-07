import java.util.function.*;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import tester.*;

//represents a generic list
interface IList<T> {
  // accepts a visitor for a list
  <R> R accept(IListVisitor<T, R> vis);

  // asks a question about a T
  boolean apply(T t);

  // maps a functtion to every member ina list and returns a new list
  <R> IList<R> map(IFunc<T, R> fun);

  // <R> IList<R> acceptt(MapVisitor<T, R> mapVisitor, IFunc<T, R> fun);
}

//represents the IList visitor
interface IListVisitor<T, R> {
  // applies an operation to the empty
  R visitMt(MtList<T> mt);

  // applies an operation to non-empty
  R visitCons(ConsList<T> cons);
}

//do I need this??
interface IFunc<T, R> {
  R apply(T t); // do i need to make an ifunc?
  // so you apply u to t?
}

//represents an ancester tree
interface IAT {
  <R> R accept(IATVisitor<R> visitor);
}

//visitor interface for iat
interface IATVisitor<R> {
  // visit method for empty
  R visit(Unknown u);

  // visit method for person
  R visit(Person p);
}

//represents an empty generic list
class MtList<T> implements IList<T> {
  // need a way to accept a visitor for an empty
  // accepts a visitor for a list
  public <R> R accept(IListVisitor<T, R> vis) {
    return vis.visitMt(this);
  }

  // checks if the given list isempty or not
  public boolean apply(T t) {
    return false;
  }

  // applies some function to a list and returns a new list
  public <R> IList<R> map(IFunc<T, R> fun) {
    return new MtList<R>();
  }
}

//represents a generic non-empty list
class ConsList<T> implements IList<T> {
  T first;
  IList<T> rest;

  ConsList(T first, IList<T> rest) {
    this.first = first;
    this.rest = rest;
  }

  // accepts a visitor for a list
  public <R> R accept(IListVisitor<T, R> vis) {
    return vis.visitCons(this);
  }

  // asks a question about a T
  public boolean apply(T t) {
    return t.equals(this.first) || this.rest.apply(t);
  }

  // applies some function to a list and returns a new list
  public <R> IList<R> map(IFunc<T, R> fun) {
    // so it will take the first of a list and apply it then recur on the rest
    return new ConsList<R>(fun.apply(this.first), this.rest.map(fun));
  }
}

//represents the map visitor that is a helper??
class MapVisitor<T, R> implements IListVisitor<T, IList<R>> {
  Function<T, R> function;

  MapVisitor(Function<T, R> function) {
    this.function = function;
  }

  // should map to thte empty
  public IList<R> visitMt(MtList<T> mt) {
    return new MtList<>(); // there shouldn't be a new thing
  }

  // maps onto a list of something
  public IList<R> visitCons(ConsList<T> cons) {
    return new ConsList<R>(this.function.apply(cons.first), cons.rest.accept(this));
  }
}

//represents the appendvisitor class
class AppendVisitor<T> implements IListVisitor<T, IList<T>> {
  IList<T> thatList;

  public AppendVisitor(IList<T> thatList) {
    this.thatList = thatList;
  }

  // appends the visitmt list
  public IList<T> visitMt(MtList<T> mt) {
    return this.thatList;
  }

//appends the non empty list
  public IList<T> visitCons(ConsList<T> cons) {
    return new ConsList<>(cons.first, cons.rest.accept(this));
  }
}

//represents an unknown ancestor tree
class Unknown implements IAT {
  Unknown() {
  }

  // accepts the visitor
  public <R> R accept(IATVisitor<R> visitor) {
    return visitor.visit(this);
  }
}

//represents a person in an ancester tree
class Person implements IAT {
  String name;
  int yob;
  IAT parent1;
  IAT parent2;

  Person(String name, int yob, IAT parent1, IAT parent2) {
    this.name = name;
    this.yob = yob;
    this.parent1 = parent1;
    this.parent2 = parent2;
  }

  // acceptor for visiting a person in teh tree
  public <R> R accept(IATVisitor<R> visitor) {
    return visitor.visit(this);
  }

}

//priduces a list of peoples names in teh iat
class NamesVisitor implements IATVisitor<IList<String>> {
  // produces an list of strings
  public IList<String> visit(Unknown u) {
    return new MtList<String>();
  }

  // prodices a list of string s for non empty
  public IList<String> visit(Person p) {
    return new ConsList<String>(p.name,
        p.parent1.accept(this).accept(new AppendVisitor<String>(p.parent2.accept(this))));
  }
}

//represents the exampels class
class Examples {
  IList<String> m1 = new MtList<>(); // empty should be empty
  IList<Integer> L1 = new ConsList<Integer>(3,
      new ConsList<Integer>(12, new ConsList<Integer>(233, new MtList<>())));
  IList<String> Ls1 = new ConsList<String>("3",
      new ConsList<String>("12", new ConsList<String>("233", new MtList<>())));
  IList<String> L2 = new ConsList<String>("dark", new ConsList<String>("sip",
      new ConsList<String>("turn", new ConsList<String>("swimming", new MtList<>()))));
  IList<Double> L3 = new ConsList<Double>(1.2,
      new ConsList<Double>(3.5, new ConsList<Double>(32.2, new MtList<>())));
  IList<Integer> L4 = new ConsList<Integer>(4,
      new ConsList<Integer>(43, new ConsList<Integer>(54, new MtList<>())));
  IAT P1 = new Unknown();
  IAT P2 = new Person("Timmy", 1993, new Unknown(), new Unknown());
  IAT P3 = new Person("Sarah", 2002, new Unknown(), new Unknown());
  IAT P4 = new Person("Matt", 2010, P2, P3);
  IAT P5 = new Person("Mary", 2003, P2, new Unknown());
  // AppendVisitor<String> appendvis = new AppendVisitor<>(Ls1);
  // AppendVisitor<Integer> appendvis2 = new AppendVisitor<>(L4);
  IList<String> appendList = L2.accept(new AppendVisitor<>(Ls1));
  IList<Integer> appendL2 = L1.accept(new AppendVisitor<>(L4));

  Function<Integer, String> intToString = i -> i.toString();
  Function<Integer, Double> toDouble = i -> i * 2.0;

  IATVisitor<IList<String>> namesVisitor = new NamesVisitor();

  // need to test the mapvisitor function and teh appendvisitor
  void testMap(Tester t) {
    t.checkExpect(this.L1.accept(new MapVisitor<>(intToString)),
        new ConsList<>("3", new ConsList<>("12", new ConsList<>("233", new MtList<>()))));
  }

  void testMap2(Tester t) {
    t.checkExpect(this.L1.accept(new MapVisitor<>(toDouble)), new ConsList<Double>(6.0,
        new ConsList<Double>(24.0, new ConsList<Double>(466.0, new MtList<>()))));
  }

  // test append method
  void testAppend(Tester t) {
    t.checkExpect(this.appendList,
        new ConsList<String>("dark",
            new ConsList<String>("sip",
                new ConsList<String>("turn", new ConsList<String>("swimming", new ConsList<String>(
                    "3",
                    new ConsList<String>("12", new ConsList<String>("233", new MtList<>()))))))));
  }

  // second test for append
  void testAppend2(Tester t) {
    t.checkExpect(this.appendL2,
        new ConsList<Integer>(3,
            new ConsList<Integer>(12, new ConsList<Integer>(233, new ConsList<Integer>(4,
                new ConsList<Integer>(43, new ConsList<Integer>(54, new MtList<>())))))));
  }

  // test for IAT with IATVisitor
  void testIAT(Tester t) {
    t.checkExpect(P1.accept(namesVisitor), new MtList<>());
  }

  // test IATVisitor
  void testIATVisitor(Tester t) {
    t.checkExpect(P2.accept(new NamesVisitor()), new ConsList<>("Timmy", new MtList<>()));
  }

  void testIATVisitor2(Tester t) {
    t.checkExpect(P4.accept(new NamesVisitor()),
        new ConsList<>("Matt", new ConsList<>("Timmy", new ConsList<>("Sarah", new MtList<>()))));

  }

  Examples() {
  }
}

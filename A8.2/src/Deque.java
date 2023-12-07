import tester.Tester;

//represent the boolean question over values of type T
interface IPred<T> {
  boolean apply(T t);
}

class Deque<T> {
  // remeber the fields in sentinel will change
  Sentinel<T> header;

  Deque() {
    this.header = new Sentinel<T>();
  }

  Deque(Sentinel<T> header) {
    this.header = header;
  }

  int size() {
    return this.header.size();
  }

  // inserts t into the front of list
  void addAtHead(T t) {
    header.addAtHead(t);
  }

  // inserts t into the back of list
  void addAtTail(T t) {
    header.addAtTail(t);
  }

  // removes the first node from the Deque
  T removeFromHead() {
    if (this.size() == 0) {
      throw new RuntimeException("No removal from empty list");
    }
    return header.removeFromHead();
  }

  T removeFromTail() {
    if (this.size() == 0) {
      throw new RuntimeException("Can't remove from an empty list");
    }
    return header.removeFromTail();
  }

  // find a node that satifies the predicate
  public ANode<T> find(IPred<T> p) {
    return header.find(p);
  }

  void removeNode(ANode<T> aN) {
    if (!aN.equals(header)) {
      header.removeNodeFirst(aN);
    }
  }
}

//represent a node with either a sentinel or a regular node
abstract class ANode<T> {
  ANode<T> next;
  ANode<T> prev;
  
  //constructor
  ANode() {
    this.next = null;
    this.prev = null;
  }

// returns number of nodes
  int size(ANode<T> s) {
    if (this.next.equals(s)) {
      return 0;
    }
    else {
      return this.next.size(s);
    }
  }

//find the node that meets the predicate
  T remove() {
    this.prev.next = this.next;
    this.next.prev = this.prev;
    return this.getData();
  }

//find the node that meets the predicate
  ANode<T> find(IPred<T> pred) {
    return this;
  }

//helper in removing the node from a deque
  void removeNode(ANode<T> s) {
    if (this.equals(s)) {
      this.remove();
    }
    else {
      this.next.removeNode(s);
    }
  }

  T getData() {
    return null;
  }
}

class Node<T> extends ANode<T> {
// every daya has 2links its prev and next
  T data;

// first constructor
  Node(T data) {
    this.data = data;
    this.next = null; // test for null-ness
    this.prev = null;
  }

// convenience constructor
  Node(T data, ANode<T> next, ANode<T> prev) {
    super();
    if (prev == null || next == null) {
      throw new IllegalArgumentException("Cannot accept null node");
    }

    this.data = data;
    this.next = next;
    this.prev = prev;
    
    prev.next = this; // so theh new previous is the next one
    next.prev = this;
  }

  ANode<T> find(IPred<T> pred) {
    if (pred.apply(this.data)) {
      return this;
    }
    else {
      // implied else
      return this.next.find(pred);
    }
  }

  T getData() {
    return this.data;
  }
}

class Sentinel<T> extends ANode<T> {
  /*
   * has no fields a node is always present tho, its next and prev fields are
   * always being updated to the "head" and "tail" inherits from a common
   * superclass so i guess like inheritance?? but ANode can be a sentinel or
   * something else
   */

  // constructor
  Sentinel() {
    this.prev = this;
    this.next = this;
  }

  // help in returning the number of nodes
  public int size() {
    if (this.next.equals(this)) {
      return 0;
    }
    else {
      return this.next.size(this);
    }
  }

  // add a node to the the head
  T addAtHead(T t) {
    new Node<T>(t, this.next, this);
    return t;
  }

  // add a node to the tail
  T addAtTail(T t) {
    new Node<T>(t, this, this.prev);
    return t;
  }

  // remove node from head of deque
  T removeFromHead() {
    return this.next.remove();
  }

  // remove node from tail of deque
  T removeFromTail() {
    return this.prev.remove();
  }

  // help remove a node form deque
  void removeNodeFirst(ANode<T> n) {
    this.next.removeNode(n);
  }

  // help in removing the node form a deque
  void removeNode(ANode<T> a) {
    return;
  }

  // assists in finding a node that meets predicate
  ANode<T> find(IPred<T> p) {
    ANode<T> current = this.next;
    while(!current.equals(this)) {
      if (p.apply(current.getData())) {
        return current;
      }
      current = current.next;
    }
    return this;
  }
}

//for the tests with values greater than 60
class GrtSix implements IPred<Integer> {
  public boolean apply(Integer num) {
    return num > 60;
  }
}

// for the testing if it is a string is equal to "abc"
class IsString implements IPred<String> {
  public boolean apply(String s) {
    return s.equals("abc");
  }
}

// is a string value is equal to "sarah"
class IsSarah implements IPred<String> {
  public boolean apply(String s) {
    return s.equals("sarah");
  }
}

class ExamplesDeque {
  Deque<String> deque1 = new Deque<String>();
  Sentinel<String> Str1 = new Sentinel<String>();
  // nodes for strings
  Node<String> abc = new Node<String>("abc", Str1, Str1); // name them like entries in matrix
  Node<String> bcd = new Node<String>("bcd", Str1, abc);
  Node<String> cde = new Node<String>("cde", Str1, bcd);
  Node<String> def = new Node<String>("def", Str1, cde);
  Deque<String> deque2 = new Deque<String>(Str1);

  Sentinel<Integer> S3 = new Sentinel<Integer>();
  Node<Integer> Nd31 = new Node<Integer>(32, S3, S3);
  Node<Integer> Nd32 = new Node<Integer>(23, S3, Nd31);
  Node<Integer> Nd33 = new Node<Integer>(99, S3, Nd32);
  Node<Integer> Nd34 = new Node<Integer>(60, S3, Nd33);
  Node<Integer> Nd35 = new Node<Integer>(12, S3, Nd34);
  Deque<Integer> deque3 = new Deque<Integer>(S3);

  // resets? the data
  void initData() {
    // this will basically start everythign in their testsing places
    deque1 = new Deque<String>(); // initalize everything
    Str1 = new Sentinel<String>();
    abc = new Node<String>("abc", Str1, Str1);
    bcd = new Node<String>("bcd", Str1, abc);
    cde = new Node<String>("cde", Str1, bcd);
    def = new Node<String>("def", Str1, cde);
    deque2 = new Deque<String>(Str1);
    S3 = new Sentinel<Integer>();
    Nd31 = new Node<Integer>(53, S3, S3);
    Nd32 = new Node<Integer>(23, S3, Nd31);
    Nd33 = new Node<Integer>(99, S3, Nd32);
    Nd34 = new Node<Integer>(60, S3, Nd33);
    Nd35 = new Node<Integer>(54, S3, Nd34);
    deque3 = new Deque<Integer>(S3);
  }

  // test for the size func
  boolean testSize(Tester t) {
    initData();
    return t.checkExpect(this.deque1.size(), 0) 
        && t.checkExpect(this.deque2.size(), 4)
        && t.checkExpect(this.deque3.size(), 5);
  }

  // test on adding things to the head
  void testaddHead(Tester t) {
    initData();

    t.checkExpect(this.deque1.header.next, this.deque1.header);
    t.checkExpect(this.deque2.header.next, this.abc);

    deque1.addAtHead("test");
    deque2.addAtHead("jab");

    t.checkExpect(this.deque1.header.next,
        new Node<String>("test", this.deque1.header, this.deque1.header));
    t.checkExpect(this.deque2.header.next, new Node<String>("jab", abc, this.deque2.header));
  }

  // test for adding to the tail
  void testaddToTail(Tester t) {
    initData();

    t.checkExpect(this.deque1.header.prev, this.deque1.header);
    t.checkExpect(deque2.header.prev, def);

    deque1.addAtTail("test");
    deque2.addAtTail("efg"); // the next expected

    t.checkExpect(this.deque1.header.prev,
        new Node<String>("test", this.deque1.header, this.deque1.header));
    t.checkExpect(this.deque2.header.prev, new Node<String>("efg", Str1, this.def));
  }

  // test for the add and remove form the head
  void testAddRemove(Tester t) {
    initData();
    this.deque1.addAtHead("john");
    this.deque1.addAtHead("peter");
    this.deque1.addAtTail("albert");
    this.deque1.addAtTail("gerald");

    Sentinel<String> S4 = new Sentinel<String>();
    Node<String> Nd41 = new Node<String>("peter", S4, S4);
    Node<String> Nd42 = new Node<String>("john", S4, Nd41);
    Node<String> Nd43 = new Node<String>("albert", S4, Nd42);
    Node<String> Nd44 = new Node<String>("gerald", S4, Nd43);
    Deque<String> deque4 = new Deque<String>(S4);
    t.checkExpect(this.deque1, deque4);

    this.deque1.removeFromHead();
    this.deque1.removeFromTail();
    Sentinel<String> S5 = new Sentinel<String>();
    Node<String> N50 = new Node<String>("john", S5, S5);
    Node<String> N51 = new Node<String>("albert", S5, N50);
    Deque<String> deque5 = new Deque<String>(S5);
    t.checkExpect(this.deque1, deque5);
  }

  // test for finding and removing things
  void testFindRemove(Tester t) {
    initData();
    t.checkExpect(this.deque1.find(new IsString()), this.deque1.header);
    t.checkExpect(this.deque2.find(new IsString()), this.abc);
    t.checkExpect(this.deque2.find(new IsSarah()), this.Str1);
    t.checkExpect(this.deque3.find(new GrtSix()), this.Nd33);
    Sentinel<String> S6 = new Sentinel<String>();
    Node<String> N61 = new Node<String>("abc", S6, S6);
    Node<String> N62 = new Node<String>("bcd", S6, N61);
    Node<String> N63 = new Node<String>("cde", S6, N62);
    Node<String> N64 = new Node<String>("def", S6, N63);
    Deque<String> deque6 = new Deque<String>(S6);
    this.deque2.removeNode(new Node<String>("john"));
    t.checkExpect(this.deque2, deque6);
    Sentinel<String> S7 = new Sentinel<String>();
    Node<String> N71 = new Node<String>("abc", S7, S7);
    Node<String> N72 = new Node<String>("bcd", S7, N71);
    Node<String> N73 = new Node<String>("def", S7, N72);
    Deque<String> deque7 = new Deque<String>(S7);
    this.deque2.removeNode(cde);
    t.checkExpect(this.deque2, deque7);
    Sentinel<Integer> S8 = new Sentinel<Integer>();
    Node<Integer> N81 = new Node<Integer>(53, S8, S8);
    Node<Integer> N82 = new Node<Integer>(99, S8, N81);
    Node<Integer> N83 = new Node<Integer>(60, S8, N82);
    Node<Integer> N84 = new Node<Integer>(54, S8, N83);
    Deque<Integer> deque8 = new Deque<Integer>(S8);
    this.deque3.removeNode(Nd32);
    t.checkExpect(this.deque3, deque8);
  }
}
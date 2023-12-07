import tester.Tester;

/*To do List
 * create data definitions - done
 * create examples and tests - done
 * design methods with stubs - done
 * add comments above check expects saying what you are testing - done 
 * add comments in the code - done
 * need a way to add a list to a new list
 * implement the examples from the self checks - done
 */

interface ILoString {
  /*
   * fields:
   * 
   * 
   * methods: sort ... LOS 
   * toLowerCase ... String 
   * isSorted ... boolean
   * interleave... LoS 
   * merge ... LOS (do not use sort)
   *  reverse ...LOS
   * isDoubledList ...boolean 
   * isPalidromeList ... boolean 
   * Int compareTo ... Int
   */

  ILoString sort();

  boolean isSorted();

  ILoString interleave(ILoString other);

  // takes a sorted list and a given sorted list ad proguces a new sorted LoS
  ILoString merge(ILoString that);

  // helper: checks if the given string comes before the given list cause of
  // alphabet
  ILoString mergeHep(ILoString acc);

  // helper that helps merge
  ILoString insert(String s);

  ILoString reverse();

  // helper: for helping reverse
  ILoString reverseHep(ILoString acc);

  // helper to check if something is sorted
  boolean isSortedHelper(String s);

  boolean isDoubledList();

  // helper for doubledlist
  boolean isDoubledhelp(String s);

  boolean isPalindromeList();

  boolean sameList(ILoString other);

  boolean sameListHelp(String s, ILoString that);
}

//represents an empty list of string
class MtLoString implements ILoString {
  /*
   * fields:
   *
   * methods: 
   * sort ... ILoString 
   * toLowerCase ... String 
   * isSorted ... boolean
   * interleave... ILoString
   *  merge ... ILoString (do not use sort)
   *  mergehep...ILoString 
   *  reverse ...ILoString
   *  reversehelp ... ILoString 
   * isDoubledList...boolean
   *  DoubledLIstHelp ... boolean 
   * isPalidromeList ... boolean
   */

  // Sorts a empty list of strings and returns a sorted empty list
  @Override
  public ILoString sort() {
    // sorting method
    return this;
  }

  // interleaves an empty list with another list
  @Override
  public ILoString interleave(ILoString that) {
    return that;
  }

  // merges an empty list of strings with another list
  @Override
  public ILoString merge(ILoString that) {
    // implement merge method
    return that;
    // return that.mergeHep(this, str new MtLoString());
  }

  // merge helper
  @Override
  public ILoString mergeHep(ILoString acc) {
    return acc;
    // return acc.insert(str);
  }

  // inserts the new string to the list
  @Override
  public ILoString insert(String s) {
    return new ConsLoString(s, this);
  }

  // Reverses a empty list but will return a empty list
  @Override
  public ILoString reverse() {
    return this;
  }

  // helper for reverse
  @Override
  public ILoString reverseHep(ILoString acc) {
    return acc;
  }

  // checks if a empty list of strings is sorted
  @Override
  public boolean isSorted() {
    return true;
    // returns true because an empty is sorted
  }

  // helper for isSorted
  @Override
  public boolean isSortedHelper(String s) {
    return true;
  }

  // determines if an empty list has identical strings
  @Override
  public boolean isDoubledList() {
    return true;
  }

  // helper
  @Override
  public boolean isDoubledhelp(String s) {
    return false;
  }

  // checks if an empty list of strings is a Pall
  @Override
  public boolean isPalindromeList() {
    return true;
  }

  @Override
  public boolean sameList(ILoString that) {
    return that.sameListHelp(null, this);
  }

  public boolean sameListHelp(String s, ILoString that) {
    return s == null;
  }
}

//class that represents Strings
class ConsLoString implements ILoString {
  String first;
  ILoString rest;

  ConsLoString(String first, ILoString rest) {
    this.first = first;
    this.rest = rest;
  }

  /*
   * fields:
   * this.first ... String
   * this.rest ... ILoString
   * 
   * methods:
   *  sort ... ILoString
   *   isSorted ... boolean 
   *   isSortedHelp ... boolean
   * interleave... ILoString 
   * merge ... ILoString (do not use sort)
   *  mergehelper ...ILoString 
   *  reverse ...ILoString 
   *  reverseHep ... ILoString 
   *  isDoubledList ...boolean 
   *  isDoubledListHelp ... boolean 
   *  isPalindromeList ... boolean
   *  sameList...boolean 
   *  sameListHelper .. boolean 
   *  
   *  methods for fields:
   * this.rest.sort() ... ILoString 
   * this.rest.isSorted() ... ILoString 
   * this.rest.isSortedHelp ...boolean
   * this.rest.interleave ... ILoString 
   * this.rest.merge ... ILoString
   * this.rest.mergehelper ... ILoString 
   * this.rest.reverse ... ILoString
   * this.rest.reverseHep ... ILoString 
   * this.rest.isDoubledList ... boolean
   * this.rest.isDoubledListHelp ... boolean
   *  this.rest.isPalindome ... boolean
   * this.rest.sameList ...boolean
   * this.rest.sameListhelper ... boolean
   * other.sort() ... ILoString 
   * other.sort() ... ILoString 
   * other.isSorted() ...ILoString 
   * other.isSortedHelp ... boolean 
   * other.interleave ... ILoString
   * other.merge ... ILoString 
   * other.mergehelper ... ILoString 
   * other.reverse ...ILoString 
   * other.reverseHep ... ILoString
   *  other.isDoubledList ... boolean
   * other.isDoubledListHelp ... boolean 
   * other.isPalindome ... boolean
   * other.sameList ... boolean 
   * other.sameListHelper ... boolean
   * 
   */

  // sorts a list in alphabetical order
  @Override
  public ILoString sort() {
    // sorting method
    return this.rest.sort().insert(this.first);
  }

  // takes a ILoString and gives a list where 1st, 2rd, 5th, and 2nd and 4th and
  // creates a new list
  @Override
  public ILoString interleave(ILoString that) {
    return new ConsLoString(this.first, that.interleave(this.rest));
    // would represent the new list
  }

  // merges two list together
  @Override
  public ILoString merge(ILoString that) {
    return this.mergeHep(that);
  }

  // checks if a string comes before a given list
  @Override
  public ILoString mergeHep(ILoString acc) {
    return this.rest.mergeHep(acc.insert(this.first));
  }

  // compare the string to the first to see where to place it
  @Override
  public ILoString insert(String s) {
    if (s.toLowerCase().compareTo(this.first.toLowerCase()) <= 0) {
      return new ConsLoString(s, this);
    }
    else {
      return new ConsLoString(this.first, this.rest.insert(s));
    }
  }

  // returns the reverse of a list
  @Override
  public ILoString reverse() {
    return this.reverseHep(new MtLoString());
  }

  // helper for the reverse method
  @Override
  public ILoString reverseHep(ILoString acc) {
    return rest.reverseHep(new ConsLoString(this.first, acc));
  }

  // checks if s list is sorted
  @Override
  public boolean isSorted() {
    // calls the helper to see if the list is sorted then will return true
    return this.rest.isSortedHelper(this.first); // stub
  }

  // helper to the sorted list
  @Override
  public boolean isSortedHelper(String s) {
    return this.first.toLowerCase().compareTo(s.toLowerCase()) >= 0
        // if greater than after the string
        && this.rest.isSortedHelper(this.first);
  }

  // checks is a list has identical strings
  @Override
  public boolean isDoubledList() {
    return this.rest.isDoubledhelp(this.first);
  }

  @Override
  public boolean isDoubledhelp(String s) {
    return s.equals(this.first) && this.rest.isDoubledList();
  }

  // checks if a list has the same words read in either order
  @Override
  public boolean isPalindromeList() {
    return this.reverse().sameList(this);
  }

  @Override
  // a helper to palindrome to see if the other word is in the list
  public boolean sameList(ILoString that) {
    return that.sameListHelp(this.first, this.rest);
  }

  // a helper to the helper to see if the first is in the list
  public boolean sameListHelp(String s, ILoString that) {
    return s.equals(this.first) && that.sameList(this.rest);
  }
}

class SExamples {
  // create lists of strings with the empty space
  ILoString emptyLst = new MtLoString();
  ILoString emptyLst2 = new MtLoString();

  ILoString lista = new ConsLoString("bee", new ConsLoString("lamp", new MtLoString()));

  ILoString list1 = new ConsLoString("falling", new ConsLoString("spark", new ConsLoString("river",
      new ConsLoString("tower", new ConsLoString("asphalt", new MtLoString())))));

  ILoString list2 = new ConsLoString("water", new ConsLoString("library", new ConsLoString("notes",
      new ConsLoString("butter", new ConsLoString("sweater", new MtLoString())))));

  ILoString list3 = new ConsLoString("Light",
      new ConsLoString("RED", new ConsLoString("Socks", new ConsLoString("Fountain",
          new ConsLoString("PIANO", new ConsLoString("airplane", new MtLoString()))))));

  ILoString list4 = new ConsLoString("fold", new ConsLoString("chance", new ConsLoString("GATE",
      new ConsLoString("luminous", new ConsLoString("listen", new MtLoString())))));

  ILoString list5 = new ConsLoString("blue",
      new ConsLoString("blue", new ConsLoString("red", new ConsLoString("red",
          new ConsLoString("purple", new ConsLoString("purple", new MtLoString()))))));

  ILoString list6 = new ConsLoString("glasses", new ConsLoString("cube", new ConsLoString("steak",
      new ConsLoString("cube", new ConsLoString("glasses", new MtLoString())))));

  ILoString list7 = new ConsLoString("k", new ConsLoString("b", new MtLoString()));
  ILoString list8 = new ConsLoString("l", new ConsLoString("y", new MtLoString()));
  ILoString list9 = new ConsLoString("paper", new ConsLoString("pencil", new MtLoString()));
  ILoString list10 = new ConsLoString("c", new MtLoString());

  // expected results below:
  ILoString sortempty = emptyLst.sort();

  ILoString expectL1 = new ConsLoString("asphalt",
      new ConsLoString("falling", new ConsLoString("river",
          new ConsLoString("spark", new ConsLoString("tower", new MtLoString())))));

  ILoString l2rv = new ConsLoString("sweater", new ConsLoString("butter", new ConsLoString("notes",
      new ConsLoString("lirbrary", new ConsLoString("water", new MtLoString())))));

  ILoString expectL34 = new ConsLoString("Light", new ConsLoString("fold", new ConsLoString("RED",
      new ConsLoString("chance", new ConsLoString("Socks", new ConsLoString("GATE",
          new ConsLoString("Fountain", new ConsLoString("luminous", new ConsLoString("PIANO",
              new ConsLoString("listen", new ConsLoString("airplace", new MtLoString())))))))))));

  ILoString l13merge = new ConsLoString("falling", new ConsLoString("spark", new ConsLoString(
      "river",
      new ConsLoString("tower", new ConsLoString("asphalt", new ConsLoString("water",
          new ConsLoString("library", new ConsLoString("notes", new ConsLoString("Light",
              new ConsLoString("RED",
                  new ConsLoString("Socks", new ConsLoString("Fountain", new ConsLoString("PIANO",
                      new ConsLoString("airplane", new MtLoString()))))))))))))));

  ILoString l24merge = new ConsLoString("water", new ConsLoString("library",
      new ConsLoString("notes", new ConsLoString("butter", new ConsLoString("sweater",
          new ConsLoString("fold", new ConsLoString("chance", new ConsLoString("GATE",
              new ConsLoString("luminous", new ConsLoString("listen", new MtLoString()))))))))));

  ILoString l78 = new ConsLoString("k",
      new ConsLoString("l", new ConsLoString("b", new ConsLoString("y", new MtLoString()))));
  ILoString l785 = new ConsLoString("k",
      new ConsLoString("l", new ConsLoString("b", new ConsLoString("y", new MtLoString()))));
  ILoString l1and5 = new ConsLoString("asphalt",
      new ConsLoString("blue",
          new ConsLoString("blue", new ConsLoString("falling", new ConsLoString("purple",
              new ConsLoString("red", new ConsLoString("red", new ConsLoString("river",
                  new ConsLoString("spark", new ConsLoString("tower", new MtLoString()))))))))));

  ILoString nonempt = new ConsLoString("blue", new MtLoString());
  ILoString inter1 = new ConsLoString("paper",
      new ConsLoString("c", new ConsLoString("pencil", new MtLoString())));

  
  // checks the methods
  boolean testStrings(Tester t) {
    // test 1: test the sort method
    // call the sort method on list1
    return t.checkExpect(list1.sort(), expectL1)
        // test 2: test sorting an empty list
        && t.checkExpect(emptyLst.isSorted(), true)
        // test 3: an unsorted list should return false
        && t.checkExpect(list2.isSorted(), false)
        // test 4: a sorted list that returns true
        && t.checkExpect(expectL1.isSorted(), true)
        // test 5: test the interleave method of L3 & L4
        && t.checkExpect(list3.interleave(list4), expectL34)
        // test 6: test the reverse method
        && t.checkExpect(list2.reverse(), l2rv)
        // test 7: test reversing a reverse
        && t.checkExpect(l2rv.reverse(), list2)
        // test 8: test the merge
        && t.checkExpect(list1.merge(list3), l13merge)
        // test 9: test a second merge
        && t.checkExpect(list2.merge(list4), l24merge)
        // test 10: test is Doubledlist
        && t.checkExpect(list5.isDoubledList(), true)
        // test 11: test is second DoubledList
        && t.checkExpect(list1.isDoubledList(), false)
        // test 12: test a isPalindromeList
        && t.checkExpect(list6.isPalindromeList(), true)
        // test 13: test second isPalindromeList
        && t.checkExpect(list3.isPalindromeList(), false)
        // test 14, 15, 16 on interleave with empty and non empty
        && t.checkExpect(emptyLst.interleave(nonempt), nonempt)
        && t.checkExpect(list7.interleave(list8), l78)
        && t.checkExpect(list9.interleave(list10), inter1)
        // test merge section
        && t.checkExpect(emptyLst.merge(lista), lista) // tests an empty with nonempty
        // tests merge of 2 empty
        && t.checkExpect(emptyLst.merge(emptyLst2), emptyLst)
        // test merging of an nonempty and empty
        && t.checkExpect(lista.merge(emptyLst), lista)
        // test merge of nonempty with a sorted nonempty sorted
        && t.checkExpect(list7.merge(list8), l785) && t.checkExpect(list1.merge(list5), l1and5)
        && t.checkExpect(emptyLst.isPalindromeList(), true)
        && t.checkExpect(list7.isPalindromeList(), true)
        && t.checkExpect(list8.isPalindromeList(), false);
  }

  SExamples() {
  }
}
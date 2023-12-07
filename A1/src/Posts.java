class Post {
  String user;
  String text;
  int likes;
  int timeStamp;

  Post(String user, String text, int likes, int timeStamp) {
    this.user = user;
    this.text = text;
    this.likes = likes;
    this.timeStamp = timeStamp;
  }
}

class ExamplesPost {
  Post personalNews = new Post("iheartfundies",
      "Some personal news: I will be " + "taking fundies 2 this fall", 200, 1625699955);
  Post cupcakeAd = new Post("thequeenscups", "life is too short to not eat cupcakes", 48,
      1631661555);
  Post inspirational = new Post("anonymous",
      "King said injustice anywhere is " + "a threat to justice everywhere", 300000000, 1631661555);

  ExamplesPost() {
  }
}
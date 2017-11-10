// Assignment 10 - Problem 2
// Der Jennifer
// jennder
// Tseng Ming-Chiang
// ming

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import tester.*;
import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;

// to represent a Node
class Vertex {
  int x;
  int y;
  ArrayList<Edge> edges;

  Vertex(int x, int y, ArrayList<Edge> edges) {
    this.x = x;
    this.y = y;
    this.edges = edges;
  }

  Vertex(int x, int y) {
    this.x = x;
    this.y = y;
    this.edges = new ArrayList<Edge>();
  }

  // check if two nodes are the same
  public boolean equals(Object other) {
    if (other instanceof Vertex) {
      Vertex target = (Vertex) other;
      return this.x == target.x && this.y == target.y;
    }
    else {
      return false;
    }
  }

  // override the hashcode of this object
  public int hashCode() {
    return this.x * 3 + this.y * 5;
  }

  // draw this node
  WorldImage draw() {
    WorldImage base = new RectangleImage(Maze.scale, Maze.scale, "outline", Color.LIGHT_GRAY);
    if (!this.hasEdge("left")) {
      base = new OverlayOffsetImage(new RectangleImage(1, Maze.scale, "Solid", Color.black),
          Maze.scale / 2 - 0.5, 0, base);
    }
    if (!hasEdge("up")) {
      base = new OverlayOffsetImage(new RectangleImage(Maze.scale, 1, "Solid", Color.black), 0,
          Maze.scale / 2 - 0.5, base);
    }
    if (!hasEdge("down")) {
      base = new OverlayOffsetImage(new RectangleImage(Maze.scale, 1, "Solid", Color.black), 0,
          -(Maze.scale / 2 - 0.5), base);
    }
    if (!hasEdge("right")) {
      base = new OverlayOffsetImage(new RectangleImage(1, Maze.scale, "Solid", Color.black),
          -(Maze.scale / 2 - 0.5), 0, base);
    }
    return base;
  }

  // check if this cell has an edge to each side
  boolean hasEdge(String s) {
    if (s.equals("left")) {
      for (Edge e : this.edges) {
        if (e.to.x < this.x) {
          return true;
        }
      }
    }
    if (s.equals("right")) {
      for (Edge e : this.edges) {
        if (e.to.x > this.x) {
          return true;
        }
      }
    }
    if (s.equals("up")) {
      for (Edge e : this.edges) {
        if (e.to.y < this.y) {
          return true;
        }
      }
    }
    if (s.equals("down")) {
      for (Edge e : this.edges) {
        if (e.to.y > this.y) {
          return true;
        }
      }
    }
    return false;
  }

}

// to represent an edge
class Edge {
  Vertex from;
  Vertex to;
  int weight;

  Edge(Vertex from, Vertex to) {
    this.from = from;
    this.to = to;
    this.weight = new Random().nextInt(300);
  }

  Edge(Vertex from, Vertex to, int weight) {
    this.from = from;
    this.to = to;
    this.weight = weight;
  }

  WorldImage draw() {
    if ((this.from.x - this.to.x) == 0) {
      return new RectangleImage(1, Maze.scale, "solid", Color.red);
    }
    else {
      return new RectangleImage(Maze.scale, 1, "solid", Color.red);
    }
  }
}

// player class
class Player {
  int x;
  int y;

  Player(int x, int y) {
    this.x = x;
    this.y = y;
  }

  // draw the player
  WorldImage draw() {
    return new RectangleImage(Maze.scale, Maze.scale, "solid", Color.green);
  }

  // moves the player according to the keyevents
  Vertex move(String s, ArrayList<Vertex> nodes) {
    Vertex target = new Vertex(0, 0);
    for (Vertex n : nodes) {
      if (n.x == x && n.y == y) {
        target = n;
      }
    }
    if (s.equals("up")) {
      if (target.hasEdge("up")) {
        this.y = y - 1;
      }
    }
    if (s.equals("down")) {
      if (target.hasEdge("down")) {
        this.y = y + 1;
      }
    }
    if (s.equals("left")) {
      if (target.hasEdge("left")) {
        this.x = x - 1;
      }
    }
    if (s.equals("right")) {
      if (target.hasEdge("right")) {
        this.x = x + 1;
      }
    }
    return target;
  }
}

// target class
class Target {
  int x;
  int y;

  Target(int x, int y) {
    this.x = x;
    this.y = y;
  }

  // draw the target
  WorldImage draw() {
    return new RectangleImage(Maze.scale, Maze.scale, "solid", new Color(150, 100, 255));
  }
}

// ArrayUtils class
class ArrayUtils {
  <T> void swap(ArrayList<T> alist, int ind1, int ind2) {
    T temp = alist.get(ind1);
    alist.set(ind1, alist.get(ind2));
    alist.set(ind2, temp);
  }

  // sort the list via selection sort
  <T> void sort(ArrayList<T> arr, IComparator<T> comp) {
    for (int i = 0; i < arr.size(); i = i + 1) {
      int indofMin = this.findMinIndex(arr, i, comp);
      this.swap(arr, i, indofMin);
    }
  }

  // find the index of the minimum item starting from
  <T> int findMinIndex(ArrayList<T> arr, int i, IComparator<T> comp) {
    int currentMin = i;
    for (int g = i + 1; g < arr.size(); g = g + 1) {
      if (comp.compare(arr.get(currentMin), arr.get(g)) > 0) {
        currentMin = g;
      }
    }
    return currentMin;
  }
}

interface IComparator<T> {
  int compare(T t1, T t2);
}

class EdgeComp implements IComparator<Edge> {
  public int compare(Edge e1, Edge e2) {
    return e1.weight - e2.weight;
  }
}

class IntegerComp implements IComparator<Integer> {
  public int compare(Integer e1, Integer e2) {
    return e1 - e2;
  }

}

// to represent a Graph
class Maze extends World {
  ArrayList<Vertex> allNodes;
  ArrayList<Edge> edges;
  int width;
  int height;
  String draw;
  Player player;
  static final int scale = 30;
  ArrayList<Vertex> seen;
  ArrayList<Vertex> searchPath;
  Target target;
  ArrayList<Vertex> solution;
  HashMap<Vertex, Vertex> parents;
  boolean viewPath;
  boolean solved;

  Maze(int width, int height) {
    this.width = width;
    this.height = height;
    this.draw = "background";
    this.initialize();
  }

  void initialize() {
    ArrayList<ArrayList<Vertex>> nodes = new ArrayList<ArrayList<Vertex>>();
    allNodes = new ArrayList<Vertex>();
    ArrayList<Edge> allEdges = new ArrayList<Edge>();
    this.player = new Player(0, 0);
    this.target = new Target(width - 1, height - 1);
    this.seen = new ArrayList<Vertex>();
    this.solution = new ArrayList<Vertex>();
    this.searchPath = new ArrayList<Vertex>();
    this.parents = new HashMap<Vertex, Vertex>();
    this.viewPath = true;
    this.solved = false;

    // initializing the double arrayList with allNodes
    for (int i = 0; i < width; i = i + 1) {
      nodes.add(new ArrayList<Vertex>());
      for (int g = 0; g < height; g = g + 1) {
        nodes.get(i).add(new Vertex(i, g));
      }
    }

    // fixing all the edges
    for (int i = 0; i < width; i = i + 1) {
      for (int g = 0; g < height; g = g + 1) {
        if (i != width - 1) {
          allEdges.add(new Edge(nodes.get(i).get(g), nodes.get(i + 1).get(g)));
        }
        if (g != height - 1) {
          allEdges.add(new Edge(nodes.get(i).get(g), nodes.get(i).get(g + 1)));
        }
      }
    }
    // adding all the nodes to become ArrayList<Node>
    for (int i = 0; i < width; i = i + 1) {
      for (int g = 0; g < height; g = g + 1) {
        allNodes.add(nodes.get(i).get(g));
      }
    }
    // find the minimum spanning tree of all Edges
    this.edges = minSpanTree(allEdges);
  }

  // finding minimum spanning tree for all Edges
  ArrayList<Edge> minSpanTree(ArrayList<Edge> allEdges) {
    // creating a empty HashMap
    HashMap<Vertex, Vertex> representatives = new HashMap<Vertex, Vertex>();
    // adds all the nodes into the empty hashMap
    for (Vertex n : this.allNodes) {
      representatives.put(n, n);
    }

    // initializing the minimum spanning tree
    ArrayList<Edge> edgesInTree = new ArrayList<Edge>();

    // sort all edges
    new ArrayUtils().sort(allEdges, new EdgeComp());

    // iterates through the list of all edges to run the Kruskal's algorithm
    Iterator<Edge> workIterator = allEdges.iterator();
    while (!isOneTree(representatives)) {
      Edge nextEdge = workIterator.next();

      Vertex fromRep = find(representatives, nextEdge.from);
      Vertex toRep = find(representatives, nextEdge.to);

      if (!fromRep.equals(toRep)) {
        nextEdge.from.edges.add(nextEdge);
        nextEdge.to.edges.add(new Edge(nextEdge.to, nextEdge.from, nextEdge.weight));
        edgesInTree.add(nextEdge);
        union(representatives, fromRep, toRep);
      }
    }
    return edgesInTree;
  }

  // check if the representatives are all in the same tree
  boolean isOneTree(HashMap<Vertex, Vertex> representatives) {
    boolean isOneTree = true;
    Vertex rep = find(representatives, this.allNodes.get(0));
    for (Vertex n : this.allNodes) {
      isOneTree = isOneTree && find(representatives, n).equals(rep);
    }
    return isOneTree;
  }

  // find the oldest node representative
  Vertex find(HashMap<Vertex, Vertex> representatives, Vertex n) {
    Vertex source = n;
    Vertex target = representatives.get(n);
    while (!source.equals(target)) {
      source = target;
      target = representatives.get(target);
    }
    return target;
  }

  // chenge the oldest representative to the given one
  // EFFECT: change the oldest representative
  void union(HashMap<Vertex, Vertex> representatives, Vertex x, Vertex y) {
    representatives.put(x, representatives.get(y));
  }

  // Depth-first search
  boolean hasPathD(Vertex from, Vertex to) {
    ArrayList<Vertex> worklist = new ArrayList<Vertex>();
    // Initialize the worklist with the from vertex
    worklist.add(0, from);
    // As long as the worklist isn't empty...
    while (worklist.size() > 0) {
      Vertex current = worklist.remove(0);
      if (current.equals(to)) {
        return true; // Success!
      }
      else if (!searchPath.contains(current)) {
        // add all the neighbors of next to the worklist for further processing
        for (Edge e : current.edges) {
          worklist.add(0, e.to);
          if (!parents.containsKey(e.to)) {
            parents.put(e.to, current);

          }
        }
        this.searchPath.add(current);
      }
    }
    return false;
  }

  // Breath-first search
  boolean hasPathB(Vertex from, Vertex to) {
    ArrayList<Vertex> worklist = new ArrayList<Vertex>();
    // Initialize the worklist with the from vertex
    worklist.add(from);
    // As long as the worklist isn't empty...
    while (worklist.size() > 0) {
      Vertex current = worklist.remove(0);
      if (current.equals(to)) {

        return true; // Success!
      }
      else if (!searchPath.contains(current)) {
        // add all the neighbors of next to the worklist for further processing
        for (Edge e : current.edges) {
          worklist.add(e.to);
          if (!parents.containsKey(e.to)) {
            parents.put(e.to, current);
          }
        }
        // add next to alreadySeen, since we're done with it
        this.searchPath.add(current);

      }
    }
    return false;
  }

  // get the solution path
  void getSolution() {
    Vertex p = this.allNodes.get(allNodes.size() - 1);
    Vertex s = this.parents.get(p);
    solution.add(p);
    while (!p.equals(this.seen.get(0))) {
      this.solution.add(s);
      p = s;
      s = this.parents.get(p);
    }
  }

  // draw the world
  public WorldScene makeScene() {
    WorldScene board = new WorldScene(width * scale, height * scale);

    board.placeImageXY(new RectangleImage(width * scale, height * scale, "solid", Color.LIGHT_GRAY),
        width * scale / 2, height * scale / 2);

    if (this.viewPath) {
      // draw all the visited Nodes
      for (Vertex n : this.seen) {
        board.placeImageXY(new RectangleImage(scale - 1, scale - 1, "solid", Color.pink),
            n.x * scale + scale / 2 + 1, n.y * scale + scale / 2 + 1);
      }
    }

    board.placeImageXY(target.draw(), target.x * scale + scale / 2, target.y * scale + scale / 2);
    board.placeImageXY(player.draw(), player.x * scale + scale / 2, player.y * scale + scale / 2);

    // draw all the visited Nodes
    for (Vertex n : this.solution) {
      board.placeImageXY(new RectangleImage(scale - 1, scale - 1, "solid", Color.pink),
          n.x * scale + scale / 2 + 1, n.y * scale + scale / 2 + 1);
    }
    // draw the node with blocked edges
    for (Vertex n : this.allNodes) {
      board.placeImageXY(n.draw(), n.x * scale + scale / 2, n.y * scale + scale / 2);
    }

    // draw the solution path once we found it
    for (Vertex n : this.solution) {
      board.placeImageXY(new RectangleImage(scale - 1, scale - 1, "solid", Color.blue),
          n.x * scale + scale / 2 + 1, n.y * scale + scale / 2 + 1);
    }

    // display the solved maze image
    if (this.solved) {
      board.placeImageXY(new TextImage("MAZE SOLVED", 40, Color.RED), 400, 325);
    }
    // draws the minimum spanning tree
    if (this.draw.equals("edge")) {
      for (Edge e : this.edges) {
        if ((e.from.x - e.to.x) == 0) {
          board.placeImageXY(e.draw(), e.to.x * scale + scale / 2,
              (e.to.y * scale + e.from.y * scale) / 2 + scale / 2);
        }
        else {
          board.placeImageXY(e.draw(), (e.to.x * scale + e.from.x * scale) / 2 + scale / 2,
              e.to.y * scale + scale / 2);
        }
      }
    }
    return board;
  }

  // keyEvents
  public void onKeyEvent(String ke) {
    if (ke.equals("p")) {
      this.draw = "edge";
    }
    if (ke.equals("r")) {
      this.draw = "background";
    }
    if (ke.equals("n")) {
      this.initialize();
    }
    if (ke.equals("up")) {
      Vertex to = player.move("up", this.allNodes);
      if (!seen.contains(to) && (seen.size() > 0)) {
        parents.put(to, seen.get(seen.size() - 1));
      }
      if (seen.size() == 0) {
        parents.put(to, allNodes.get(0));
      }
      seen.add(to);

    }
    if (ke.equals("down")) {
      Vertex to = player.move("down", this.allNodes);
      if (!seen.contains(to) && (seen.size() > 0)) {
        parents.put(to, seen.get(seen.size() - 1));
      }
      if (seen.size() == 0) {
        parents.put(to, allNodes.get(0));

      }
      seen.add(to);

    }
    if (ke.equals("right")) {
      Vertex to = player.move("right", this.allNodes);
      if (!seen.contains(to) && (seen.size() > 0)) {
        parents.put(to, seen.get(seen.size() - 1));
      }
      if (seen.size() == 0) {
        parents.put(to, allNodes.get(0));
      }
      seen.add(to);

    }
    if (ke.equals("left")) {
      Vertex to = player.move("left", this.allNodes);
      if (!seen.contains(to) && (seen.size() > 0)) {
        parents.put(to, seen.get(seen.size() - 1));
      }
      if (seen.size() == 0) {
        parents.put(to, allNodes.get(0));
      }
      seen.add(to);

    }
    if (ke.equals("b")) {
      this.seen = new ArrayList<Vertex>();
      this.searchPath = new ArrayList<Vertex>();
      this.hasPathB(this.allNodes.get(0), this.allNodes.get(allNodes.size() - 1));

    }
    if (ke.equals("d")) {
      this.seen = new ArrayList<Vertex>();
      this.searchPath = new ArrayList<Vertex>();
      this.hasPathD(this.allNodes.get(0), this.allNodes.get(allNodes.size() - 1));

    }
    if (ke.equals("w")) {
      this.getSolution();
    }
    if (ke.equals("s")) {
      this.viewPath = !this.viewPath;
    }

  }

  // animates the search path
  public void onTick() {
    if (searchPath.size() > 0) {
      seen.add(searchPath.remove(0));
    }
    if ((this.player.x == this.target.x && this.player.y == this.target.y)) {
      parents.put(allNodes.get(allNodes.size() - 1), seen.get(seen.size() - 1));
      this.onKeyEvent("w");
      this.solved = true;
    }
  }

}

class ExamplesMaze {
  Maze maze1;
  Maze maze2;
  ArrayList<Integer> a1;
  ArrayList<Integer> a2;
  ArrayList<Integer> a3 = new ArrayList<Integer>();
  ArrayUtils utils = new ArrayUtils();
  IntegerComp intC = new IntegerComp();
  EdgeComp edgeC = new EdgeComp();
  Vertex n1 = new Vertex(0, 0, new ArrayList<Edge>());
  Vertex n2 = new Vertex(0, 1, new ArrayList<Edge>());
  Vertex n3 = new Vertex(1, 0, new ArrayList<Edge>());
  Vertex n4 = new Vertex(1, 1, new ArrayList<Edge>());
  Edge e1 = new Edge(n1, n2, 4);
  Edge e2 = new Edge(n1, n3, 8);
  Edge e3 = new Edge(n3, n4, 10);
  Edge e4 = new Edge(n2, n4, 5);
  Player p = new Player(0, 0);
  ArrayList<Vertex> aln = new ArrayList<Vertex>();
  ArrayList<Edge> ae = new ArrayList<Edge>(Arrays.asList(e1, e2, e3, e4));
  ArrayList<Edge> mae = new ArrayList<Edge>(Arrays.asList(e1, e4));
  HashMap<Vertex, Vertex> same = new HashMap<Vertex, Vertex>();
  HashMap<Vertex, Vertex> position = new HashMap<Vertex, Vertex>();

  void init() {
    this.maze1 = new Maze(2, 2);
    this.maze2 = new Maze(15, 20);
    this.a1 = new ArrayList<Integer>(Arrays.asList(1, 5, 4, 3, 2));
    this.a2 = new ArrayList<Integer>(Arrays.asList(5, 6, 7, 2, 4));
    this.n1.edges.add(e1);
    this.n1.edges.add(e2);
    this.aln.add(n1);
    this.aln.add(n2);
    this.aln.add(n3);
    this.aln.add(n4);
    for (Vertex v : this.aln) {
      same.put(v, v);
    }
    for (Vertex v : this.aln) {
      position.put(v, n4);
    }
  }

  // testing maze constructor which includes the method initialize()
  void testMaze(Tester t) {
    init();
    maze1.initialize();
    this.maze1.allNodes = aln;
    maze2.initialize();
    t.checkExpect(maze1.edges.size(), 3);
    t.checkExpect(maze1.player, new Player(0, 0));
    t.checkExpect(maze1.target, new Target(1, 1));
    t.checkExpect(maze1.seen, new ArrayList<Vertex>());
    t.checkExpect(maze2.edges.size(), 299);
    t.checkExpect(maze2.allNodes.size(), 300);
  }

  // testing methods in Nodes class
  void testNodes(Tester t) {
    this.init();
    t.checkExpect(n1.equals(n1), true);
    t.checkExpect(n1.equals(n2), false);
    t.checkExpect(n1.equals(e1), false);
    t.checkExpect(n1.hasEdge("left"), false);
    t.checkExpect(n1.hasEdge("right"), true);
    t.checkExpect(n1.hasEdge("down"), true);
    t.checkExpect(n1.hasEdge("up"), false);
    t.checkExpect(n3.hasEdge("up"), false);
    t.checkExpect(n1.hashCode(), 0);
    t.checkExpect(n4.hashCode(), 8);
  }

  // testing methods in Player Class
  void testPlayer(Tester t) {
    this.init();
    Vertex pLeft = p.move("left", this.aln);
    t.checkExpect(pLeft.x, 0);
    t.checkExpect(pLeft.y, 0);
    t.checkExpect(p, new Player(0, 0));
    Vertex pRight = p.move("right", this.aln);
    t.checkExpect(pRight.x, 0);
    t.checkExpect(pRight.y, 0);
    t.checkExpect(p, new Player(1, 0));
    Vertex pDown = p.move("down", this.aln);
    t.checkExpect(pDown.x, 1);
    t.checkExpect(pDown.y, 0);
    t.checkExpect(p, new Player(1, 0));
    Vertex pUp = p.move("up", this.aln);
    t.checkExpect(pUp.x, 1);
    t.checkExpect(pUp.y, 0);
    t.checkExpect(p, new Player(1, 0));
  }

  // testing the Utils class and comparator
  void testUtiles(Tester t) {
    init();
    utils.sort(a1, intC);
    utils.sort(a3, intC);
    utils.swap(a2, 0, 2);
    t.checkExpect(a1, new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5)));
    t.checkExpect(a2, new ArrayList<Integer>(Arrays.asList(7, 6, 5, 2, 4)));
    t.checkExpect(utils.findMinIndex(a2, 0, intC), 3);
    t.checkExpect(intC.compare(5, 2), 3);
    t.checkExpect(edgeC.compare(e1, e2), -4);
  }

  // testing minSpanningTree,isOneTree,find, Union
  void testMinSpanningTree(Tester t) {
    init();
    t.checkExpect(maze1.minSpanTree(this.ae).get(0), this.e1);
    t.checkExpect(maze1.minSpanTree(this.ae).get(1), this.e4);
    t.checkExpect(this.maze1.isOneTree(this.same), false);
    t.checkExpect(this.maze1.find(this.same, n1), n1);
    t.checkExpect(this.maze1.find(this.position, n3), n4);
    t.checkExpect(this.maze1.find(this.position, n1), n4);
    this.maze1.union(this.same, n1, n2);
    t.checkExpect(this.maze1.find(this.same, n1), n2);
    this.maze1.union(this.same, n2, n3);
    t.checkExpect(this.maze1.find(this.same, n1), n3);
  }

  // testing onKeys
  void testOnKey(Tester t) {
    init();
    this.maze1.allNodes = this.aln;
    this.maze1.edges = this.ae;
    this.maze1.onKeyEvent("down");
    this.maze1.onKeyEvent("right");
    this.maze1.onKeyEvent("p");
    this.maze1.onKeyEvent("s");
    t.checkExpect(this.maze1.draw, "edge");
    this.maze1.onKeyEvent("r");
    t.checkExpect(this.maze1.draw, "background");
    t.checkExpect(this.maze1.viewPath, false);
    t.checkExpect(this.maze1.seen.size(), 2);
    t.checkExpect(this.maze1.seen.contains(n3), false);
    t.checkExpect(this.maze1.parents.get(n1), n1);
    t.checkExpect(this.maze1.parents.get(n2), n1);
    t.checkExpect(this.maze1.parents.size(), 2);
  }

  // testing DFS and BFS
  void testSearch(Tester t) {
    init();
    this.maze1.allNodes = aln;
    this.maze1.edges = this.ae;
    this.maze1.minSpanTree(this.maze1.edges);
    t.checkExpect(this.maze1.edges.get(0), this.e1);
    t.checkExpect(this.maze1.edges.get(1), this.e4);
    t.checkExpect(this.maze1.hasPathD(n1, n4), true);
    t.checkExpect(this.maze1.hasPathD(n1, n3), false);
    t.checkExpect(
        this.maze2.hasPathB(maze2.allNodes.get(0), maze2.allNodes.get(maze2.allNodes.size() - 1)),
        true);
    t.checkExpect(this.maze1.parents.get(n2), n1);
    t.checkExpect(this.maze1.parents.get(n4), n2);
    t.checkExpect(this.maze1.seen.size(), 0);
  }

  // testing getSolution
  void testGetSolution(Tester t) {
    init();
    this.maze1.allNodes = aln;
    this.maze1.edges = this.ae;
    this.maze1.minSpanTree(this.maze1.edges);
    this.maze1.hasPathD(n1, n4);
    this.maze1.onTick();
    this.maze1.onTick();
    this.maze1.onTick();
    this.maze1.getSolution();
    t.checkExpect(this.maze1.seen.size(), 3);
    t.checkExpect(this.maze1.solution.size(), 3);
    t.checkExpect(this.maze1.solution, new ArrayList<Vertex>(Arrays.asList(n4,n2, n1)));
  }

  // world function
  // when run it with 100, 60 with scale 10 it takes a while to run
  // public static void main(String[] args) {
  // Maze w = new Maze(100, 60);
  // w.bigBang(1000, 600, 0.1);
  // }

  public static void main(String[] args) {
    Maze w = new Maze(30, 20);
    w.bigBang(900, 600, 0.01);
  }
}

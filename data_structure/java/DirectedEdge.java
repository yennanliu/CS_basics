// https://www.coursera.org/learn/algorithms-part2/lecture/e3UfD/shortest-paths-apis

public class DirectedEdge
{
  private final int v, w;
  private final double weight;

  public DirectedEdge(int v, int w, double weight)
  {
    this.v = v;
    this.w = w;
    this.weight = weight;
  }

  public int from()
  { return v ; }

  public int to()
  { return w; }

  public int weight()
  { return weight ; }

}
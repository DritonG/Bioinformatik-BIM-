import net.gumbix.dynpro.IntDynPro;
import net.gumbix.dynpro.Idx;
import scala.collection.immutable.List;

/**
 * @author Markus Gumbel (m.gumbel@hs-mannheim.de)
 */
public class Knapsack extends IntDynPro {

    public static void main(String[] args) {
        String[] rowLabels = {"A", "B", "C", "D"};
        int[] weights = {2, 2, 6, 5};
        int[] values = {6, 3, 5, 4};
        Knapsack dp = new Knapsack(rowLabels, weights, values, 10);
        // The maximum is expected at the last item (n-1)
        // with no capacity left (0);
        List solution = dp.solution(new Idx(weights.length - 1, 0));
        System.out.println(solution);
        System.out.println(dp.mkMatrixString(solution));
    }

    private String[] items;
    private int[] weights;
    private int[] values;
    private int capacity;

    public Knapsack(String[] items, int[] weights, int[] values,
                    int capacity) {
        this.items = items;
        this.weights = weights;
        this.values = values;
        this.capacity = capacity;
    }

    @Override
    public int n() {
        return weights.length;
    }

    @Override
    public int m() {
        return capacity + 1;
    }

    @Override
    public double value(Idx idx, Integer d) {
        return d * values[idx.i()];
    }

    /**
     * If the remaining capacity (idx.j) plus the weight that could be taken
     * is less than the overall capacity we could take it. Thus,  { 0, 1 }.
     * If not, we can only skip it (={0}).
     */
    @Override
    public Integer[] decisions(Idx idx) {
        if (idx.j() + weights[idx.i()] <= capacity) {
            return new Integer[]{0, 1};
        } else {
            return new Integer[]{0};
        }
    }

    /**
     * The prev. state is the previous item (idx.i-1) and the prev. capacity.
     * The prev. capacity is the remaining capacity (idx.j) plus weight that was
     * taken (or plus 0 if it was skipped).
     */
    @Override
    public Idx[] prevStates(Idx idx, Integer d) {
        if (idx.i() > 0) {
            Idx pidx = new Idx(idx.i() - 1, idx.j() + d * weights[idx.i()]);
            return new Idx[]{pidx};
        } else return new Idx[]{};
    }

    @Override
    public String[] rowLabels() {
        return items;
    }
}
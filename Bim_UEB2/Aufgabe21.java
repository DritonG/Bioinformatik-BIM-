import net.gumbix.dynpro.Idx;
import net.gumbix.dynpro.IntDynPro;
import net.gumbix.dynpro.PathEntry;
import scala.Option;
import scala.Some;
import scala.collection.immutable.List;
import scala.collection.script.Script;

/**
 * Created by d.goxhufi on 17.11.2015.
 */
public class Aufgabe21 extends IntDynPro {

    public static void main(String[] args) {
        String[] s = {"$", "C", "G", "A", "T", "C", "C", "T", "G", "T"};
        //------ test mit einer identischen sequenz ------------------
//        String[] t = {"$","C", "G", "A", "T", "C", "C", "T", "G", "T"};

        String[] t = {"$", "C", "A", "T", "C", "G", "C", "C", "T", "T"};
        //String[] rowLabels = {"A", "B", "C", "D"};

//        private String sS = s.toString();
//        private String tS = t.toString();

        Aufgabe21 dp = new Aufgabe21(s, t);
        // The maximum is expected at the last item (n-1)
        // with no capacity left (0);
//        System.out.println(solution);
        List<PathEntry<Integer>> solution = dp.solution(new Idx(s.length -1, t.length-1));
        System.out.println(solution);
        System.out.println(dp.mkMatrixString(solution));
        System.out.println("Die Ähnlichkeit beträgt: " + dp.extremeFunction(s.length,t.length));
    }

    // decisions
    public static final int B = 1;
    public static final int I = 123;
    public static final int DEL = 456;
    public static final int START = 0;
    public static final int GAP = -2;

    private String[] s;
    private String[] t;

//    private int sim;


    public Aufgabe21(String[] s, String[] t) {
        //die zwei Sequenzen s und t
        this.s = s;
        this.t = t;
    }

    @Override
    public int n() {
        return s.length;
    }

    @Override
    public int m() {
        return t.length;
    }

    @Override
    public double value(Idx idx, Integer d) {
        if (d.equals(START)) {
            return START;
        } else if (d.equals(DEL)) {
            //return g
            return +GAP;
        } else if (d.equals(I)) {
            return +GAP;
        } else if (d.equals(B)) {
            if (s[idx.i()].equalsIgnoreCase(t[idx.j()])) {
                return +B;
            } else return -1;
        }

        return 0;
        //return d * t[idx.i()];
    }


    /*Entscheidungstabelle, siehe Folie 40.17 */
    @Override
    public Integer[] decisions(Idx idx) {
        if (idx.i() == 0 && idx.j() == 0) {
            return new Integer[]{START};
        } else if (idx.i() == 0) {
            return new Integer[]{I};
        } else if (idx.j() == 0) {
            return new Integer[]{DEL};
        } else {
            return new Integer[]{I, DEL, B};
        }
    }

    @Override
    public Idx[] prevStates(Idx idx, Integer d) {
//        Idx sidx = new Idx(0, 0);
        Idx pidx;
        if (d.equals(I)) {
            pidx = new Idx(idx.i(), idx.j() - 1);
            return new Idx[]{pidx};
        } else if (d.equals(DEL)) {
            pidx = new Idx(idx.i() - 1, idx.j());
            return new Idx[]{pidx};
        } else if (d.equals(B)) {
            pidx = new Idx(idx.i() - 1, idx.j() - 1);
            return new Idx[]{pidx};
        } else return new Idx[]{};
    }

//    @Override
//    public double extremeFunction(double x1, double x2){
//        return super.extremeFunction(x1, x2);
//    }

//    @Override
//    public Formatter formatter(){
//        Formatter formatter = new Formatter();
//        return formatter;
//    }

    @Override
    public String[] rowLabels() {
        return s;
    }

    @Override
    public Option<String[]> columnLabels() {
        // Sei u ein Array der Länge m (Anzahl Spalten)
        return new Some<>(t);
    }
//    public int getSim(){
//        Idx simIdx = new Idx(t.length, s.length);
//        int sim = value(simIdx,d);
//        return 0;
//    }
}
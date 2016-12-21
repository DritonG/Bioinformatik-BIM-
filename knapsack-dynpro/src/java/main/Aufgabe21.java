import net.gumbix.dynpro.Idx;
import net.gumbix.dynpro.IntDynPro;
import net.gumbix.dynpro.PathEntry;
import scala.Option;
import scala.Some;
import scala.collection.immutable.List;

/**
 * Created by d.goxhufi on 17.11.2015.
 */
public class Aufgabe21 extends IntDynPro {

    public static void main(String[] args) {
        //---------------------- Nukleotide ------------------------
        String[] s = {"$", "C", "G", "A", "T", "C", "C", "T", "G", "T"};
        String[] t = {"$", "C", "A", "T", "C", "G", "C", "C", "T", "T"};
        //------ test mit einer identischen sequenz ------------------
//        String[] t = {"$","C", "G", "A", "T", "C", "C", "T", "G", "T"};

        // ---------------------- Aminosäuren -----------------------
//        String[] s = {"$", "K", "I", "Q", "Y", "K", "R", "E", "P", "N", "I", "P", "S", "V", "S", "L", "I", "N", "S", "L", "F", "A", "W", "E", "I", "R", "R", "I"};
//
//        String[] t = {"$", "K", "A", "Q", "Y", "R", "R", "E", "C", "M", "I", "F", "V", "W", "E", "I", "N", "R", "L"};


        Aufgabe21 dp = new Aufgabe21(s, t);
        Idx sim = new Idx(s.length - 1, t.length - 1);
        List<PathEntry<Integer>> solution = dp.solution(sim);
        System.out.println(solution);
        System.out.println(dp.mkMatrixString(solution));
    }

    // decisions
    public static final int START = 0; // Startwert 0
    public static final int B = 1; // Both, steht für das gleichzeitige abarbeiten der basen beider sequenzen s und t
    public static final int I = 123; // I(nsert) und DEL(etion) werden beides mit -2 bewertet welches dann als GAP initialisiert wird
    public static final int DEL = 456; // DEL
    public static final int GAP = -2; // Bewertung einer Lücke

    private String[] s;
    private String[] t;


    public Aufgabe21(String[] s, String[] t) {
        //die zwei Sequenzen s und t
        this.s = s;
        this.t = t;
    }

    // Größe der Matrix festlegen
    @Override
    public int n() {
        return s.length;
    }

    @Override
    public int m() {
        return t.length;
    }

    //Auffüllen der Matrix
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

    // Traceback function für die abfrage des vorherigen zustandes ausgehend von dem Zustand idx
    @Override
    public Idx[] prevStates(Idx idx, Integer d) {
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


    //Beschriftung der Zeilen mit der Sequenz s
    @Override
    public String[] rowLabels() {
        return s;
    }

    //Beschriftung der Spalten mit einer Sequenz t
    @Override
    public Option<String[]> columnLabels() {
        // Sei u ein Array der Länge m (Anzahl Spalten)
        return new Some<>(t);
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
}
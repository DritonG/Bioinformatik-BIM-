import net.gumbix.dynpro.Idx;
import net.gumbix.dynpro.IntDynPro;
import net.gumbix.dynpro.PathEntry;
import scala.Option;
import scala.Some;
import scala.collection.immutable.List;

/**
 * Created by d.goxhufi on 17.11.2015.
 */
public class HMM_Viterbi extends IntDynPro {
    public enum States {
        q0(0), Fair(1), Unfair(2);

        private final int value;

        States(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    ;


    public static void main(String[] args) {
        // Die Transitions- und Emissionswahrscheinlichkeiten sowie das Alphabet
        // und die Zustände
        // sollen als Parameter übergeben werden, d.h. der Algorithmus soll
        // modellunabhängig
        // realisiert werden
        // Spalten beschriftung mit den möglichen Würfelzuständen, Fair oder
        // Unfair
        // c = S = {s1,....,sn}; endliche Menge von Zuständen (500)
        String c = "622315363413156463666435546655213466653666626531521464516451551424332216461"
                + "654325215554335255644631643624431132536562636341621646461666646663353561415"
                + "616155466412211463366565342256566666616646465532466616661455236563266622152"
                + "611664243464131436536136654361545356246424353356265613422546614536625161435"
                + "436336162566466116635233424261641466666141266641652466655425442133555114266"
                + "256466454134436563466524135653536663326666626536663662536366456666456661655"
                + "26434466434465351111221411466464423316135345662264";

        // Alphabet = K= {k1,...,kM}; endliche Menge von Ausgabe Symbolen (1-6
        String[] alphabet = {"1", "2", "3", "4", "5", "6"};
        String[] diceState = {"Fair", "Unfair"};

        // Transitionswahrscheinlichkeiten
        double transition = 1 / 20;// der wechsel von F nach U oder umgekehrt,
        // kein würfelwechsel = 1-transition

        // Emissionswahrscheinlichkeiten
        double emissionF = 1 / 6;
        double emissionU = 1 / 10;// if c = 6 --> emissionU * 5 -1
        /*
         * if (F){ emission = 1/6; }else{ if(c == 6){ emission 1/2; } emission =
		 * 1/10; }
		 */
        // if state true = F(Fair) else U(Unfair)
        HMM_Viterbi dp = new HMM_Viterbi(c, diceState, alphabet, emissionF, emissionU,
                transition);

        // List<PathEntry<Integer>> solution = dp.solution(new Idx(c.length(),
        // 1));
        System.out.println(dp.mkMatrixString());

    }


    private String[] c;
    private String[] diceState;
    private String[] alphabet;
    private double transition;
    private double emissionF;
    private double emissionU;

    public HMM_Viterbi(String c, String[] diceState, String[] alphabet, double emissionF,
                       double emissionU, double transition) {
        this.c = c.split("");
        this.diceState = diceState;
        this.alphabet = alphabet;
        this.emissionF = emissionF;
        this.emissionU = emissionU;
        this.transition = transition;
    }

    // n -> Zeilenanzahl
    @Override
    public int n() {
        return c.length;
    }

    // m -> Spaltenanzahl
    @Override
    public int m() {
        return diceState.length;
    }

    // berrechnet Wert in Matrix an der Stelle idx
    @Override
    public double value(Idx idx, Integer d) {
        switch (States.values()[d]) {
            case q0:
                if (idx.j() == 0) {
                    return (1 / 2) * emissionF;
                } else if (idx.j() == 1) {
                    if (c[idx.i()].equalsIgnoreCase(alphabet[5])) {
                        return (1 / 2) * (1 - emissionU * 5);
                    } else {
                        return (1 / 2) * emissionU;
                    }
                } else {
                    return Math.log(1);
                }
            case Fair:
//                    System.out.println("Fair: "+ (idx.i()-1)*(1-transition) * emissionF);
                return (idx.i() - 1) * (1 - transition) * emissionF;
            case Unfair:
                if (c[idx.i()].equalsIgnoreCase(alphabet[5])) {
//                        System.out.println("Unfair(6)"+(idx.i()-1)*(1-transition)*(1-emissionU*5));
                    return (idx.i() - 1) * (1 - transition) * (1 - emissionU * 5);
                } else {
//                        System.out.println("Unfair(N)"+(idx.i()-1)*(1-transition)* emissionU);
                    return (idx.i() - 1) * (1 - transition) * emissionU;
                }
            default:
                return 0;
        }
    }

    /* Entscheidungstabelle, siehe Folie 40.17 */
    @Override
    public Integer[] decisions(Idx idx) {
        if (idx.i() == 0) {
            return new Integer[]{States.q0.getValue()};
        } else return new Integer[]{States.Fair.getValue(), States.Unfair.getValue()};
    }

    // Für die Rückrechnung
    @Override
    public Idx[] prevStates(Idx idx, Integer d) {
        Idx pidx;
        if (idx.i() > 0) {
            pidx = new Idx(idx.i() - 1, d);
            return new Idx[]{pidx};
        } else
            return new Idx[]{};
    }

    // @Override
    // public double extremeFunction(double x1, double x2){
    // return super.extremeFunction(x1, x2);
    // }

    // @Override
    // public Formatter formatter(){
    // }

    @Override
    public String[] rowLabels() {
        String[] rows = new String[c.length];

        for (int i = 0; i < c.length; ++i) {
            rows[i] = String.valueOf(c[i]);
        }
        return rows;
    }

    @Override
    public Option<String[]> columnLabels() {
        // Sei u ein Array der Länge m (Anzahl Spalten)
        return new Some<>(diceState);
    }

}

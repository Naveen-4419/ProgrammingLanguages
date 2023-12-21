import java.util.*;

class CrackerBoardMove
{
    public int CrackerBoardMovefrom;
    public int CrackerBoardMoveover;
    public int CrackerBoardMoveto;

    public CrackerBoardMove(int CrackerBoardMovefrom, int CrackerBoardMoveover, int CrackerBoardMoveto)
    {
        this.CrackerBoardMovefrom = CrackerBoardMovefrom;
        this.CrackerBoardMoveover = CrackerBoardMoveover;
        this.CrackerBoardMoveto = CrackerBoardMoveto;
    }

    public CrackerBoardMove reversed()
    { return new CrackerBoardMove(CrackerBoardMoveto, CrackerBoardMoveover, CrackerBoardMovefrom); }

    @Override
    public String toString()
    {
        return "(" + CrackerBoardMovefrom + ", " + CrackerBoardMoveover + ", " + CrackerBoardMoveto + ")";
    }
}

class CrackerBoard
{
    public int pegCount;
    public int[] cells;

    public CrackerBoard(int emptyCell)
    {
        pegCount = 14;
        cells = new int[15];
        int i = 0;
        while(i < 15){
            if(i == emptyCell){
                cells[i] = 0;
            }else{
                cells[i] = 1;
            }
            i++;
        }

    }

    public CrackerBoard(int crackerBarrelPegCount, int[] crackerBareellcells)
    {
        this.pegCount = crackerBarrelPegCount;
        this.cells    = crackerBareellcells.clone();
    }

    public CrackerBoard move(CrackerBoardMove m)
    {
        int i =1;
        int j = 0;
        if (cells[m.CrackerBoardMovefrom] == i &&
                cells[m.CrackerBoardMoveover] == i &&
                cells[m.CrackerBoardMoveto]   == j)
        {
            CrackerBoard crackerBoardAfter = new CrackerBoard(pegCount-1, cells.clone());
            crackerBoardAfter.cells[m.CrackerBoardMovefrom] = j;
            crackerBoardAfter.cells[m.CrackerBoardMoveover] = j;
            crackerBoardAfter.cells[m.CrackerBoardMoveto]   = i;

            return crackerBoardAfter;
        }

        return null;
    }
}

class StepIterator implements Iterator<CrackerBoardMove>
{
    private CrackerBoardMove[] crackerBoardMoves;
    private CrackerBoardMove reversed;
    private int    i;

    public StepIterator(CrackerBoardMove[] crackerBoardMoves)
    {
        this.crackerBoardMoves = crackerBoardMoves;
        this.i     = 0;
    }

    @Override
    public boolean hasNext()
    { return i < crackerBoardMoves.length || (i == crackerBoardMoves.length && reversed != null); }

    @Override
    public CrackerBoardMove next()
    {
        if (reversed != null)
        {
            CrackerBoardMove result = reversed;
            reversed = null;
            return result;
        }

        CrackerBoardMove m = crackerBoardMoves[i++];
        reversed = m.reversed();

        return m;
    }
}

class StepList implements Iterable<CrackerBoardMove>
{
    public static final CrackerBoardMove[] CRACKER_BOARD_MOVES =
            {
                    new CrackerBoardMove(0, 1, 3),
                    new CrackerBoardMove(0, 2, 5),
                    new CrackerBoardMove(1, 3, 6),
                    new CrackerBoardMove(1, 4, 8),
                    new CrackerBoardMove(2, 4, 7),
                    new CrackerBoardMove(2, 5, 9),
                    new CrackerBoardMove(3, 6, 10),
                    new CrackerBoardMove(3, 7, 12),
                    new CrackerBoardMove(4, 7, 11),
                    new CrackerBoardMove(4, 8, 13),
                    new CrackerBoardMove(5, 8, 12),
                    new CrackerBoardMove(5, 9, 14),
                    new CrackerBoardMove(3, 4, 5),
                    new CrackerBoardMove(6, 7, 8),
                    new CrackerBoardMove(7, 8, 9),
                    new CrackerBoardMove(10, 11, 12),
                    new CrackerBoardMove(11, 12, 13),
                    new CrackerBoardMove(12, 13, 14)
            };

    @Override
    public StepIterator iterator()
    { return new StepIterator(CRACKER_BOARD_MOVES); }
}

public class Cracker
{
    static StepList steps()
    { return new StepList(); }

    static LinkedList<CrackerBoardMove> firstSolution(CrackerBoard crackerBoard)
    {
        ArrayList<LinkedList<CrackerBoardMove>> out = new ArrayList<>();
        solve(crackerBoard, out, 1);

        if (out.size() == 0)
            return null;

        return out.get(0);
    }

    static void go()
    {
        int index = 0;
        while(index < 5){
            System.out.println("=== " + index + " ===");
            CrackerBoard b = new CrackerBoard(index);
            replayBoard(firstSolution(b), b);
            System.out.println();
            index++;
        }

    }

    static void solve(CrackerBoard b, ArrayList<LinkedList<CrackerBoardMove>> solutionsList, int count)
    {
        int j = 1;
        if (b.pegCount == j)
        {
            solutionsList.add(new LinkedList<>());
            return;
        }

        for (CrackerBoardMove m : steps())
        {
            CrackerBoard crackerBoardAfter = b.move(m);
            if (crackerBoardAfter == null) continue;

            ArrayList<LinkedList<CrackerBoardMove>> tailSolutions = new ArrayList<>();
            solve(crackerBoardAfter, tailSolutions, count);

            for (LinkedList<CrackerBoardMove> solution : tailSolutions)
            {
                solution.add(0, m);
                solutionsList.add(solution);

                if (solutionsList.size() == count)
                    return;
            }
        }
    }

    static void printCrackerBoard(CrackerBoard b)
    {
        System.out.print("(" + b.pegCount + ", [");
        for (int i = 0; i < b.cells.length; i++) {
            if(i < b.cells.length - 1){
                System.out.print( b.cells[i] + ", ");
            }else{
                System.out.print(b.cells[i] + "])");
            }
           
        }
        System.out.println();
    }

    static void showBoard(CrackerBoard cb)
    {
        int[][] crackerBoardlines = { {4,0,0}, {3,1,2}, {2,3,5}, {1,6,9}, {0,10,14} };
        for (int[] crackerBoardline : crackerBoardlines)
        {
            int crackerBoardspaces = crackerBoardline[0];
            int crakerBoardbegin  = crackerBoardline[1];
            int crackerBoardend    = crackerBoardline[2];

            String crackerBoardspace = new String();
            int index1 = 0;
            while (index1 < crackerBoardspaces){
                crackerBoardspace += " ";
                index1++;
            }



            System.out.print(crackerBoardspace);

            for (int i = crakerBoardbegin; i <= crackerBoardend; i++){
                if(cb.cells[i] == 0 ){
                    System.out.print(". ");
                }else{
                    System.out.print( "x ");

                }
            }


            System.out.println();
        }

        System.out.println();
    }

    static void replayBoard(List<CrackerBoardMove> crackerBoardMoves, CrackerBoard b)
    {
        showBoard(b);
        for (CrackerBoardMove m : crackerBoardMoves)
        {
            b = b.move(m);
            showBoard(b);
        }

    }

    static void terse() {
        for (int i = 0; i < 15; i++) {
            CrackerBoard b = new CrackerBoard(i);
            System.out.println("************");
            printCrackerBoard(b);
            List<CrackerBoardMove> moves = firstSolution(b);
            for (CrackerBoardMove m : moves) {
                System.out.println(m);
                b = b.move(m);
            }
            printCrackerBoard(b);
        }
    }

    public static void main(String[] args) {
        go();

    }
}

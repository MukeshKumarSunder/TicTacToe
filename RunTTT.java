
import java.util.Scanner;



class RunTTT 
{
    static TTT        m_ttt;
    static State m_state;
    static TTT.Value m_turn;
    static TTT.Value m_winner = TTT.Value.EMPTY;
    static int        m_nrMoves;
    static int        m_lastMove;
    static int        m_depth;

    RunTTT()
    {
	m_state  = null;
	m_winner = TTT.Value.EMPTY;
	m_nrMoves= 0;
	m_lastMove = -1;
	
    }
    
    
    static void MakeMove(int r,int c)
    {
	m_lastMove = m_ttt.m_nrCols * r + c;
	m_ttt.UpdateState(m_state.state, r, c, m_turn);
	++m_nrMoves;
	if(m_ttt.CheckWin(m_state.state, r, c)){
	    m_winner = m_turn;
        }
	else if(m_turn == TTT.Value.FIRST)
	    m_turn = TTT.Value.SECOND;
	else
	    m_turn = TTT.Value.FIRST;
	
//	if(m_winner != TTT.Value.EMPTY)
//	           System.out.println("player "+m_winner+" has won\n");
    }
    
    

public static void main(String args[])
{
    m_ttt=new TTT();
    m_ttt.m_nrRows  = (args.length > 1 ?Integer.parseInt(args[0]) : 3);
    m_ttt.m_nrCols  = (args.length > 2 ?Integer.parseInt(args[1]) : 3);
    m_ttt.m_nrToWin = (args.length > 3 ?Integer.parseInt(args[2]) : 3);
    m_depth = (args.length > 4 ?Integer.parseInt(args[3]) : 3);
    m_state = new State(m_ttt.NewState());
    m_ttt.ClearState(m_state.state);
    int b,r,c;
    System.out.println("running with\n");
    System.out.println(" nrRows   = "+m_ttt.m_nrRows+"\n");
    System.out.println(" nrCols   = "+m_ttt.m_nrCols+"\n");
    System.out.println(" nrToWin  = "+m_ttt.m_nrToWin+"\n");
    System.out.println(" depth    = "+m_depth+"\n");
    
    showBoard(m_state.state,m_ttt.m_nrRows,m_ttt.m_nrCols);
    Scanner sc = new Scanner(System.in);
    System.out.print("  Do you want to go first (y/n)? ");
    char ans = sc.next().toLowerCase().charAt(0);
    boolean turn;
    if (ans == 'y') {
      turn = false;
      m_turn=TTT.Value.FIRST;
    }
    else {
      turn = true;
      m_turn=TTT.Value.SECOND;
    }
    int    remMoves = m_ttt.m_nrRows * m_ttt.m_nrCols - m_nrMoves;
    while (remMoves > 0&& m_winner==TTT.Value.EMPTY) {
      // If there is a winner at this time, set the winner and the done flag to true.
      //done = isGameWon(board, turn, userSymbol, compSymbol); // Did the turn won?
      if(!turn){
          System.out.println("Enter your move:[row col] ");
          r=sc.nextInt(); c=sc.nextInt();
          if(c >= 0 && c < m_ttt.m_nrCols && 
	   r >= 0 && r < m_ttt.m_nrRows &&
	   m_ttt.GetValueState(m_state.state, r, c) == TTT.Value.EMPTY)
            {
	           System.out.println("setting position "+r+" "+c+" to value "+m_turn.getValue());
	    MakeMove(r, c);
            }
          turn=true;
          remMoves--;
      }
      else{
	    int    bestMove  = -1;
	    
	    
	    b=m_ttt.BestMove(m_state, m_depth, remMoves, m_lastMove, m_turn,bestMove,remMoves);
	         System.out.println(" bestMove = "+(b / m_ttt.m_nrCols)+" "+ (b % m_ttt.m_nrCols)+
                         " nrMoves = "+(m_nrMoves+1)+"\n");
                 System.out.println("EvalScore = "+m_ttt.evalState(m_state));
                 ++m_nrMoves;
                 m_ttt.UpdateState(m_state.state, (b / m_ttt.m_nrCols), (b % m_ttt.m_nrCols), m_turn);
                 if(m_ttt.CheckWin(m_state.state, (b / m_ttt.m_nrCols), (b % m_ttt.m_nrCols))){
	    m_winner = m_turn;
                     
                 }
                 else if(m_turn == TTT.Value.FIRST)
	    m_turn = TTT.Value.SECOND;
	else
	    m_turn = TTT.Value.FIRST;
	
//	if(m_winner != TTT.Value.EMPTY)
//	           System.out.println("player "+m_winner+" has won\n");
            turn=false;
            remMoves--;
      }     
      showBoard(m_state.state,m_ttt.m_nrRows,m_ttt.m_nrCols);
    }
    if(m_winner != TTT.Value.EMPTY)
	           System.out.println("player "+m_winner.getValue()+" has won\n");
    else
        System.out.println("Its a DRAW!");
    //gManager.MainLoop("TTT", 600, 600);
    
}

public static void showBoard(char[] brd,int rows,int cols)
  {
    int numRow = rows;
    int numCol = cols;

    System.out.println();

    // First write the column header
    System.out.print("    ");
    for (int i = 0; i < numCol; i++)
      System.out.print(i + "   ");
    System.out.print('\n');

    System.out.println(); // blank line after the header

    // The write the table
    for (int i = 0; i < numRow; i++) {
      System.out.print(i + "  ");
      for (int j = 0; j < numCol; j++) {
        if (j != 0)
          System.out.print("|");
        System.out.print(" " + brd[((i*numCol)+j)] + " ");
      }

      System.out.println();

      if (i != (numRow - 1)) {
        // separator line
        System.out.print("   ");
        for (int j = 0; j < numCol; j++) {
          if (j != 0)
            System.out.print("+");
          System.out.print("---");
        }
        System.out.println();
      }
    }
    System.out.println();
  }

}
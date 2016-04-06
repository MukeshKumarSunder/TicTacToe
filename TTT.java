
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Ninad
 */
public class TTT {
    int m_nrRows;
    int m_nrCols;
    int m_nrToWin;
    List<State> nextMoveOptions = new ArrayList<>();
    
    enum Value
	{
            EMPTY(' '),
	    FIRST('o'),
            SECOND('x');

            private final char value;
             Value(char value) {
                this.value = value;
            }
            public char getValue() {
                return value;
            }
	};
    double evalState(State s){
        if(GetValueState(s.state,s.id/m_nrCols,s.id%m_nrCols)==Value.SECOND && CheckWin(s.state,s.id/m_nrCols,s.id%m_nrCols)==true) return 1;
        if(GetValueState(s.state,s.id/m_nrCols,s.id%m_nrCols)==Value.FIRST && CheckWin(s.state,s.id/m_nrCols,s.id%m_nrCols)==true) return 1;		
        return 0;
    }
    int BestMove(State s,int depth,int remMoves,int lastMove,Value player,int bestMove, int remMove) 
    {   
        double alpha = Integer.MIN_VALUE;
        double beta = Integer.MAX_VALUE;
        int color = player == Value.FIRST? 1:-1;
        negaMax(s,depth,alpha,beta,color,lastMove,remMove);
        nextMoveOptions = order(nextMoveOptions);
        int move = nextMoveOptions.get(nextMoveOptions.size()-1).id;
        return move;
    }
    double negaMax(State s,int depth,double alpha,double beta,int color,int lastMove,int remMove){
        double bestVal;
        double v;
        if(depth==0||isTerminalNode(s.state))
            return color*evalState(s);
        List<State> childNodes = getAllSuccessors(s.state,color);
        childNodes = order(childNodes);
        bestVal = Integer.MIN_VALUE;
        Iterator<State> it = childNodes.iterator();
        State child = null;
        while(it.hasNext()){
            child = new State(it.next());
            v = -negaMax(child,(depth-1),-beta,-alpha,-color,lastMove,remMove-1);
            bestVal = Math.max(bestVal, v);
            alpha = Math.max(alpha, v);
            if(alpha>=beta)
                break;
        }
        nextMoveOptions.add(child);
        return bestVal;
    }
    boolean CheckWin(char[] s,int r,int c)
    {
    Value val  = GetValueState(s, r, c);
    int cons = 0, end;
    
    //check row
    cons = 0; 
    end = Math.min(m_nrCols, c + m_nrToWin);
    for(int i = Math.max(0, c - m_nrToWin); i < end; ++i)
	if(GetValueState(s, r, i) != val)
	    cons = 0;
	else if((++cons) >= m_nrToWin)
	    return true;
    
    //check column
    cons = 0; 
    end = Math.min(m_nrRows, r + m_nrToWin);
    for(int i = Math.max(0, r - m_nrToWin); i < end; ++i)
	if(GetValueState(s, i, c) != val)
	    cons = 0;
	else if((++cons) >= m_nrToWin)
	    return true;
    
    //check diagonal
    cons = 0; 
    end = 1 + Math.min(m_nrToWin - 1, Math.min(m_nrRows - r - 1, m_nrCols - c - 1));
    for(int i = Math.max(-m_nrToWin + 1, Math.max(-r, -c)); i < end; ++i)
	if(GetValueState(s, r + i, c + i) != val)
	    cons = 0;
	else if((++cons) >= m_nrToWin)
	    return true;
    
    //check anti-diagonal
    cons = 0; 
    end = 1 + Math.min(m_nrToWin - 1, Math.min(r, m_nrCols - c - 1));
    for(int i = Math.max(-m_nrToWin + 1, Math.max(r - m_nrRows + 1, -c)); i < end; ++i)
    {
	if(GetValueState(s, r - i, c + i) != val)
	    cons = 0;
	else if((++cons) >= m_nrToWin)
	    return true;
    }
    
    
    return false;
    
    
}
    Value GetValueState(char[] s,int row,int col)
    {
	for(Value v : Value.values()){
            if (v.getValue() == s[row * m_nrCols + col]){
                return v;
            }
        }   
        return null;
    }
    
    //This is a utility function which sets the square in position (row, col) to val
//For example, SetValue(s, 1, 2, FIRST) sets the square in position (1, 2) to the value FIRST 
// (marks the move by the first player)
    char[] SetValueState(char[] s,int row,int col, Value val)
    {
        char[] state = Arrays.copyOf(s, s.length);
	state[row * m_nrCols + col] = val.getValue();
        return state;
    }
    
    char[] UpdateState(char[] s,int row,int col, Value val)
    {
	s[row * m_nrCols + col] = val.getValue();
        return s;
    }
    
    //This is a utility function, which allocates memory for a new state
    char[] NewState()
    {
	return new char[m_nrRows * m_nrCols];
    }
    
    void ClearState(char[] s)
    {
	for(int i = 0;i<s.length;i++)
            s[i] = ' ';
    }
    
    List<State> getAllSuccessors(char[] s,int color){
        int r,c;
        Value v = (color == 1)? Value.FIRST:Value.SECOND;
        List<State> children = new ArrayList<>();
        for(int i=0;i<s.length;i++){
            r = i/m_nrCols;
            c = i%m_nrCols;
            if(GetValueState(s,r,c)==Value.EMPTY)              
                children.add(new State(SetValueState(s,r,c,v),i));
        }
        return children;
    }
    
    boolean isTerminalNode(char[] s){
        for(int i=0;i<s.length;i++){
            if(s[i]==' ')
                return false;
        }
        return true;
    }
    
    List order(List<State> l){
        Collections.sort(l, new Comparator<State>(){
            @Override
            public int compare(State state1, State state2) {
                return (int)evalState(state1)- (int)evalState(state2);}
        });
        return l;
    }
    
}

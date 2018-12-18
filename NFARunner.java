import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


public class NFARunner {
	/**
	 * Reads regular expressions from a file
	 * and converts them into NFAs
	 * 
	 * @param needs a file named test.txt in the current directory
	 */
	
	public static void main( String[] args ){
		//read the input file from the current directory
		Scanner fscan;
		try{
			fscan = new Scanner( new File("test.txt") );
		}catch( FileNotFoundException ex ){
			System.out.println("File not found\nThis program expects the input file to be named \"test.txt\"");
			return;
		}
		
		//create an NFA for each expression in the file
		while( fscan.hasNext() ){
			ArrayList<NFA> NFAList = new ArrayList<NFA>();
			
			String ex = fscan.next();
			
			//one new NFA and two from the list
			NFA n = null;
			NFA a; 
			NFA b;
			
			//read each character in the expression
			for( int i = 0; i < ex.length(); i++ ){
				char ch = ex.charAt(i);
				
				//figure out what the symbol means
				switch( ch ){
				case '&':
					/*
					 * Concatenate two NFAs by creating an epsilon transition from 
					 * the final state of the first NFA to the start state of the
					 * second NFA.
					 */
					
					//make sure there are 2 NFAs to concatenate
					if( NFAList.size() < 2 ){ return; }
					
					a = NFAList.get( NFAList.size() - 2 );
					b = NFAList.get( NFAList.size() - 1 );
					
					//add the epsilon transition
					for( NFA.State st : a.finalStateList ){
						st.addTransition('E', b.startState);
					}
					//change the isStart and isFinal values
					b.startState.isStart = false;
					for( NFA.State st : a.finalStateList ){	
						st.isFinal = false;
					}
					//move the final state of a to the final state of b
					a.finalStateList = b.finalStateList;
					
					for( NFA.Transition tr : b.transitionList ){
						a.transitionList.add( tr );
					}
					
					//remove the old NFAs from the list
					NFAList.remove(NFAList.size() - 1);
					NFAList.remove(NFAList.size() - 1);
					
					//add the new NFA to the list
					NFAList.add( a );
					break;
				case '|':
					/*
					 * Build the union of two NFAs by creating a new
					 * start state with epsilon transitions to the start
					 * states of the old NFAs
					 */
					if( NFAList.size() < 2 ){ return; }
					
					//create a new start state
					n = new NFA();
					n.startState = n.addState( false, true);
					
					//get the last two NFAs from the lsit
					a = NFAList.get( NFAList.size() - 2);
					b = NFAList.get( NFAList.size() - 1);
					
					//add epsilon transitions to the start states
					n.startState.addTransition('E', a.startState);
					n.startState.addTransition('E', b.startState);
					
					a.startState.isStart = false;
					b.startState.isStart = false;
					
					//copy the final state lists
					for( NFA.State st : a.finalStateList ){
						n.finalStateList.add( st );
					}
					for( NFA.State st : b.finalStateList ){
						n.finalStateList.add( st );
					}
					
					//copy the transition lists
					for( NFA.Transition tr : a.transitionList ){
						n.transitionList.add( tr );
					}
					for( NFA.Transition tr : b.transitionList ){
						n.transitionList.add( tr );
					}
					
					//remove the old NFAs
					NFAList.remove( NFAList.size() - 1 );
					NFAList.remove( NFAList.size() - 1 );
					
					//put the new NFA into the list
					NFAList.add( n );
					break;
				case '*':
					/*
					 * Build the Kleene star of an NFA by
					 * 1. Making a new, accepting start state with an epsilon transition to
					 *    the old start state
					 * 2. Create epsilon transitions from each of the final states
					 *    to the new start state
					 */
					
					//create a new start state that is also a final state
					n = new NFA();
					n.startState = n.addState(true, true);
					
					//get the NFA from the list
					a = NFAList.get(NFAList.size() - 1);
					
					//add an E-transition to the start state of the NFA a
					n.startState.addTransition('E', a.startState);
					a.startState.isStart = false;
					
					//add E-transitions from each of the NFA a's final states
					for( NFA.State st : a.finalStateList ){
						a.transitionList.add( a.new Transition(st, 'E', n.startState) );
						st.isFinal = false;
						
					}
					
					//add all the transitions to the new transition list
					for( NFA.Transition tr : a.transitionList ){
						n.transitionList.add( tr );
					}
					
					//remove the old NFA from the list
					NFAList.remove(a);
					
					//add the new NFA to the list
					NFAList.add( n );
					break;
				default:
					/*
					 * Base case: form a new NFA that accepts the symbol
					 */
					
					//create a start state
					n = new NFA();
					n.startState = n.addState(  false, true );
					
					//add a transition to the final state with the input symbol
					n.startState.addTransition(ch, n.addState( true, false));
					
					//add the NFA to the list
					NFAList.add( n );
					break;
				}
			}
			
			//print the resulting NFA
			System.out.println(ex);
			NFAList.get(0).printNFA();
			System.out.println();
		}
		fscan.close();
	}
}

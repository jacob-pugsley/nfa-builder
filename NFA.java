import java.util.ArrayList;

/* Jacob Pugsley
 * CS 317
 * October 1, 2018
 */
public class NFA {
	/**
	 * Represents an NFA
	 * Includes the start state, list of transitions, and a list of final states
	 * The transition list represents a list of states and transitions
	 */
	int numberOfStates = 1;
	State startState;
	ArrayList<State> finalStateList = new ArrayList<State>();
	ArrayList<Transition> transitionList = new ArrayList<Transition>();
	
	/**
	 * Add a state to the NFA
	 * @param isFinal: is this a final state?
	 * @param isStart: is this the start state?
	 * @return the State object which was created
	 */
	public State addState( boolean isFinal, boolean isStart ){
		State s = new State();
		if( isFinal ){
			this.finalStateList.add(s);
		}
		s.isFinal = isFinal;
		s.isStart = isStart;
		return s;
	}
	
	/**
	 * Represents a state in the NFA
	 * 
	 */
	class State{
		//the name of each state is initially 0; they are given names
		//  when the NFA is printed
		int name = 0;
		
		boolean isStart;
		boolean isFinal;

		public void addTransition( char input, State nextState ){
			transitionList.add( new Transition( this, input, nextState ) );
		}
		
		
	}
	
	/**
	 * Represents a transition between states in the NFA
	 * The list of these objects will be printed in the end
	 */
	class Transition{
		char input;
		State prevState;
		State nextState;
		
		public Transition( State prevState, char input, State nextState ){
			this.input = input;
			this.nextState = nextState;
			this.prevState = prevState;
		}
	}
	
	/**
	 * Print all of the transitions in this NFA's transition list
	 * Also assigns names to the states based on the total
	 *   number of states.
	 */
	public void printNFA(){
		//print all the transitions in the transition list
		for( Transition tr : transitionList ){
			
			if( tr.nextState.name == 0 && !tr.nextState.isStart ){
				//we need the additional check because the first state
				//  will be q0
				tr.nextState.name = numberOfStates;
				numberOfStates++;
			}
			
			System.out.print( "(q" + tr.prevState.name + ", " + tr.input + ") -> q" + tr.nextState.name );
			if( tr.nextState.isFinal ){
				System.out.print("F");
			}
			System.out.println();
		}
	}
}

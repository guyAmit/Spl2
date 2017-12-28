package bgu.spl.a2.sim.Actions.subActions;

import java.util.HashMap;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

public class fetchGrades extends Action<HashMap<String, Integer>> {

	/**
	 * <h1>fetchGrades</h1>
	 * <h2>general notes:</h2>>
	 * 	this.actorState is the private state of the student
	 *	this.actorId is the Id of the stident
	*/
	
	public fetchGrades() {
		this.actionName="fetch Grades";
	}
	
	@Override
	protected void start() {
		HashMap<String, Integer> grades =new HashMap<>();
		grades.putAll(((StudentPrivateState)this.actorState).getGrades());
		this.complete(grades);
	}

}

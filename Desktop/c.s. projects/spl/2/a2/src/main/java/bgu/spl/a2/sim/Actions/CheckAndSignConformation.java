/**
 * 
 */
package bgu.spl.a2.sim.Actions;

import java.util.ArrayList;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.Computer;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

/**
 * @author Guy-Amit
 *
 */
public class CheckAndSignConformation extends Action<Boolean> {
	
	private StudentPrivateState studentPrivateState;
	private Computer computer;
	private ArrayList<String> coursesIds;
	
	public CheckAndSignConformation(Computer computer,ArrayList<String> coursesIds) {
		this.actionName="CheckAndSignConformation";
		this.computer=computer;
		this.coursesIds = coursesIds;
	}
	@Override
	protected void start() {
		this.studentPrivateState=(StudentPrivateState)this.actorState;
		this.studentPrivateState.setSignature(this.computer.checkAndSign(this.coursesIds, this.studentPrivateState.getGrades()));
		this.complete(true);
	}

}

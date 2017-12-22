package bgu.spl.a2.sim.Actions;

import java.util.ArrayList;
import bgu.spl.a2.*;
import bgu.spl.a2.Action;
import bgu.spl.a2.sim.Computer;
import bgu.spl.a2.sim.Simulator;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

public class Check_Administrative_Obligations extends Action<Boolean> {

	

	/**
	 * <h1>Check_Administrative_Obligations action</h1>
	 * <h2>general notes:</h2>>
	 * 	this.actorState is the private state of the department
	 *	this.actorId is the Id of the department
	*/
	
	private ArrayList<StudentPrivateState> studentsPrivateStates;
	private String computerId;
	private ArrayList<String> coursesIds;
	private ArrayList<String> studentsIds;
	
	public Check_Administrative_Obligations(ArrayList<String> coursesToCheck,ArrayList<String> studentsIds,String computerId) {
		this.actionName="Check administrative obligations";
		this.studentsPrivateStates = new ArrayList<>();
		this.computerId=computerId;
		this.studentsIds=studentsIds;
		this.coursesIds=coursesToCheck;
		studentsIds.forEach(student -> this.studentsPrivateStates.add((StudentPrivateState)this.pool.getPrivaetState(student)));
	}
	
	
	/**
	 * <h1>start-Check_Administrative_Obligations</h1>
	 * <p> this is not a classic action. this action work with the simulator,</br>
	 * and does not use any sub actions. the action will "wait" for his turn </br>
	 * to use the computer, and then use it.</p>
	 */
	@Override
	protected void start() {
		Promise<Computer> computerPromise = Simulator.wareHouse.acquireComputer(computerId);
		computerPromise.subscribe(()->{
			for (StudentPrivateState student : this.studentsPrivateStates) {
				student.setSignature(computerPromise.get().checkAndSign(coursesIds, student.getGrades()));
			}
			Simulator.wareHouse.freeComputer(this.computerId);
			this.complete(true);
		});
	}
	
	
	@Override
	public String toString() {
		return "Check_Administrative_Obligations: <"+this.studentsIds+", "+this.coursesIds+">";
	}

}

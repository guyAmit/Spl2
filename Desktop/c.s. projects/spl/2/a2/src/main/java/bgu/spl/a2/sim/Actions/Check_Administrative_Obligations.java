package bgu.spl.a2.sim.Actions;

import java.util.ArrayList;
import bgu.spl.a2.*;
import bgu.spl.a2.Action;
import bgu.spl.a2.sim.Computer;
import bgu.spl.a2.sim.Simulator;
import bgu.spl.a2.sim.Actions.subActions.CheckAndSignConformation;
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
		this.actionName="Administrative Check";
		this.studentsPrivateStates = new ArrayList<>();
		this.computerId=computerId;
		this.studentsIds=studentsIds;
		this.coursesIds=coursesToCheck;
	}
	
	
	/**
	 * <h1>start-Check_Administrative_Obligations</h1>
	 * <p> this is not a classic action. this action work with the simulator,</br>
	 * and does not use any sub actions. the action will "wait" for his turn </br>
	 * to use the computer, and then use it.</p>
	 */
	@Override
	protected void start() {
		this.studentsIds.forEach(student -> this.studentsPrivateStates.add((StudentPrivateState)this.pool.getPrivaetState(student)));
		Promise<Computer> computerPromise = Simulator.wareHouse.acquireComputer(computerId);
		ArrayList<Action<Boolean>> subActions = new ArrayList<>();
		//waiting to the action turn for the computer, and then doing the check and signs
		computerPromise.subscribe(()->{
			int i=0;
			for (StudentPrivateState student : this.studentsPrivateStates) {
				CheckAndSignConformation conf = new CheckAndSignConformation(computerPromise.get().checkAndSign(coursesIds, student.getGrades()));
				subActions.add(conf);
				this.pool.submit(conf, studentsIds.get(i), student);
				i++;
			}
			//freeing the computer.
			this.then(subActions, ()->{
				Simulator.wareHouse.freeComputer(this.computerId);
				if(this.promise.isResolved())
					this.promise.get();
				else this.complete(true);
			});
		});
		this.actorState.addRecord(actionName);
	}
	
	
	@Override
	public String toString() {
		return "Check_Administrative_Obligations: <"+this.studentsIds+", "+this.coursesIds+">";
	}

}

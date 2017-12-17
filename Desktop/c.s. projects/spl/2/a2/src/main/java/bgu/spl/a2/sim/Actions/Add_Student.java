package bgu.spl.a2.sim.Actions;

import java.util.ArrayList;
import java.util.List;

import bgu.spl.a2.Action;
import bgu.spl.a2.PrivateState;
import bgu.spl.a2.sim.Actions.subActions.ConformationAction;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;
/**
 * @author Guy-Amit
 */
public class Add_Student extends Action<Boolean> {
	
	
	
	/**
	 * <h1>add student action</h1>
	 * <h2>general notes:</h2>>
	 * 	this.actorState is the private state of the department
	 *	this.actorId is the Id of the department
	*/
	
	private String studentId;
	private StudentPrivateState studentPrivateState;

	public  Add_Student(String studentId) {
		this.studentId=studentId;
		this.studentPrivateState = new StudentPrivateState();
	}
	
	/**
	 * <h1>start-addStudebt</h1>
	 * <p>this method should create a new student in the department.<br>
	 * 	we will do it by adding the new student to the department student list first<br>
	 * and then we will send a {@link #ConformationAction} action to the newly created student actor to make<br>
	 * sure it is really created </p>
	 */
	@Override
	protected void start() {
		ArrayList<Action<Boolean>> subActions = new ArrayList<>();
		//sending a sub action to the student actor to check that he was indeed created
		ConformationAction conf = new ConformationAction();
		this.pool.submit(conf, studentId, studentPrivateState);
		subActions.add((Action<Boolean>) conf);
		this.then(subActions, ()->{
			//will be executed when all the SubActions will finish
			//and also after the action will get back into his original
			//queue
			Boolean resualt = subActions.get(0).getResult().get();
			if(resualt) {
				((DepartmentPrivateState)this.actorState).getStudentList().add(this.studentId);
			}else {System.out.println("student was not created");}
			this.complete(resualt);
		});
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

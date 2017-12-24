package bgu.spl.a2.sim.Actions.subActions;

import bgu.spl.a2.Action;
import bgu.spl.a2.PrivateState;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

/**
 * 
 * @author Guy-Amit
 *
 */

public class RegistrationConformation extends Action<Boolean> {

	/**
	 * <h1>RegistrationConformation action</h1>
	 * <h2>general notes:</h2>>
	 * 	this.actorState is the private state of the student
	 *	this.actorId is the Id of the student
	*/
	
	private String courseId;
	private Integer grade;//optional
	private CoursePrivateState coursePrivateState;
	
	public RegistrationConformation(String courseId, Integer grade) {
		this.courseId=courseId;
		if(grade!=null) this.grade = grade;
		else grade = new Integer(0);
		this.actionName="Registration conformation";
	}
	
	public RegistrationConformation(String courseId) {
		this.courseId=courseId;
		this.grade=new Integer(0);
	}
	
	/**
	 * <h1>start-RegistrationConformation</h1>
	 * --for now I am ignoring preference list and just adding the course--
	 * so I am just checking if the student meets the requirements for the course,
	 * if he does I register the student.
	 */
	@Override
	protected void start() {
		this.coursePrivateState = (CoursePrivateState)this.pool.getPrivaetState(courseId);
		if(((StudentPrivateState)this.actorState).meetRequirements(coursePrivateState.getPrequisites())) {
			((StudentPrivateState)this.actorState).getGrades().put(courseId, grade);
			this.complete(true);
		}
		else
			this.complete(false);
	}
	
	@Override
	public String toString() {
		return "RegistrationConformation: "+this.actorId;
	}

}




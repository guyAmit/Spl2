/**
 * 
 */
package bgu.spl.a2.sim.Actions.subActions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

/**
 * @author Guy-Amit
 *
 */
public class UnRegistrationConformation extends Action<Boolean> {

	/**
	 * <h1>RegistrationConformation action</h1>
	 * <h2>general notes:</h2>>
	 * 	this.actorState is the private state of the student
	 *	this.actorId is the Id of the student
	*/
	
	private String courseId;
	private Integer grade;//optional
	private CoursePrivateState coursePrivateState;

	
	public UnRegistrationConformation(String courseId) {
		this.courseId=courseId;
		this.grade=new Integer(0);
		this.coursePrivateState = (CoursePrivateState)this.pool.getPrivaetState(courseId);

	}
	
	/**
	 * <h1>start-unRegistrationConformation</h1>
	 * simple confirmation action for the unRegister action,checks if <br>
	 * the student is indeed registered, if he is, this method will unregister him<br>
	 * and send the result back to the original action
	 */
	@Override
	protected void start() {
		if(((StudentPrivateState)this.actorState).getGrades().containsKey(courseId)){
			((StudentPrivateState)this.actorState).getGrades().remove(this.courseId);
			this.complete(true);
		}
		else
			this.complete(false);
	}

}

package bgu.spl.a2.sim.Actions;

import java.util.ArrayList;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.Simulator;
import bgu.spl.a2.sim.Actions.subActions.CloseCourseConformation;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;
/**
 * 
 * @author Guy-Amit
 */

public class Close__Course extends Action<Boolean> {

	/**
	 * <h1>add student action</h1>
	 * <h2>general notes:</h2>>
	 * 	this.actorState is the private state of the department
	 *	this.actorId is the Id of the department
	*/
	private String courseId;
	private CoursePrivateState coursePrivateState;
	
	public Close__Course(String courseId) {
		this.courseId=courseId;
		this.actionName="Close Course";
	}
	
	/**
	 * <h1>start-close_course</h1>
	 * this method sends a {@link #CloseCourseConformation} action to the course that<br>
	 * Should be closed. the {@link #CloseCourseConformation} send a sub action of type {@link #UnRegistrationConformation}
	 * to all the students of the course to remove them self from the course, and report back to the<br>
	 * action once they are done. once all the students remove them self, the course will change his<br>
	 * private state and report back to this action, that the process succeeded
	 */
	@Override
	protected void start() {
		this.coursePrivateState = (CoursePrivateState)this.pool.getActors().get(this.courseId);
		if(((DepartmentPrivateState)this.actorState).getCourseList().contains(this.courseId)) {
			ArrayList<Action<Boolean>> subActions = new ArrayList<>();
			//sending a sub action to the course actor- telling him to remove all student from the course
			CloseCourseConformation conf = new CloseCourseConformation();
			this.pool.submit(conf,this.courseId, this.coursePrivateState);
			subActions.add(conf);
			this.then(subActions,()->{
				//will be executed when all the SubActions will finish
				//and also after the action will get back into his original
				//queue
				Boolean resualt = subActions.get(0).getResult().get();
				if(resualt) {
					((DepartmentPrivateState)this.actorState).getCourseList().remove(this.courseId);
				}else {
					System.out.println("course was not closed");
					}
				this.complete(resualt);
				Simulator.phaseActions.countDown();
			});
		}
		else {
			this.complete(false);
			System.out.println("course is not in the department");
		}
		
	}
	
	@Override
	public String toString() {
		return "Close Course: "+this.courseId;
	}
	

}

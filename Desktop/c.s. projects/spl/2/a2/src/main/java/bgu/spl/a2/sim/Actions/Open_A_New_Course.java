/**
 * 
 */
package bgu.spl.a2.sim.Actions;

import java.util.ArrayList;
import java.util.List;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.Simulator;
import bgu.spl.a2.sim.Actions.subActions.ConformationAction;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

/**
 * @author Guy-Amit
 */
public class Open_A_New_Course extends Action<Boolean> {

	/**
	 * <h1>Open_A_New_Course action</h1>
	 * <h2>general notes:</h2>>
	 * 	this.actorState is the private state of the department
	 *	this.actorId is the Id of the department
	*/
	
	private String courseId;
	private CoursePrivateState coursePrivateState;
	private int spaces;
	
	public Open_A_New_Course(String courseId,int spaces,List<String> prequisites ) {
		this.courseId=courseId;
		this.spaces=spaces;
		this.actionName="Open Course";
		this.coursePrivateState = new CoursePrivateState();
		this.coursePrivateState.SetPrivateState(spaces,0,new ArrayList<String>(),prequisites);
	}
	
	
	/**
	 * <h1>start-Open_A_New_Course</h1>
	 * <p>this method should create a new course in the department.<br>
	 * 	we will do it by adding the new course to the department course list first<br>
	 * and then we will send a {@link #ConformationAction} action to the newly created course actor to make<br>
	 * sure it is really created </p>
	 * @sync explanation : the courses list is not a concurrent data structure i.e. it does not support</br>
	 * the addition of two items at the same time 
	 */
	@Override
	protected void start() {
		ArrayList<Action<Boolean>> subActions = new ArrayList<>();
		//sending a sub action to the course actor to make sure that he was indeed created
		this.pool.getActors().put(this.courseId, this.coursePrivateState);
		ConformationAction conf = new ConformationAction();
		this.pool.submit(conf, this.courseId, this.coursePrivateState );
		subActions.add((Action<Boolean>) conf);
		this.then(subActions, ()->{
			//will be executed when all the SubActions will finish
			//and also after the action will get back into his original
			//queue
			Boolean resualt = subActions.get(0).getResult().get();
			List<String> coursesIds = ((DepartmentPrivateState)this.actorState).getCourseList();
			if(resualt) {
				coursesIds.add(this.courseId);
				}
			else {System.out.println("student was not created");}
			
			this.complete(resualt);
		});
		this.actorState.addRecord(actionName);
	}
	
	@Override
	public String toString() {
		return "Open new course: "+this.courseId;
	}
}

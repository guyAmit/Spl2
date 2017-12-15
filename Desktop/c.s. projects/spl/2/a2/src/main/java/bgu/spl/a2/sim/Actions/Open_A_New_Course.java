/**
 * 
 */
package bgu.spl.a2.sim.Actions;

import java.util.ArrayList;
import java.util.List;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

/**
 * @author Guy-Amit
 */
public class Open_A_New_Course extends Action<Boolean> {

	/**
	 * <h1>add student action</h1>
	 * <h2>general notes:</h2>>
	 * 	this.actorState is the private state of the department
	 *	this.actorId is the Id of the department
	*/
	
	private String courseId;
	private CoursePrivateState coursePrivateState;
	private int spaces;
	
	public Open_A_New_Course(String CourseId,int spaces,List<String> prequisites ) {
		this.courseId=courseId;
		this.spaces=spaces;
		this.coursePrivateState = new CoursePrivateState(spaces,0,
				new ArrayList<String>(),prequisites);
	}
	
	
	@Override
	protected void start() {
		ArrayList<Action<Boolean>> subActions = new ArrayList<>();
		ConformationAction conf = new ConformationAction();
		this.sendMessage(conf, this.courseId, this.coursePrivateState );
		subActions.add((Action<Boolean>) conf);
		this.then(subActions, ()->{
			//will be executed when all the SubActions will finish
			//and also after the action will get back into his original
			//queue
			Boolean resualt = subActions.get(0).getResult().get();
			this.complete(resualt);
			if(resualt) {
				((DepartmentPrivateState)this.actorState).getCourseList().add(courseId);
			}else {System.out.println("student was not created");}
		});
	}
}

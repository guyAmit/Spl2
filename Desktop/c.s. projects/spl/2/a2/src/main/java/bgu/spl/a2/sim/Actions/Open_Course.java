/**
 * 
 */
package bgu.spl.a2.sim.Actions;

import java.util.ArrayList;
import java.util.List;
import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;

/**
 * @author AdamSh
 *@param <R>
 */
public class Open_Course<R> extends Action<R> {

	private DepartmentPrivateState departmentState = (DepartmentPrivateState)pool.getActors().get(actorId);
	private String courseName = "";
	private List <Action<?>> actions = new ArrayList<>();
	
	@SuppressWarnings("unchecked")
	@Override
	protected void start() {
		setActionName("Open A New Course");
		actions.add(new Action() {
			@Override
			protected void start() {
				// TODO Auto-generated method stub
				departmentState.getCourseList().add(courseName);
			}
		});
		then(actions,()->{
			//actorId -> department ,actorState -> course
			pool.submit(this, actorId, departmentState);
		});
		complete((R) new Object());
	}
	public void setNameToAddedCourse(String courseName) {
		this.courseName = courseName;
	}
}

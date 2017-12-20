package bgu.spl.a2.sim.Actions;

import java.util.ArrayList;
import java.util.List;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;
public class Close_A_Course<R> extends Action<R> {

	private DepartmentPrivateState departmentState = (DepartmentPrivateState)pool.getActors().get(actorId);
	private CoursePrivateState courseState = (CoursePrivateState)actorState;
	private List <Action<?>> actions = new ArrayList<>();
	private String courseName = "";
	
	@SuppressWarnings("unchecked")
	@Override
	protected void start() {
		setActionName("Close A Course");
		actions.add(new Action() {

			private List<Action<?>> actions = new ArrayList<>();
			
			@Override
			protected void start() {
				// TODO Auto-generated method stub
				courseState.setAvailableSpots(-1);
				departmentState.getCourseList().remove(courseName);
				for(String student : departmentState.getStudentList()) {
					Unregister<R> unregister = new Unregister<R>();
					unregister.NameToUnregister(student);
					then(actions,()->{
						//actorId -> course, actorState -> student
						pool.submit(unregister, student , pool.getActors().get(student));
					});
				}
			}
		});
		then(actions,()->{
			//actorId -> department ,actorState -> course
			pool.submit(this, actorId, departmentState);
		});
		complete((R) new Object());
	}
/*
 * SHOULD BE CALLED BEFORE START
 * 
 * @param courseName
 * sets the field courseName to a new String value
 */
	public void NameToDelete (String courseName){
		this.courseName = courseName;
	}
}

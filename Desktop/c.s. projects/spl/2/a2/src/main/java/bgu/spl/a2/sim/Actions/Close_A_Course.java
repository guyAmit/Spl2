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

			@Override
			protected void start() {
				// TODO Auto-generated method stub
				departmentState.getCourseList().remove(courseName);
				for(String student : departmentState.getStudentList()) {
					Unregister<R> unregister = new Unregister<R>();
					unregister.NameToUnregister(student);
					sendMessage(unregister,student,pool.getActors().get(student));
				}
				courseState.setAvailableSpots(-1);
			}
		});
		then(actions,()->{
			//actorId -> department ,actorState -> course
			pool.submit(this, actorId, departmentState);
		});
		complete((R) new Object());
	}

	public void NameToDelete (String courseName){
		this.courseName = courseName;
	}
}

package bgu.spl.a2.sim.Actions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

public class Unregister<R> extends Action<R> {
	private CoursePrivateState courseState = (CoursePrivateState)pool.getActors().get(actorId);
	private StudentPrivateState studentState = (StudentPrivateState) actorState;
	private String nameOfStudent = "";
	private List<Action<?>> actions = new ArrayList<Action<?>>();
	
	@SuppressWarnings("unchecked")
	@Override
	protected void start() {
		// TODO Auto-generated method stub
		setActionName("Unregister");
		
		actions.add(new Action() {
			@Override
			protected void start() {
				// TODO Auto-generated method stub
				//no need to check if grade at list 56

					if(courseState.getRegStudents().remove(nameOfStudent)) {
						studentState.getGrades().remove(actorId);
						courseState.setAvailableSpots(courseState.getAvailableSpots() + 1);
					}
			}
		});
		then(actions,()->{
			//actorId -> course, actorState -> student
			pool.submit(this, actorId, courseState);
		});
		complete((R) new Object());
	}
	/**
	 * SHOULD BE CALLED BEFORE START
	 * 
	 * 
	 * @param nameOfStudent
	 * sets the field nameOfStudent which is the name of the student which is going to 
	 * be unregistered of the course to a new String value
	 */
	public void NameToUnregister(String nameOfStudent) {
		this.nameOfStudent = nameOfStudent;
	}
	
}

package bgu.spl.a2.sim.Actions.subActions;

import java.util.ArrayList;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

public class CloseCourseConformation extends Action<Boolean> {


	/**
	 * <h1>Participate_In_Course action</h1>
	 * <h2>general notes:</h2>>
	 * 	this.actorState is the private state of the course
	 *	this.actorId is the Id of the course
	*/
	
	private ArrayList<String> studentsIds;
	private CoursePrivateState coursePrivateState;
	
	public CloseCourseConformation() {
		this.coursePrivateState = (CoursePrivateState)this.actorState;
		this.studentsIds = (ArrayList<String>) coursePrivateState.getRegStudents();
	}
	
	@Override
	protected void start() {
		// TODO Auto-generated method stub
		ArrayList<Action<Boolean>> subActions = new ArrayList<>();
		for (String studentId : studentsIds) {
			UnRegistrationConformation conf = new UnRegistrationConformation(this.actorId);
			StudentPrivateState studentPrivateState = (StudentPrivateState)this.pool.getActors().get(studentId);
			this.sendMessage(conf,studentId, studentPrivateState);
			subActions.add(conf);
		}
		this.then(subActions, ()->{
			for (Action<Boolean> action : subActions) {
				if(!action.getResult().get()) {
					System.out.println("some student was not removed from the course");
					this.complete(false);
					break;
				}
			}
			this.coursePrivateState.closeCourse();
			this.complete(true);
		});
		

	}

}

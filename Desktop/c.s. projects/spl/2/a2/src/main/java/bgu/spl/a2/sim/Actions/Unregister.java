package bgu.spl.a2.sim.Actions;

import java.util.ArrayList;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.Actions.subActions.UnRegistrationConformation;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

public class Unregister extends Action<Boolean> {


	/**
	 * <h1>Participating_In_Course action</h1>
	 * <h2>general notes:</h2>>
	 * 	this.actorState is the private state of the course
	 *	this.actorId is the Id of the course
	*/
	

	private String studentId;
	private StudentPrivateState studentPrivateState;
	
	public Unregister(String StudentId) {
		this.studentId=studentId;
		this.studentPrivateState = (StudentPrivateState)this.pool.getActors().get(StudentId);
	}
	
	

	/**
	 * <h1>start-unregister</h1>
	 * this method tries to unregister a student from a course<br>
	 * it will do so by sending a sub action of type {@link #UnRegistrationConformation}<br>
	 * to the student actor to check if he is indeed registered to the course, if he <br>
	 * is, the sub action will update the student private state<br>
	 * and return a massage to the original {@link #Unregister}<br>
	 * action, the the operation succeeded.
	 */
	@Override
	protected void start() {
		if(((CoursePrivateState)this.actorState).getRegStudents().contains(studentId)) {
			if(((CoursePrivateState)this.actorState).getAvailableSpots()>0) {
				ArrayList<Action<Boolean>> subActions = new ArrayList<>();
				//sending a sub action to the student actor to remove him self from this course
				UnRegistrationConformation conf = new UnRegistrationConformation(this.actorId);
				this.pool.submit(conf,this.studentId, studentPrivateState);
				subActions.add(conf);
				this.then(subActions, ()->{
					//will be executed when all the SubActions will finish
					//and also after the action will get back into his original
					//queue
					Boolean resualt = subActions.get(0).getResult().get();
					if(resualt) {
						((CoursePrivateState)this.actorState).unRegister(this.studentId);
					}
					else {System.out.println("unregistration failed");}
					this.complete(resualt);

				});
				
		}
		else {
			this.complete(false);
			System.out.println("student is not registerd");
		}
	}

	}
}

/**
 * 
 */
package bgu.spl.a2.sim.Actions;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.Simulator;
import bgu.spl.a2.sim.Actions.subActions.*;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

/**
 * @author Guy-Amit
 */
public class Participate_In_Course extends Action<Boolean> {


	/**
	 * <h1>Participate_In_Course action</h1>
	 * <h2>general notes:</h2>>
	 * 	this.actorState is the private state of the course
	 *	this.actorId is the Id of the course
	*/
	
	private String studendId;
	private StudentPrivateState studentPrivateState;
	private Integer grade;//optional
	private AtomicBoolean failed;
	public Participate_In_Course(String studentId,int grade) {
		this.studendId=studentId;
		this.grade = new Integer(grade);
		this.actionName="Particapate in course";
		this.failed= new AtomicBoolean(false);
	}
	
	public Participate_In_Course(String studentId) {
		this.studendId=studentId;
		this.grade= new Integer(0);
		this.actionName="Particapate in course";
	}
	
	/**
	 * <h1>start-Participate_In_Course</h1>
	 * this method tries to register a student to a course<br>
	 * it will do so by sending a sub action to the student actor<br>
	 * to check if he meets the prequisites of the course, if he <br>
	 * does, the sub action will update the student private state<br>
	 * and return a massage to the original {@link #Participate_In_Course} action the registration<br>
	 * Succeeded 
	 */
	@Override
	protected void start() {
		// TODO Auto-generated method stub
		this.studentPrivateState = (StudentPrivateState)this.pool.getPrivaetState(this.studendId);
		if(((CoursePrivateState)this.actorState).getAvailableSpots()>0) {
			/****Indicating that the registering is in proccses****
			 *  --decreasing the spots, so other student wont be able to register
			 *  --adding the student to the list, so other actions will 
			 *  	know he is indeed in registering proccess
			 */
			((CoursePrivateState)this.actorState).changeSpots(-1); 
			((CoursePrivateState)this.actorState).getRegStudents().add(this.studendId);
			
			ArrayList<Action<Boolean>> subActions = new ArrayList<>();
			RegistrationConformation conf;
			if(this.grade==null)
				conf = new RegistrationConformation(actorId);
			else conf = new RegistrationConformation(actorId, grade);
			subActions.add(conf);
			this.pool.submit(conf,this.studendId, studentPrivateState);
			this.then(subActions, ()->{
				//will be executed when all the SubActions will finish
				//and also after the action will get back into his original
				//queue
				//TODO:hendle the concurrent close curse action
				if(((CoursePrivateState)this.actorState).getAvailableSpots()!=-1) {
					Boolean result = subActions.get(0).getResult().get();
					((CoursePrivateState)this.actorState).changeSpots(1);
					((CoursePrivateState)this.actorState).getRegStudents().remove(this.studendId);
					//complete the registering proccess according to the result
					if(result) 
						((CoursePrivateState)this.actorState).register(this.studendId);
					else
						System.out.println("registration failed");
					this.complete(result);
				}
				else {//course has been closed before before finished registering
					this.complete(false);
					//AvailableSpots and registered list should already be updated
					//and the sub actions of the close course should have taken care of the student private state
				}
			});
		}else {
			this.complete(false);
			System.out.println("no spots available");
		}
		this.actorState.addRecord(actionName);
}

	
	@Override
	public String toString() {
		return this.studendId+": Participate in course";
	}

}

/**
 * 
 */
package bgu.spl.a2.sim.Actions;

import java.util.ArrayList;

import bgu.spl.a2.Action;
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
	
	public Participate_In_Course(String studentId,int grade) {
		this.studendId=studentId;
		this.grade = new Integer(grade);
		this.studentPrivateState = (StudentPrivateState)this.pool.getPrivaetState(studentId);
	}
	
	public Participate_In_Course(String studentId) {
		this.studendId=studentId;
		this.studentPrivateState = (StudentPrivateState)this.pool.getPrivaetState(studentId);
		this.grade= new Integer(0);
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
		if(((CoursePrivateState)this.actorState).getAvailableSpots()>0) {
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
				Boolean resualt = subActions.get(0).getResult().get();
				this.complete(resualt);
				if(resualt) {
					((CoursePrivateState)this.actorState).register(this.studendId);
				}
				else {System.out.println("registration failed");}
			});
			
		}else {
			this.complete(false);
			System.out.println("no spots available");
		}
		
	}

}

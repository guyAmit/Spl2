package bgu.spl.a2.sim.Actions;

import java.util.ArrayList;
import java.util.List;

import bgu.spl.a2.Action;
/**
 * 
 * @author Guy-Amit
 *
 */
public class Register_With_Preferences extends Action<Boolean> {
	
	
	/**
	 * <h1>RegistrationConformation action</h1>
	 * <h2>general notes:</h2>>
	 * 	this.actorState is the private state of the student
	 *	this.actorId is the Id of the student
	*/
	
	private ArrayList<String> preferences;
	private ArrayList<Integer> grades;
	
	public Register_With_Preferences(ArrayList<String> preferences, List<Integer> Grades) {
		this.preferences=preferences;
		this.grades=grades;
	}
	
	public Register_With_Preferences(ArrayList<String> preferences) {
		this.preferences=preferences;
	}
	
	/**
	 * <h1>start-Register_With_Preferences</h1>
	 * the action will start by taking the first course of the list and try to register to it,<br>
	 * if it will not succeed it will enqueue it self back into the queue and do the same with the <br>
	 * next courses.
	 */
	@Override
	public void start() {
		int grade=0;
		String courseId;
		if(!this.preferences.isEmpty()) {
			courseId = this.preferences.remove(0);
			if(this.grades!=null && !this.grades.isEmpty()) grade=this.grades.remove(0);
			Participate_In_Course participateInCourseAction = new Participate_In_Course(this.actorId, grade);
			ArrayList<Action<Boolean>> subActions = new ArrayList<>();
			subActions.add(participateInCourseAction);
			this.pool.submit(participateInCourseAction, courseId, this.pool.getPrivaetState(courseId));
			this.then(subActions, ()->{
				//will be executed when all the SubActions will finish
				//and also after the action will get back into his original
				//queue
				Boolean resualt = subActions.get(0).getResult().get();
				if(!resualt) {
					this.call=null; //enabling the action to go back into the queue s.t. it will not start from calling the callback
					this.pool.submit(this, this.actorId, this.actorState); //submitting the action back into his queue
				}
				else
					this.complete(true);
			});
		}
		else {
			this.complete(false);
			System.out.println("regestration with prefernces failed");
		}
	}

}

package bgu.spl.a2.sim.Actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.Simulator;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
/**
 * 
 * @author Guy-Amit
 *
 */
public class Open_new_spots extends Action<Object> {

	/**
	 * <h1>Open_new_spots action</h1>
	 * <h2>general notes:</h2>>
	 * 	this.actorState is the private state of the course
	 *	this.actorId is the Id of the course
	*/
	
	private String courseId;
	private CoursePrivateState coursePrivateState;
	private int spaces;
	
	public Open_new_spots(String courseId,int spaces) {
		this.courseId=courseId;
		this.coursePrivateState=(CoursePrivateState)this.actorState; 
		this.actionName="Open new spots";
		this.spaces=spaces;
	}
	
	/**
	 * <h1>start-open new spots</h1>
	 * check if the course exists, if it does and it is not closed<br>
	 * this method will directly add new available spots in the course
	 */
	@Override
	public void start() {
		if(this.coursePrivateState!=null) {
			if(this.coursePrivateState.getAvailableSpots()!=-1) {
				this.coursePrivateState.changeSpots(this.spaces);
			}
			else {System.out.println("course is closed");}
			}
		else {System.out.println("course does not exists");}
		this.complete(true);
		this.actorState.addRecord(actionName);
		}
	
	@Override
	public String toString() {
		return "Open new spots, course: "+this.actorId;
	}
}

package bgu.spl.a2.sim.privateStates;

import java.util.ArrayList;
import java.util.List;

import bgu.spl.a2.PrivateState;

/**
 * this class describe course's private state
 */
public class CoursePrivateState extends PrivateState{

	private Integer availableSpots;
	private Integer registered;
	private List<String> regStudents;
	private List<String> prequisites;
	
	/**
 	 * Implementors note: you may not add other constructors to this class nor
	 * you allowed to add any other parameter to this constructor - changing
	 * this may cause automatic tests to fail..
	 */
	public CoursePrivateState() {
		availableSpots = 100;
		registered = 0;
		regStudents = new ArrayList<>();
		prequisites = new ArrayList<>();
	}
	
	public CoursePrivateState(Integer availableSpots, Integer registered
			,List<String> regStudents,List <String> prequisites) {
		this.availableSpots=availableSpots;
		this.registered = registered;
		this.regStudents = regStudents;
		this.prequisites = prequisites;
	}
	public Integer getAvailableSpots() {
		return availableSpots;
	}

	public Integer getRegistered() {
		return registered;
	}

	public List<String> getRegStudents() {
		return regStudents;
	}

	public List<String> getPrequisites() {
		return prequisites;
	}
	
	/**
	 * <h1>closeCourse</h1>
	 * change the private state of this course<br>
	 * to a closed one
	 */
	public void closeCourse() {
		this.regStudents.clear();
		this.availableSpots=-1;
		this.registered=0;
	}
	/**
	 * <h1>addSpots</h1>
	 * open new spots to the course
	 * @see the sync is to prevent other threads from changing the number of
	 * 		Available spots in the course while this thread is changing it
	 * @param spaces
	 */
	public void addSpots(int spaces) {
		synchronized(this.availableSpots) {
			this.availableSpots+=spaces;
		}
	}
	/**
	 * <h1>register</h1>
	 * assisting method for changing the private state<br>
	 * of the course when a student is trying to register
	 * @param studentId
	 */
	synchronized public void register(String studentId) {
		this.regStudents.add(studentId);
		this.registered++;
		this.availableSpots--;
	}
	
	/**
	 * <h1>unRegister</h1>
	 * assisting method for changing the private state<br>
	 * of the course when a student is trying to unRegister
	 * @param studentId
	 */
	public void unRegister(String studenId) {
		this.regStudents.remove(studenId);
		this.availableSpots++;
		this.registered--;
	}
	
	@Override
	public String toString() {
		return "course private state:\n"+"registerd students:"+this.regStudents+"\n avilable spots: "
				+this.availableSpots;
	}
}

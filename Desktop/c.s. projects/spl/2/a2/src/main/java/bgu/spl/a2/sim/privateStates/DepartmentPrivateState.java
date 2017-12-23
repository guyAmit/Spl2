package bgu.spl.a2.sim.privateStates;

import java.util.ArrayList;
import java.util.List;

import bgu.spl.a2.PrivateState;

/**
 * this class describe department's private state
 */
public class DepartmentPrivateState extends PrivateState{
	private List<String> courseList;
	private List<String> studentList;
	
	/**
 	 * Implementors note: you may not add other constructors to this class nor
	 * you allowed to add any other parameter to this constructor - changing
	 * this may cause automatic tests to fail..
	 */
	public DepartmentPrivateState() {
		courseList = new ArrayList<>();
		studentList = new ArrayList<>();
	}
	public DepartmentPrivateState (List<String>courseList, List<String>studentList) {
		this.courseList = courseList;
		this.studentList = studentList;
	}
	public List<String> getCourseList() {
		return courseList;
	}

	public List<String> getStudentList() {
		return studentList;
	}
	
	@Override
	public String toString() {
		return "department private state:\n"+"Actions:"+this.getLogger()+"\n"+"students:"+this.studentList+"\n"
				+"courses: "+this.courseList;
	}
}

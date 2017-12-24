package bgu.spl.a2.sim.privateStates;

import java.util.HashMap;
import java.util.List;

import bgu.spl.a2.PrivateState;

/**
 * this class describe student private state
 */
public class StudentPrivateState extends PrivateState{

	private HashMap<String, Integer> grades;
	private long signature;
	
	/**
 	 * Implementors note: you may not add other constructors to this class nor
	 * you allowed to add any other parameter to this constructor - changing
	 * this may cause automatic tests to fail..
	 */
	public StudentPrivateState() {
		grades = new HashMap<>();
		signature = 0;
	}
	
	public void SetPrivateState(HashMap<String,Integer> grades,long signature) {
		this.grades = grades;
		this.signature = signature;
	}
	public HashMap<String, Integer> getGrades() {
		return grades;
	}

	public long getSignature() {
		return signature;
	}
	
	public void setSignature(long signature) {
		this.signature = signature;
	}
	
	public boolean meetRequirements(List<String> courses) {
		for (String courseId : courses) {
			if(!this.grades.containsKey(courseId))
					return false;		}
		return true;
	}
	
	@Override
	public String toString() {
		return "student private state:\n"+"Actions:"+this.getLogger()+"\n"+"grades"+this.getGrades()+"\n"+
				"signature:"+this.signature;
	}
}

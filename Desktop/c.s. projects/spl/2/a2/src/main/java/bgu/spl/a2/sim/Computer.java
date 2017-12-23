package bgu.spl.a2.sim;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class Computer {

	String computerType;
	long failSig;
	long successSig;
	
	
	
	
	public Computer(String computerType) {
		this.computerType = computerType;
	}
	
	/**
	 * this method checks if the courses' grades fulfill the conditions
	 * @param courses
	 * 							courses that should be pass
	 * @param coursesGrades
	 * 							courses' grade
	 * @return a signature if couersesGrades grades meet the conditions
	 */
	public long checkAndSign(List<String> courses, Map<String, Integer> coursesGrades){
		AtomicBoolean passed = new AtomicBoolean(true);
		for (String course : courses) {
			if(coursesGrades.containsKey(course)==false || coursesGrades.get(course)<56) {
				passed.compareAndSet(true, false);
				break;
			}
		}
		if(passed.get()) return successSig;
		else return failSig;
		
	}
	
	public String getComputerType() {
		return computerType;
	}

	public void setComputerType(String computerType) {
		this.computerType = computerType;
	}

	public long getFailSig() {
		return failSig;
	}

	public void setFailSig(long failSig) {
		this.failSig = failSig;
	}

	public long getSuccessSig() {
		return successSig;
	}

	public void setSuccessSig(long successSig) {
		this.successSig = successSig;
	}
	
}

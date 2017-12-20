package bgu.spl.a2.sim.Actions;

import java.util.ArrayList;
import java.util.List;

import bgu.spl.a2.Action;
import bgu.spl.a2.PrivateState;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

public class Add_Student<R> extends Action<R> {
	
	private DepartmentPrivateState departmentState = (DepartmentPrivateState)pool.getActors().get(actorId);
	private List <Action<?>> actions = new ArrayList<>();
	private String nameOfStudent="";
	@SuppressWarnings("unchecked")
	@Override
	protected void start() {
		setActionName("Add Student");
		
		actions.add(new Action() {
			@Override
			protected void start() {
				// TODO Auto-generated method stub
				departmentState.getStudentList().add(nameOfStudent);
			}
		});
		then(actions,()->{
			//actorId -> department, actorState -> student
			pool.submit(this, actorId, departmentState);
		});
		complete((R) new Object());
	}
	
	public void AddNameToBeAdded(String nameOfStudent) {
		this.nameOfStudent = nameOfStudent;
	}
}

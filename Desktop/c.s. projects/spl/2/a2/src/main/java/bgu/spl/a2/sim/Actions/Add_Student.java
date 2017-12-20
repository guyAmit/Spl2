package bgu.spl.a2.sim.Actions;

import java.util.ArrayList;
import java.util.List;

import bgu.spl.a2.Action;
import bgu.spl.a2.PrivateState;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

public class Add_Student<R> extends Action<R> {
	
	DepartmentPrivateState state = (DepartmentPrivateState)actorState;
	List <Action<?>> actions = new ArrayList<>();
	
	@SuppressWarnings("unchecked")
	@Override
	protected void start() {
		setActionName("Add Student");
		
		actions.add(new Action() {
			@Override
			protected void start() {
				// TODO Auto-generated method stub
				state.getStudentList().add(actorId);
			}
		});
		then(actions,()->{
			//actorId -> student actorState -> department
			pool.submit(this, actorId, actorState);
		});
		complete((R) new Object());
	}
}

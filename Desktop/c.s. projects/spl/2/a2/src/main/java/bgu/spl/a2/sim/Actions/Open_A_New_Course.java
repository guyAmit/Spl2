/**
 * 
 */
package bgu.spl.a2.sim.Actions;

import java.util.ArrayList;
import java.util.List;
import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;

/**
 * @author AdamSh
 *@param <R>
 */
public class Open_A_New_Course<R> extends Action<R> {

	DepartmentPrivateState state = (DepartmentPrivateState)actorState;
	List <Action<?>> actions = new ArrayList<>();
	
	@SuppressWarnings("unchecked")
	@Override
	protected void start() {
		setActionName("Open A New Course");
		actions.add(new Action() {
			@Override
			protected void start() {
				// TODO Auto-generated method stub
				state.getCourseList().add(actorId);
			}
		});
		then(actions,()->{
			//actorId -> course actorState -> department
			pool.submit(this, actorId, actorState);
		});
		complete((R) new Object());
	}
}

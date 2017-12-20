/**
 * 
 */
package bgu.spl.a2.sim.Actions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicBoolean;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;
/**
 * @author AdamSh
 * @param <R>
 *
 */
public class Participating_In_Course<R> extends Action<R> {
	
	private Integer grade = new Integer(-1);
	private CoursePrivateState courseState = (CoursePrivateState)pool.getActors().get(actorId);
	private StudentPrivateState studentState = (StudentPrivateState) actorState;
	private AtomicBoolean canRegister = new AtomicBoolean(true);
	private List<Action<?>> actions = new ArrayList<Action<?>>();
	
	@SuppressWarnings("unchecked")
	@Override
	protected void start() {
		// TODO Auto-generated method stub
		setActionName("Add Student");
		
		actions.add(new Action() {
			@Override
			protected void start() {
				// TODO Auto-generated method stub
				//no need to check if grade at list 56
				for(String course : courseState.getPrequisites()){
					if(!studentState.getGrades().containsKey(course)) {
						canRegister.compareAndSet(true, false);
						break;
					}
					if(canRegister.get()) {
						courseState.getRegStudents().add(actorId);
						courseState.setAvailableSpots(courseState.getAvailableSpots() - 1);
						studentState.getGrades().put(actorId,grade);
					}
				}
			}
		});
		then(actions,()->{
			//actorId -> course actorState -> student
			pool.submit(this, actorId, pool.getPrivaetState(actorId));
		});
		complete((R) new Object());
	}
	
	public void giveGrade(Integer grade) {
		this.grade = grade;
	}
}

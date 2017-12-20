package bgu.spl.a2.sim.Actions;

import java.util.ArrayList;
import java.util.List;
import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;

public class Add_Spaces<R> extends Action<R> {

	private CoursePrivateState courseState = (CoursePrivateState)actorState;
	private List <Action<?>> actions = new ArrayList<>();
	private Integer newPlases = 0;
	
	@SuppressWarnings("unchecked")
	@Override
	protected void start() {
		setActionName("Open New Places In Course");
		actions.add(new Action() {
			@Override
			protected void start() {
				// TODO Auto-generated method stub
				if(courseState.getAvailableSpots() != -1)
				courseState.setAvailableSpots(courseState.getAvailableSpots() + newPlases);
			}
		});
		then(actions,()->{
			//actorId -> course ,actorState -> course
			pool.submit(this, actorId, actorState);
		});
		complete((R) new Object());
	}
	/*
	 * SHOULD BE CALLED BEFORE START
	 * 
	 * 
	 * @param newPlases
	 * Sets the field newPlases to a new Integer value
	 */
	public void SetPlacesToAdd(Integer newPlases) {
		this.newPlases = newPlases;
	}

}

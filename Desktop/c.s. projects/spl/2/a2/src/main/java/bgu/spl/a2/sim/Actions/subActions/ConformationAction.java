package bgu.spl.a2.sim.Actions.subActions;

import bgu.spl.a2.Action;

public class ConformationAction extends Action<Boolean> {

	/**
	 * <h1>ConformationAction</h1>
	 * a dummy action, that we will use to make sure that some actions<br>
	 * indeed created actors. we will send this action to a new actor,<br>
	 * if the {@link #start()} method is called we will know that the actor is<br>
	 * indeed functioning.
	 */
	public ConformationAction() {
	}
	
	@Override
	public void start() {	
		complete(true);
	}
}

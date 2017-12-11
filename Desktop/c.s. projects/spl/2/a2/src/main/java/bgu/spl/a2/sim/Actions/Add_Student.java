package bgu.spl.a2.sim.Actions;

import java.util.List;

import bgu.spl.a2.Action;
import bgu.spl.a2.PrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

public class Add_Student<R> extends Action<R> {
	
	@Override
	protected void start() {
	this.pool.getActors().put(actorId, actorState);
	//(StudentPrivateState)actorState);	
	}
}

package bgu.spl.a2.sim.Actions;

import java.util.List;

import bgu.spl.a2.Action;

public class Add_Student<R> extends Action<R> {

	private List<Action> actions;
	private String depId;
	public Add_Student(String depId) {
		this.depId = depId;
	}
	@Override
	protected void start() {
		// TODO Auto-generated method stub
		this.actionName = "Add Student";
		//this.pool.getActors().put(this.actorId, new PrivateState())
	}
}

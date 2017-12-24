/**
 * 
 */
package bgu.spl.a2.sim.Actions.subActions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

/**
 * @author Guy-Amit
 *
 */
public class CheckAndSignConformation extends Action<Boolean> {

	private Long signture;
	private StudentPrivateState studentPrivateState;
	public CheckAndSignConformation(Long signture) {
		this.signture=signture;
		this.actionName="CheckAndSignConformation";
	}
	@Override
	protected void start() {
		this.studentPrivateState=(StudentPrivateState)this.actorState;
		this.studentPrivateState.setSignature(this.signture);
		this.complete(true);
	}

}

package bgu.spl.a2.sim;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import bgu.spl.a2.Promise;

/**
 * 
 * this class is related to {@link Computer}
 * it indicates if a computer is free or not
 * 
 * Note: this class can be implemented without any synchronization. 
 * However, using synchronization will be accepted as long as the implementation is blocking free.
 *
 */
public class SuspendingMutex {
	
	
	private Computer computer;
	private AtomicBoolean isFree; //false if the computer is acquired true if free
	
	/**
	 * <h1>waitingList</h1>
	 * a queue that represent all the action that are waiting to use the</br>
	 * {@link #computer}. once the computer is free, a promise from the qeueue will</br>
	 * get resolved, and notify the action that is waiting for the {@link @Computer}
	 */
	private LinkedBlockingQueue<Promise<Computer>> waitingList;
	
	/**
	 * Constructor
	 * @param computer
	 */
	public SuspendingMutex(Computer computer){
		//TODO: replace method body with real implementation
		this.computer = computer;
		this.isFree = new AtomicBoolean(true);
		this.waitingList=new LinkedBlockingQueue<>();
	}
	/**
	 * Computer acquisition procedure
	 * Note that this procedure is non-blocking and should return immediatly
	 * 
	 * creates a new promise for the computer, adding a callback</br>
	 * that will lock the computer for the waiting action.<br>
	 * @see the user should check if the promise is resolved or not
	 * 
	 * @return a promise for the requested computer
	 */
	public Promise<Computer> down(){
		Promise<Computer> promise = new Promise<>();
		//locking the computer if it is available
		if(this.isFree.compareAndSet(true, false))
			promise.resolve(computer);
		else {//returning a promise for this computer with a call back that will lock it when it is free
			promise.subscribe(()->{this.isFree.compareAndSet(true, false);});
			this.waitingList.add(promise);
		}
		return promise;
	}
	
	
	/**
	 * Computer return procedure
	 * releases a computer which becomes available in the warehouse upon completion
	 * 
	 * {@link #up} is used by the ware house
	 */
	public void up(){
		if(this.waitingList.size()>0) {
			this.waitingList.remove().resolve(this.computer);
		}
		this.isFree.compareAndSet(false, true);
	}
}

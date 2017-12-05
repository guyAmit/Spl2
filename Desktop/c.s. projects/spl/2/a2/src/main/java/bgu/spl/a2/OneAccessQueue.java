/**
 * 
 */
package bgu.spl.a2;

import java.util.LinkedList;
import java.util.concurrent.atomic.*;
/**
 * @author Guy-Amit
 * @param <E>
 *
 */
public class OneAccessQueue<E> extends LinkedList<E>{
	
	private AtomicBoolean isFree;
	
	public OneAccessQueue(){
		super();
		isFree.set(true);
	}
	/**
	 * <h1>isFree</h1>
	 * <p>{@link #isFree} will return True if no other thread is working on the Queue</p>
	 * @return if the queue is free to work
	 */
	public AtomicBoolean isFree(){
		return this.isFree;
	}
	/**
	 * <h1>enqueue</h1>
	 * <p> the method tries to insert a new value into the Queue, <br>
	 * 	if no one is working on the queue, the addition will go as <br>
	 * 	planned, otherwise the method will return False </p>
	 * @Pre the Queue should be free, i.e. the user locked it using the {@link #tryToLock()} method
	 * @param e-will be used to hold actions
	 * @return True if the addition was successful, False otherwise
	 */
	public  AtomicBoolean enqueue(E e) {
		if(this.isFree().get()) {
			super.addLast(e);
			return this.isFree();
		}
		else{
			return this.isFree();
		}
	}
	
	/**
	 * <h1>dequeue</h1>
	 * <p>the method tries to remove the first item in the Queue,<br>
	 * 	if no one is working on the queue, the removal will go as <br>
	 * 	planned, otherwise the method will return <b>Null</b> </p></p>
	 * @Pre the Queue should be free, i.e. the user locked it using the {@link #tryToLock()} method
	 * @return the first item in the Queue
	 */
	public E dequeue() {
		if(this.isFree().get()) {
			return super.removeFirst();
		}
		else {
			return null;
		}
	}
	
	/**
	 * <h1>enqueue</h1>
	 * <p>the method will try to get accesses to the queue<br>
	 * if it succeeds it will lock the Queue,and return True<br>
	 * synchronized explanation: the if the Queue is free, we need to lock it without getting interrupted</p>
	 * @return True is lock was a success
	 */
	 public AtomicBoolean tryToLock() {
		try {
			synchronized (isFree) {
			if(this.isFree().get()) {
					this.isFree.set(false);
					return new AtomicBoolean(true);
			}
			else
				return this.isFree();
			}
		}catch (Exception e) {
			return this.isFree();
		}
	}

}

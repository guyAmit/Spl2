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
	private String name;

	
	public OneAccessQueue(String name){
		super();
		isFree.set(true);
		this.name=name;
	}
	
	
	/**
	 * <h1>getName</h1>
	 * @return the {@link #name} of the queue
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * <h1>isFree</h1>
	 * <p>{@link #isFree} will return True if no other thread is working on the Queue</p>
	 * @return if the queue is free to work
	 */
	public Boolean isFree(){
		return this.isFree.get();
	}
	
	/**
	 * <h1>enqueue</h1>
	 * <p> the method tries to insert a new value into the Queue, <br>
	 * 	if no one is working on the queue, the addition will go as <br>
	 * 	planned, otherwise the method will return False </p>
	 * @param e-will be used to hold actions
	 * @return True if the addition was successful, False otherwise
	 */
	public  Boolean enqueue(E e) {
		try {
			super.addLast(e);
			return true;
		}catch (Exception t) {
			return false;
		}
	}
	
	/**
	 * <h1>dequeue</h1>
	 * <p>the method tries to remove the first item in the Queue,<br>
	 * 	if no one is working on the queue, the removal will go as <br>
	 * 	planned, otherwise the method will return <b>Null</b> </p></p>
	 * @Pre the Queue should be locked on the using thread, i.e. the user locked it by using the {@link #tryToLock()} method
	 * @return the first item in the Queue
	 */
	public E dequeue() {
		if(!this.isFree()) {
			E returnVal = super.removeFirst();
			this.isFree.compareAndSet(false, true);
			return returnVal;
		}
		else {
			return null;
		}
	}
	
	/**
	 * <h1>tryToLock</h1>
	 * <p>the method will try to get accesses to the queue<br>
	 * if it succeeds it will lock the Queue,and return True<br>
	 * we will achieve that using the <b>compareAndSet</b> method<br>
	 * <b>use this method first, do not deqeueu without using this method!!</b></p>
	 * @return True if lock was a success
	 */
	 public Boolean tryToLock() {
		try {
			if(this.isFree.compareAndSet(true, false)) {
				return true;
			}
			else {
				return false;
			}
		}catch (Exception e) {
			return false;
		}
	}
	 
	 
	 /**
	  * <h1>length</h1>
	  * @return the length of the queue
	  */
	 public int length() {
		 return super.size();
	 }


}

/**
 * 
 */
package bgu.spl.a2;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.*;
/**
 * @author Guy-Amit
 * @param <E>
 *
 */
public class OneAccessQueue<E> extends LinkedBlockingQueue<E>{
	
	private AtomicBoolean frontLock;
	private AtomicBoolean backLock;
	private AtomicInteger length;
	
	public OneAccessQueue(){
		super();
		this.frontLock = new AtomicBoolean(true);
		this.backLock = new AtomicBoolean(true);
		this.length = new AtomicInteger(0);
	}
	
	
	/**
	 * <h1>getSize</h1>
	 * @return the {@link #length} of the queue, this is a concurrent method
	 */
	public int getSize() {
		return this.length.get();
	}
	
	
	/**
	 * <h1>isBackFree</h1>
	 * <p>{@link #backLock} will return True if no other thread is working on the back of the Queue</p>
	 * @return if the queue is free to work
	 */
	public Boolean isBackFree(){
		return this.backLock.get();
	}
	
	
	/**
	 * <h1>isFrontFree</h1>
	 * <p>{@link #isFrontLock} will return True if no other thread is working on the front of the Queue</p>
	 * @return if the queue is free to work
	 */
	public Boolean isFrontFree(){
		return this.frontLock.get();
	}
	
	/**
	 * <h1>enqueue</h1>
	 * <p> the method tries to insert a new value into the Queue, <br>
	 * 	if no one is working on the queue, the addition will go as <br>
	 * 	planned, otherwise the method will return False </p>
	 * @param e-will be used to hold actions
	 * @pre in order to use this method properly you must use the {@link #tryToLockEequeue()} method first and get true
	 * @return True if the addition was successful, False otherwise
	 */
	public  Boolean enqueue(E e) {
		try {
			if(!this.backLock.get()) {
				super.put(e);
				this.length.incrementAndGet();
				this.backLock.compareAndSet(false, true);
				return true;
			}
			return false;
		}catch (Exception t) {
			return false;
		}
	}
	
	/**
	 * <h1>dequeue</h1>
	 * <p>the method tries to remove the first item in the Queue,<br>
	 * 	if no one is working on the queue, the removal will go as <br>
	 * 	planned, otherwise the method will return <b>Null</b> </p></p>
	 * @pre in order to use this method properly you must use the {@link #tryToLockDequeue()} method first and get true
	 * @return the first item in the Queue
	 */
	public E dequeue() {
		if(!this.frontLock.get()) {
			if(this.length.get()>0) {
				E returnVal=null;
				try {
					returnVal = super.poll();
				}catch(NoSuchElementException e) {}
				this.length.decrementAndGet();
				return returnVal;
			}
			return null;
		}
		else {
			return null;
		}
	}
	
	/**
	 * <h1>tryToLockEnqueue</h1>
	 * <p>trying to lock the back of the queue in order to enqueue</p>
	 * @return true if the back has been locked 
	 */
	public boolean tryToLockEnqueue() {
		try {
			return this.backLock.compareAndSet(true, false);
		}catch(Exception e){
			
		}
		return false;
	}
	
	
	/**
	 * <h1>tryToLockDequeue</h1>
	 * <p>trying to lock the front of the queue in order to dequeue</p>
	 * @return true if the front has been locked 
	 */
	public boolean tryToLockDequeue() {
		try {
			return this.frontLock.compareAndSet(true, false);
		}catch(Exception e){
			
		}
		return false;
	}
	
	/**
	 * <h1>freeBackLock</h1>
	 * free the back lock
	 * @return true if the operation was a success
	 */
	public void freeBackLock() {
		this.backLock.set(true);
	}
	
	
	/**
	 * <h1>freeFrontLock</h1>
	 *  free the front lock
	 */
	public void freeFrontLock() {
		this.frontLock.set(true);
	}
	 
	 @Override
	 public String toString() {
		 return "backLock:"+this.backLock.get()+" frontLock:"+this.frontLock.get()+"\n items: "+super.toString()+"\n";
	 }


}

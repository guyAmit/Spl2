package bgu.spl.a2;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.plaf.SliderUI;

import bgu.spl.a2.sim.Simulator;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;

/**
 * represents an actor thread pool - to understand what this class does please
 * refer to your assignment.
 *
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add can only be
 * private, protected or package protected - in other words, no new public
 * methods
 */

public class ActorThreadPool {
	
	private ArrayList<Thread> threads; // hold all the threads, and a boolean representing whether they should stop working
	private Map<String,PrivateState> actors; //<actorID,private state>
	private Map<String,OneAccessQueue<Action<?>>> actions; // <actorID,actionQueue>
	public static VersionMonitor monitor;
	public static AtomicInteger size;
	private AtomicBoolean shotDown;
	/**
	 * creates a {@link ActorThreadPool} which has nthreads. Note, threads
	 * should not get started until calling to the {@link #start()} method.
	 *
	 * Implementors note: you may not add other constructors to this class nor
	 * you allowed to add any other parameter to this constructor - changing
	 * this may cause automatic tests to fail..
	 *
	 * @param nthreads
	 *            the number of threads that should be started by this thread
	 *            pool
	 */
	public ActorThreadPool(int nthreads) {
		size = new AtomicInteger(0);
		this.shotDown=new AtomicBoolean(false);
		this.actors= new ConcurrentHashMap<String,PrivateState>();
		this.actions = new ConcurrentHashMap<String,OneAccessQueue<Action<?>>>();
		this.threads = new ArrayList<Thread>();
		monitor = new VersionMonitor();
		for(int i =0; i<nthreads; i++) {
			this.threads.add(new Thread(()->{
				Thread thisThread = Thread.currentThread();
				while(!thisThread.isInterrupted()) { //should be true until changed by the shutdown method
					for (Map.Entry<String, OneAccessQueue<Action<?>>> entry : actions.entrySet()) {
						if(thisThread.isInterrupted()) break;
						if(entry.getValue().getSize() >0) {
							if(entry.getValue().tryToLockDequeue()) {
									if(entry.getValue().getSize()==0) {
										entry.getValue().freeFrontLock();										
										continue;
									}
									else {
										if(this.shotDown.get()) {entry.getValue().freeFrontLock(); break;}
										Action action = entry.getValue().dequeue();
										if(action==null) {entry.getValue().freeFrontLock(); continue;}
										String actorId = entry.getKey();
										PrivateState actorPrivateState = this.getPrivaetState(actorId);
										size.decrementAndGet();
										action.handle(this, actorId, actorPrivateState);
										System.out.println(Simulator.Actioncounter.getCount());
										System.out.println(size.get());
										monitor.inc();
									}
									entry.getValue().freeFrontLock();										
							}
							
						}
						}
						try {
							if(!this.shotDown.get() && !this.ifAllNotSleeping())
								monitor.await(monitor.getVersion());
						} catch (InterruptedException e) {
							thisThread.interrupt();
						}						
				}
			}));
		}
	}	
	
	
	/**
	 * <h1>ifAllSleeping</h1>
	 * checks if all other the threads are not waiting
	 */
	private boolean ifAllNotSleeping() {
		Thread thisThread =Thread.currentThread();
		for (Thread thread : threads) {
			if(!thread.equals(thisThread) && thread.getState()!=Thread.State.WAITING) {
				return true;
			}
		}
		return false;
	}
		  
	/**
	 * getter for actors
	 * @return actors
	 */
	public Map<String, PrivateState> getActors(){
		return actors;
	}
	/**
	 * getter for actor's private state
	 * @param actorId actor's id
	 * @return actor's private state
	 */
	public PrivateState getPrivaetState(String actorId){
		return actors.get(actorId);
	}
	
	/**
	 * submits an action into an actor to be executed by a thread belongs to
	 * this thread pool
	 *
	 * @param action
	 *            the action to execute
	 * @param actorId
	 *            corresponding actor's id
	 * @param actorState
	 *            actor's private state (actor's information)
	 * @see if the actorId is not in the private states map, thats mean that we need to crete<br>
	 * 		a new department private state because there are no other options.
	 * @sync explanation: we want to prevent a state where we have added the new action queue into</br>
	 * 		into the data structure without his private state
	 */
	public void submit(Action<?> action, String actorId, PrivateState actorState) {
		OneAccessQueue<Action<?>> actionQueue = this.actions.get(actorId);
		if(actionQueue!=null & actorState!=null) {
			Boolean lock=false;
			do{
				lock=actionQueue.tryToLockEnqueue();
			}while(!lock);
			actionQueue.enqueue(action);			
		}
		else if(actionQueue==null && actorState!=null){ //creating a new course/student actor 
			actionQueue = new OneAccessQueue<>();
			if(action!=null) {
				actionQueue.tryToLockEnqueue();
				actionQueue.enqueue(action);
			}
			this.actions.put(actorId, actionQueue);
		}
		else { //creating a new department, and putting the action into it
			actionQueue = new OneAccessQueue<Action<?>>();
			if(action!=null) {
				actionQueue.tryToLockEnqueue();
				actionQueue.enqueue(action);
			}
			DepartmentPrivateState depratmentPrivateState = new DepartmentPrivateState();
			this.actions.put(actorId, actionQueue);
			this.actors.put(actorId, depratmentPrivateState);
			}
		size.incrementAndGet();
		}
		

	/**
	 * closes the thread pool - this method interrupts all the threads and waits
	 * for them to stop - it is returns *only* when there are no live threads in
	 * the queue.
	 *
	 * after calling this method - one should not use the queue anymore.
	 *
	 * @throws InterruptedException
	 *             if the thread that shut down the threads is interrupted
	 */
	public void shutdown() throws InterruptedException {
		this.shotDown.set(true);
		synchronized (this.threads) {
		this.threads.forEach(thread->{thread.interrupt();});
		}
		monitor.inc();
		synchronized (this) {
			this.notifyAll();
		}
	}

	/**
	 * start the threads belongs to this thread pool
	 */
	public void start() {
		// TODO: replace method body with real implementation
		for (Thread entry : this.threads) {
			entry.start();
		}
	}

}
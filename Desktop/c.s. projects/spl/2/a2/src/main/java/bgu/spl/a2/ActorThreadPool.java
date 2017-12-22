package bgu.spl.a2;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

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
	private AtomicBoolean isShutDown;
	private Map<String,PrivateState> actors; //<actorID,private state>
	private Map<String,OneAccessQueue<Action<?>>> actions; // <actorID,actionQueue>
	public static VersionMonitor monitor;
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
		// TODO: replace method body with real implementation
		this.actors= new ConcurrentHashMap<String,PrivateState>();
		this.actions = new ConcurrentHashMap<String,OneAccessQueue<Action<?>>>();
		this.threads = new ArrayList<Thread>();
		monitor = new VersionMonitor();
		this.isShutDown = new AtomicBoolean(true);
		for(int i =0; i<nthreads; i++) {
			this.threads.add(new Thread(()->{

				while(this.isShutDown.get()) { //should be true until changed by the shutdown method
					for (Map.Entry<String, OneAccessQueue<Action<?>>> entry : actions.entrySet()) {
						if(entry.getValue().size() >0) {
							if(entry.getValue().tryToLockDequeue()) {
									Action action = entry.getValue().dequeue();
									if(action==null) continue;
									String actorId = entry.getKey();
									PrivateState actorPrivateState = this.getPrivaetState(actorId);
									actorPrivateState.addRecord(action.getActionName());
									action.handle(this, actorId, actorPrivateState);
									monitor.inc();
									entry.getValue().freeFrontLock(); // checking that the queue indeed get freed
								}
							}
						}
						try {
							monitor.await(monitor.getVersion());
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

				}
				
		
				
			}));
			this.threads.set(i, null);
		}
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
	 */
	public void submit(Action<?> action, String actorId, PrivateState actorState) {
<<<<<<< HEAD
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
			this.actors.put(actorId,actorState);
=======
		if(this.actions.containsKey(actorId)) {
			this.actions.get(actorId).add(action);
			this.actors.get(actorId).addRecord(action.getActionName());
>>>>>>> 88f28c05f4085f53da17967c2778531d1addd2e2
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
			isShutDown.compareAndSet(true, false);
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

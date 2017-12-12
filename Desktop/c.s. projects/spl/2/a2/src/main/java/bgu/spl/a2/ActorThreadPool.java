package bgu.spl.a2;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

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
	//private ArrayList<OneAccessQueue<Action>> actionsQueue; //action Queues for each actor
	private Map<String,OneAccessQueue<Action>> actions; // <actorID,actionQueue>
	private VersionMonitor version;
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
		  this.actions = new ConcurrentHashMap<String,OneAccessQueue<Action>>();
		  //this.actionsQueue = new ArrayList<OneAccessQueue<Action>>();
		  this.threads = new ArrayList<Thread>();
		  this.isShutDown = new AtomicBoolean(true);
		  this.version = new VersionMonitor();
		  this.threads.add(new Thread(()->{
			  
			  while(this.isShutDown.get()) { //should be true until changed by the shutdown method
				  for (Map.Entry<String, OneAccessQueue<Action>> entry : actions.entrySet()) {
					if(entry.getValue().tryToLock()) {
						Action act = entry.getValue().dequeue();
						String actorId = entry.getKey();
						PrivateState state = this.getPrivaetState(actorId);
						act.handle(this,actorId, state);
						version.inc();
					}
					else continue;
				}
				  try {
					version.await(version.getVersion()+1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			  }
			  
		  }));
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
	 */
	public void submit(Action<?> action, String actorId, PrivateState actorState) {
		if(this.actions.containsKey(actorId)) {
			this.actions.get(actorId).add(action);
		}
		else {
			OneAccessQueue<Action> newQueue = new OneAccessQueue<Action>();
			newQueue.enqueue(action);
			this.actions.put(actorId, newQueue);
			this.actors.put(actorId, actorState);
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

package bgu.spl.a2;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
	
	private ArrayList<Thread> Threads;
	private Map<String,PrivateState> privateStates; //<actorID,private state>
	private ArrayList<OneAccessQueue<Action>> actionsQueue; //action Queues for each actor
	private Map<String,OneAccessQueue<Action>> actors; // <actorID,actionQueue>
	
	
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
		  this.privateStates= new ConcurrentHashMap<String,PrivateState>();
		  this.actors = new ConcurrentHashMap<String,OneAccessQueue<Action>>();
		  this.actionsQueue = new ArrayList<>();
		  this.Threads = new ArrayList<>();
		  for (int i = 0; i < nthreads; i++) {
			this.Threads.add(new Thread(()->
			{
				//ToDo:: implement runnable
				
			}));
		  }
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
		//check if actor already exists
		if(this.privateStates.containsKey(actorId)) {
			while(!this.actors.get(actorId).tryToLock().get())
				this.actors.get(actorId).enqueue(action);
		}
		else {//actor does not exits
			this.privateStates.put(actorId, actorState);
			OneAccessQueue<Action> newQueue = new OneAccessQueue<>();
			while(!newQueue.tryToLock().get()) {
				newQueue.enqueue(action);
				this.actors.put(actorId, newQueue);
				this.actionsQueue.add(newQueue);
			}
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
		// TODO: replace method body with real implementation
		throw new UnsupportedOperationException("Not Implemented Yet.");
	}

	/**
	 * start the threads belongs to this thread pool
	 */
	public void start() {
		// TODO: replace method body with real implementation
		for (Thread thread : Threads) {
			thread.start();
		}
	}

}

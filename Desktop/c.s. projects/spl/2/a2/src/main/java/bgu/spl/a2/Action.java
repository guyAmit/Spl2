package bgu.spl.a2;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * an abstract class that represents an action that may be executed using the
 * {@link ActorThreadPool}
 *
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add to this class can
 * only be private!!!
 *
 * @param <R> the action result type
 */
public abstract class Action<R> {
	
	public static AtomicInteger actionInPhase;
	protected Promise<R> promise = new Promise<>();
	protected R result = null;
	protected String actionName = "";
	protected ActorThreadPool pool = null;
	protected String actorId = "";
	protected PrivateState actorState = null;
	protected callback call = null;
	/**
     * start handling the action - note that this method is protected, a thread
     * cannot call it directly.
     */
    protected abstract void start();

    /**
    *
    * start/continue handling the action
    *
    * this method should be called in order to start this action
    * or continue its execution in the case where it has been already started.
    *
    * IMPORTANT: this method is package protected, i.e., only classes inside
    * the same package can access it - you should *not* change it to
    * public/private/protected
    *
    *if {@link #call}==null -> this is the first time we are calling the action.
    *if not this is a continuation of a computation
    */
    /*package*/ final void handle(ActorThreadPool pool, String actorId, PrivateState actorState) {
    	if(this.pool == null)this.pool = pool;
    	if(this.actorId.equals(""))this.actorId = actorId;
    	if(this.actorState == null)this.actorState = actorState;
    	if(this.call != null) { 
    		this.call.call();
    	}
    	else {
    		this.start();//should call then and complete
    	}
    }
    
    
    /**
     * add a callback to be executed once *all* the given actions results are
     * resolved
     * 
     * Implementors note: make sure that the callback is running only once when
     * all the given actions completed.
     *
     * @param actions
     * @param callback the callback to execute once all the results are resolved
     */
    protected final void then(Collection<? extends Action<?>> actions, callback callback) {
    	this.call = callback;
    	AtomicInteger i = new AtomicInteger(actions.size());
    	for(Action<?> action: actions){
    		action.promise.subscribe(()->{
    			i.getAndDecrement();
    			if(i.get()<=1)
    			{
    				//the last SubAction that get called should enqueue the original 
    				//action back into his queue
    				this.sendMessage(this, actorId, actorState);
    			}
    		});
    	}
    }

    /**
     * resolve the internal result - should be called by the action derivative
     * once it is done.
     *
     * @param result - the action calculated result
     */
    protected final void complete(R result) {
    	this.promise.resolve(result);
    }
    
    /**
     * @return action's promise (result)
     */
    public final Promise<R> getResult() {
    	return this.promise;
    }
    
    /**
     * send an action to an other actor
     * 
     * @param action
     * 				the action
     * @param actorId
     * 				actor's id
     * @param actorState
	 * 				actor's private state (actor's information)
	 *    
     * @return promise that will hold the result of the sent action
     */
    public Promise<?> sendMessage(Action<?> action, String actorId, PrivateState actorState){
    	this.pool.submit(action, actorId, actorState);
    	//TODO: need to replace with real implementation
    	return null;
    }
    /**
	 * set action's name
	 * @param actionName
	 */
	public void setActionName(String actionName){
        this.actionName = actionName;
	}
	
	/**
	 * @return action's name
	 */
	public String getActionName(){
        return this.actionName;
	}
}

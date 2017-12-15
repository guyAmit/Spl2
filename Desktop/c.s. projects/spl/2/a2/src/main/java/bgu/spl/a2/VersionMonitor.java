package bgu.spl.a2;
/**
 * @author Guy-Amit
 */
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Describes a monitor that supports the concept of versioning - its idea is
 * simple, the monitor has a version number which you can receive via the method
 * {@link #getVersion()} once you have a version number, you can call
 * {@link #await(int)} with this version number in order to wait until this
 * version number changes.
 *
 * you can also increment the version number by one using the {@link #inc()}
 * method.
 *
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add can only be
 * private, protected or package protected - in other words, no new public
 * methods
 */
public class VersionMonitor {

	private AtomicInteger version;
	
	public VersionMonitor() {
		this.version = new AtomicInteger(0);
	}

	/**
	 * <h1>getVersion</h1>
	 * <p>return the current version of the monitor</p>
	 * @return {@link #version}
	 */
    public int getVersion() {
        return this.version.get();
    }

	/**
	 *<h1>inc</h1>
	 *<p>increases the version number by 1, and notify all the blocked threads to wake up<br>
	 *<h2>sync exp:</h2>
	 *in order to use {@link #notifyAll()} one must be synchronized on the object</p>
	 *@throws IllegalMonitorStateException
	 */
    public void inc() throws IllegalMonitorStateException {
    	int v = this.version.get();
    	this.version.compareAndSet(v,v+1);
    	synchronized (this) {
    		this.notifyAll();
    	}
    }

	/**
	 *<h1>await</h1>
	 *<p>blocking the thread that is using to monitor till the version changes<br>
	 *<h2>sync exp:</h2>
	 * we will sync on this, i.e. the VersionMonitor instance<br>
	 * in order to block the thread</p>
	 * @param version
	 * @Inv some other thread will notify this thread to wake up using the {@link #inc()} method
	 * @throws InterruptedException
	 */
    synchronized public void await(int version) throws InterruptedException {
        while(this.getVersion()==version) {
        	try {
        		this.wait();
        	}catch (InterruptedException e) {
				// TODO: handle exception
			}
        }
    }
}

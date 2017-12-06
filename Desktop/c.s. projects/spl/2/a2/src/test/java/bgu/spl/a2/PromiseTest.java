package bgu.spl.a2;

import static org.junit.Assert.*;
//import java.util.concurrent.CountDownLatch;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import bgu.spl.a2.Promise;

public class PromiseTest {
	/**
	 * we are going to test all the methods of Promise
	 */
	@Before
	public void setUp() throws Exception {
	}
	@After
	public void tearDown() throws Exception {
	}
	/**
	 * checking if method get of Promise returns a proper value<br>
	 * (get is atomic method)
	 */
	@Test
	public void testGet() {
		Promise<Integer> x = new Promise<>();
		try {
			try {
				x.get();
				fail();
			}catch(Exception e) {}
			x.resolve(5);
			Integer value =new Integer( x.get());
			assertTrue(value != null && value==5);
		}
		catch(Exception e){
			fail();
		}
	}
	/**
	 * checking if method isResolved of Promise returns a proper value<br>
	 * (isResolved is atomic method)
	 */
	@Test
	public void testIsResolved() {
		Promise<Integer> x = new Promise<>();
		try {
			if(x.isResolved())
				fail();
			x.resolve(5);
			assertTrue(x.isResolved());
		}
		catch(Exception e){
			fail();
		}
	}
	/**
	 * checking if method Resolve of Promise does the flowing things properly:<br>
	 * 1.sets the result value to the new value<br>
	 * 2.triggers all the subscribed call backs
	 */
	@Test
	public void testResolve() throws InterruptedException {
		Promise<Integer> x = new Promise<>();
		final boolean[] passed = {false};
		try {
			x.subscribe(()->passed[0] = true);
			x.resolve(5);
			try{
				x.resolve(6);
				fail();
			}
			catch(IllegalStateException e){
				Integer value =new Integer( x.get());
				assertTrue(value != null && value==5 && passed[0]==true);
			}
			catch(Exception e){
				fail();
			}
		}
		catch(Exception e){
			fail();
		}
	}
	/**
	 *checking if method subscribe of Promise adds a callback to be called when the object is resolved<br>
	 *(isResolved is atomic method)
	 */
	@Test
	public void testSubscribe() {
		Promise<Integer> x = new Promise<>();
		final boolean[] passed = {false};
		try {
			x.subscribe(()->passed[0] = true);
			x.resolve(5);
			assertTrue(passed[0]);
		}
		catch(Exception e){
			fail();
		}
	}
}
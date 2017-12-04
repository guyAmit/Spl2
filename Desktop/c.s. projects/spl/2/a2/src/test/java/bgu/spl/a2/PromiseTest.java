package bgu.spl.a2;

import static org.junit.Assert.*;

import java.util.concurrent.CountDownLatch;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import bgu.spl.a2.Promise;

public class PromiseTest {
	
	Promise<Integer> x;

	@Before
	public void setUp() throws Exception {
		x=new Promise<>();
	}

	@After
	public void tearDown() throws Exception {
	}

	
	@Test
	public void testGet() {
		try{
			Thread t = new Thread(new Runnable() {
				
				@Override
				public void run() {
					//x.resolve(5);
					try {
						x.resolve(5);
					}
					catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						fail("Get is not working");
					}
					catch(Exception e){
						fail("Get is not working");
					}
				}				
			});
			
			t.start();
			t.join();
			assertEquals(5, (int)x.get());
		}
		catch (Exception e) {
			fail("Get Method Failed");
		}
		
	}

	@Test
	public void testIsResolved() {
		try{
			Thread t = new Thread(new Runnable() {
				
				@Override
				public void run() {
					//x.resolve(5);
					try {
						x.resolve(5);
					}
					catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						fail("IsResolved is not working");
					}
					catch(Exception e){
						fail("IsResolved is not working");
					}
				}				
			});
			
			t.start();
			t.join();
			assertTrue(x.isResolved());
		}
		catch (Exception e) {
			fail("IsResolved Method Failed");
		}
	}

	@Test
	public void testResolve() throws InterruptedException {
		try{
			Thread t = new Thread(new Runnable() {			
				@Override
				public void run() {
					//x.resolve(5);
					try {
						x.resolve(5);
						try{
							x.resolve(6);
							fail("Resolve is not working");
						}
						catch(IllegalStateException e){
							int value = x.get();
							assertEquals(5, value);
						}
						catch(Exception e){
							fail();
						}
					}
					catch(Exception e){
						fail("IsResolved is not working");
					}
				}				
			});
			t.start();
			t.join();
		}
		catch (Exception e) {
			fail("IsResolved Method Failed");
		}		
	}
	
	

	@Test
	public void testSubscribe() {
		fail("Not yet implemented");
	}

}

package bgu.spl.a2;

import static org.junit.Assert.*;
import java.lang.Thread.State;

//import java.time.chrono.ThaiBuddhistEra;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import bgu.spl.a2.VersionMonitor;

public class VersionMonitorTest {

	//the object under test
	VersionMonitor out;
	
	/**
	 * <h1> setup method</h1>
	 * <p> creating a new monitor for each test </p>
	 * @return a new monitor 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		try {
			out = new VersionMonitor();
		}catch (Exception e) {
			fail("creation of the vertion monitor failed");
		}
			
	}
	

	/**
	 * <h1> tearDown method</h1>
	 * <p> Destroy the monitor under test-for garbage collection</p> 
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
		try {
			out=null;
		}catch(Exception e) {
			
		}
	}

	/**
	 * <h1>testGetVersion</h1>
	 * <p> test whether the version number has been retrieved </p>
	 * @Pre the monitor version is 0 
	 * @Inv the monitor version should stay 0
	 * @throws Exception
	 */
	@Test
	public void testGetVersion() {
		try {
			int v=out.getVersion();
			assertEquals(0, v);
		}
		catch(Exception e){
			fail("geting vertion failed");
		}
	}

	/**
	 * <h1>TestInc</h1>
	 * <p> testing that the version number indeed changes when using {@link Inc()}</p>
	 * @param out.version is the current monitor version 
	 * @post monitor version should be version+1
	 * @throws Exception
	 */
	@Test
	public void testInc() {
		try {
			int v = out.getVersion();
			out.inc();
			assertEquals(v+1, out.getVersion());
			
		}catch(IllegalMonitorStateException e) {
			fail("notify all throws an exeption");
		}
	}
	
	
	/**
	 * <h1>Test await</h1>
	 * <p>testing the ability of the {@link Await()} method to freeze a thread <br>,
	 * the test will create a thread that will run {@link Await()}, we expect <br>
	 * the thread to get into waiting state-we will check that. after words we <br>
	 * will use {@link Inc()}, we expect that the thread will get out of waiting mode<br>
	 * -and we will check that too </p>
	 * @Pre t should be at running state
	 * @Inv t should get out of running state <=> {@link Await()} is called
	 * @throws InterruptedException
	 */
	@Test
	public void testAwait() throws InterruptedException {
		int version = out.getVersion();
		final boolean[] passed = {false,false};
		Thread t= new Thread(()->{
			try {
				passed[0]=true;
				out.await(version);
				passed[1]=true;
			} catch (InterruptedException e) {
				fail();
			}
		});
		t.start();
		try {Thread.sleep(1000);}catch(InterruptedException e) {};
		boolean sleeping = t.getState() == State.WAITING;
		out.inc(); //should bring t out of waiting state
		t.join();
		assertEquals(passed[0]&passed[1]&sleeping, true);
	}
}
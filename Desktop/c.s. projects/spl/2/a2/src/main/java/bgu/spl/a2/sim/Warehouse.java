package bgu.spl.a2.sim;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import bgu.spl.a2.Promise;
import bgu.spl.a2.callback;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

/**
 * represents a warehouse that holds a finite amount of computers
 *  and their suspended mutexes.
 * 
 */
public class Warehouse {

	private ConcurrentHashMap<String, SuspendingMutex> computers; //<computer name,the computer mutex>
	/**
	 * <h1>WareHouse constructor</h1>
	 * create a data structure that hold all the suspending mutexes of the computers
	 * @param array list of computers
	 */
	public Warehouse(ArrayList<Computer> computers) {
		for (Computer computer : computers) {
			SuspendingMutex mutex = new SuspendingMutex(computer);
			this.computers.put(computer.getComputerType(), mutex);
		}
	}
	
	/**
	 * <h1>acquireComputer</h1>
	 * try to get the desired computer.
	 * @param computer's name
	 * @return the desired computer or null if not avilable
	 */
	public Promise<Computer> acquireComputer(String computer) {
		SuspendingMutex computerMutex = this.computers.get(computer);
		Promise<Computer> promise = computerMutex.down();
		return promise;
	}
	
	public void freeComputer(String computer) {
		SuspendingMutex computerMutex = this.computers.get(computer);
		computerMutex.up();
	}

}

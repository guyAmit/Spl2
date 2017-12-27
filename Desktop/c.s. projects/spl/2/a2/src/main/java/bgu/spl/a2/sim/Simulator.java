package bgu.spl.a2.sim;
import java.util.HashMap;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import javax.management.monitor.Monitor;
import javax.rmi.CORBA.Util;

import bgu.spl.a2.Action;
import bgu.spl.a2.ActorThreadPool;
import bgu.spl.a2.PrivateState;
import bgu.spl.a2.sim.Actions.Add_Student;
import bgu.spl.a2.sim.Actions.Open_A_New_Course;
import bgu.spl.a2.sim.Actions.Participate_In_Course;
import bgu.spl.a2.sim.Actions.Register_With_Preferences;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;
import bgu.spl.a2.sim.ActionsCreator;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


/**
 * A class describing the simulator for part 2 of the assignment
 */
public class Simulator {

	//phases names
	private static final String Phase1="Phase 1";
	private static final String Phase2="Phase 2";
	private static final String Phase3="Phase 3";
	private static final Long Sleep = (long) 500;
	public static ActorThreadPool actorThreadPool;
	public static Warehouse wareHouse;
	public static Object jObj;
	public static CountDownLatch Actioncounter;
	
	/**
	* Begin the simulation Should not be called before attachActorThreadPool()
	 * @throws InterruptedException 
	*/
    public static void start() throws InterruptedException{
			initPhaseActions((JSONObject)jObj,Phase1);
			actorThreadPool.start();
			try {
				Actioncounter.await();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			initPhaseActions((JSONObject)jObj,Phase2);
			ActorThreadPool.monitor.inc();
			try {
				Actioncounter.await();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			initPhaseActions((JSONObject)jObj,Phase3);
			ActorThreadPool.monitor.inc();
			try {
				Actioncounter.await();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		end();
    }
	
	/**
	* attach an ActorThreadPool to the Simulator, this ActorThreadPool will be used to run the simulation
	* 
	* @param myActorThreadPool - the ActorThreadPool which will be used by the simulator
	*/
	public static void attachActorThreadPool(ActorThreadPool myActorThreadPool){
		//TODO: replace method body with real implementation
		actorThreadPool = myActorThreadPool;
	}
	
	/**
	* shut down the simulation
	* returns list of private states
	*/
	public static HashMap<String,PrivateState> end(){
		try {
			actorThreadPool.shutdown();
		} catch (InterruptedException e) {
			Thread thisThread = Thread.currentThread();
			thisThread.interrupt();
		}
		try {
			HashMap<String, PrivateState> returnMap = new HashMap<>();
			for(Map.Entry<String, PrivateState> entry : actorThreadPool.getActors().entrySet())
				returnMap.put(entry.getKey(), entry.getValue());
			FileOutputStream outStram = new FileOutputStream("result.ser");
			ObjectOutputStream oos = new ObjectOutputStream(outStram);
			oos.writeObject(returnMap);
			oos.close();
			outStram.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * <h1>initThreadPool</h1>
	 * read from the json file the number of threads and create a new<br>
	 * ActorThreadPooll and return it.
	 * @param JSONObject 
	 * @return {@link #actorThreadPool}
	 */
	public static ActorThreadPool initThreadPool(JSONObject obj) {
		String nThreads = obj.get("threads").toString();
		ActorThreadPool pool = new ActorThreadPool(Integer.parseInt(nThreads));
		return pool;		
	}
	
	/**
	 * <h1>initComputers</h1>
	 * read from the json file all the computers and return<br>
	 * an array list of all of those computers
	 * @param JSONArray
	 * @return array list of computers
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<Computer> initComputers(JSONObject obj) {
		JSONArray CompSjonArray = (JSONArray)obj.get("Computers");
		ArrayList<Computer> computers= new ArrayList<>();
		CompSjonArray.forEach(entry -> {
			String type = ((JSONObject)entry).get("Type").toString();
			int Sig_Success=Integer.parseInt(((JSONObject)entry).get("Sig Success").toString());
			int Sig_Fail=Integer.parseInt(((JSONObject)entry).get("Sig Fail").toString());
			//TODO: create a constructor for computer that also recievse the signatures
			Computer c  = new Computer(type);
			c.setFailSig(Sig_Fail);
			c.setSuccessSig(Sig_Success);
			computers.add(c);
		});
		return computers;
	}
	
	/**
	 * <h1>initPhase1Actions</h1>
	 * read from the json file all the Phase1 actions and return<br>
	 * an array list of all of those actions
	 * @param JSONArray
	 * @return array list of actions
	 */
	@SuppressWarnings("unchecked")
	public static void initPhaseActions(JSONObject obj,String phase) {
		JSONArray PhaseActionsArray = (JSONArray)obj.get(phase);
		Actioncounter = new CountDownLatch(PhaseActionsArray.size());
		PhaseActionsArray.forEach(entry -> {
			String type = ((JSONObject)entry).get("Action").toString();
			if(type.compareTo("Add Student")==0) {//#done
				ActionsCreator.createAddStudentAction((JSONObject)entry, actorThreadPool);
			}
			else if(type.compareTo("Open Course")==0) {//#done
				ActionsCreator.createOpenCourseAction((JSONObject)entry, actorThreadPool);
			}
			else if(type.compareTo("Participate In Course")==0) {//#done
				ActionsCreator.createParticipateInCourseAction((JSONObject)entry, actorThreadPool);
			}
			else if(type.compareTo("Add Spaces")==0) {//#done
				ActionsCreator.createAddSpacesAction((JSONObject)entry, actorThreadPool);
			}
			else if(type.compareTo("Register With Preferences")==0) {//#done
				ActionsCreator.createRegisterWithPreferncesAction((JSONObject)entry, actorThreadPool);
			}
			else if(type.compareTo("Unregister")==0) {//#done
				ActionsCreator.createUnregisterAction((JSONObject)entry, actorThreadPool);
			}
			else if(type.compareTo("Close Course")==0) {//#done
				ActionsCreator.createCloseCourseAction((JSONObject)entry, actorThreadPool);
			}
			else if(type.compareTo("Administrative Check")==0) {//#done
				ActionsCreator.createCheckAdministrativeObligations((JSONObject)entry, actorThreadPool);
			}
		});
	}
	
		
	public static  void main(String [] args) {
		//TODO: replace method body with real implementation
		ArrayList<Computer> computers;
		JSONParser parser = new JSONParser();
		try(FileReader reader = new FileReader(args[0])){
			jObj = parser.parse(reader);
			computers = initComputers((JSONObject)jObj);
			attachActorThreadPool(initThreadPool((JSONObject)jObj));
			wareHouse = new Warehouse(computers);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch(ParseException e) {
        	e.printStackTrace();
        }
		try {
			start();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
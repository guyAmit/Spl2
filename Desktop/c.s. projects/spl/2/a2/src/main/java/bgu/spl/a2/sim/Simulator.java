/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bgu.spl.a2.sim;
import java.util.HashMap;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import bgu.spl.a2.Action;
import bgu.spl.a2.ActorThreadPool;
import bgu.spl.a2.PrivateState;
import bgu.spl.a2.sim.Actions.Add_Student;
import bgu.spl.a2.sim.Actions.Open_A_New_Course;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;
import bgu.spl.a2.sim.ActionsCreator;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


/**
 * A class describing the simulator for part 2 of the assignment
 */
public class Simulator {

	
	public static ActorThreadPool actorThreadPool;
	public static Warehouse wareHouse;
	public static CountDownLatch phaseActions;
	
	/**
	* Begin the simulation Should not be called before attachActorThreadPool()
	*/
    public static void start(){
		actorThreadPool.start();
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			FileOutputStream outStram = new FileOutputStream("result.ser");
			ObjectOutputStream oos = new ObjectOutputStream(outStram);
			oos.writeObject(actorThreadPool.getActors());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (HashMap<String, PrivateState>) actorThreadPool.getActors();
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
	public static void initPhase1Actions(JSONObject obj) {
		JSONArray Phase1ActionsArray = (JSONArray)obj.get("Phase 1");
		phaseActions = new CountDownLatch(Phase1ActionsArray.size());
		Phase1ActionsArray.forEach(entry -> {
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
			else if(type.compareTo("Administrative Check")==0) {
				ActionsCreator.createAddStudentAction((JSONObject)entry, actorThreadPool);
			}
		});
	}
	
	
	/**
	 * <h1>initPhase2Actions</h1>
	 * read from the json file all the Phase2 actions and return<br>
	 * an array list of all of those actions
	 * @param JSONArray
	 */
	@SuppressWarnings("unchecked")
	public static void initPhase2Actions(JSONObject obj) {
		JSONArray Phase2ActionsArray = (JSONArray)obj.get("Phase 2");
		phaseActions = new CountDownLatch(Phase2ActionsArray.size());
		Phase2ActionsArray.forEach(entry -> {
			String type = ((JSONObject)entry).get("Action").toString();
			if(type.compareTo("Add Student")==0) {//#done
				ActionsCreator.createAddStudentAction((JSONObject)entry, actorThreadPool);
			}
			if(type.compareTo("Open Course")==0) {//#done
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
			else if(type.compareTo("Administrative Check")==0) {
				ActionsCreator.createAddStudentAction((JSONObject)entry, actorThreadPool);
			}
		
		});
		
	}
	
	
	public static  void main(String [] args) {
		//TODO: replace method body with real implementation
		ConcurrentHashMap<String, Computer> computers;
		//TODO: create the ware house
		JSONParser parser = new JSONParser();
		Object jObj= new Object();
		try(FileReader reader = new FileReader(args[0])){
			jObj = parser.parse(reader);
			computers = initComputers((JSONObject)jObj);
			attachActorThreadPool(initThreadPool((JSONObject)jObj));
			initPhase1Actions((JSONObject)jObj); //phase1 parsing is working
			start();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch(ParseException e) {
        	e.printStackTrace();
        }
//		Thread simulator = new Thread(()->{
//			try {
//				phaseActions.await();
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			finally {
//				try {
//					phaseActions.await();
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//	});
//	simulator.start();
//	try {
//		simulator.join();
//	} catch (InterruptedException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
	}
}

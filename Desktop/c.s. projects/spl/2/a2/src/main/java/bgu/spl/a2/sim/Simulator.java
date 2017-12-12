/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.spl.a2.sim;
<<<<<<< HEAD
import java.util.HashMap;
=======
>>>>>>> guy
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import bgu.spl.a2.Action;
import bgu.spl.a2.ActorThreadPool;
import bgu.spl.a2.PrivateState;
import bgu.spl.a2.sim.Actions.Open_A_New_Course;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.internal.matchers.CombinableMatcher;


/**
 * A class describing the simulator for part 2 of the assignment
 */
public class Simulator {

	
	public static ActorThreadPool actorThreadPool;
	
	/**
	* Begin the simulation Should not be called before attachActorThreadPool()
	*/
    public static void start(){
		//TODO: replace method body with real implementation
		throw new UnsupportedOperationException("Not Implemented Yet.");
    }
	
	/**
	* attach an ActorThreadPool to the Simulator, this ActorThreadPool will be used to run the simulation
	* 
	* @param myActorThreadPool - the ActorThreadPool which will be used by the simulator
	*/
	public static void attachActorThreadPool(ActorThreadPool myActorThreadPool){
		//TODO: replace method body with real implementation
		throw new UnsupportedOperationException("Not Implemented Yet.");
	}
	
	/**
	* shut down the simulation
	* returns list of private states
	*/
	public static HashMap<String,PrivateState> end(){
		//TODO: replace method body with real implementation
		throw new UnsupportedOperationException("Not Implemented Yet.");
	}
	
	
	/**
	 * <h1>initThreadPool</h1>
	 * read from the json file the number of threads and create a new<br>
	 * ActorThreadPooll and return it.
	 * @param JSONObject 
	 * @return {@link #actorThreadPool}
	 */
	public static ActorThreadPool initThreadPool(JSONObject obj) {
		int nThreads = (Integer)obj.get("threads");
		ActorThreadPool pool = new ActorThreadPool(nThreads);
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
			int Sig_Success=(int)((JSONObject)entry).get("Sig Success");
			int Sig_Fail=(int)((JSONObject)entry).get("Sig Fail");
			//TODO: create a constructor for computer that also recievse the signatures
			Computer c  = new Computer(type);
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
	public static ArrayList<Open_A_New_Course<Boolean>> initPhase1Actions(JSONObject obj) {
		JSONArray Phase1ActionsArray = (JSONArray)obj.get("Phase 1");
		ArrayList<Open_A_New_Course<Boolean>> actions= new ArrayList<>();
		Phase1ActionsArray.forEach(entry -> {
			String Department = ((JSONObject)entry).get("Department").toString();
			String Course = ((JSONObject)entry).get("Course").toString();
			int spaces = (int)((JSONObject)entry).get("space");
			JSONArray PrerequisitesJson = (JSONArray)(((JSONObject)entry)).get("Prerequisites");
			ArrayList<String> Prerequisites  = new ArrayList<>();
			PrerequisitesJson.forEach(pre ->{Prerequisites.add((String)pre);});
			Open_A_New_Course open;
			//actions.add(open);
		});
		return actions;
	}
	
	
	/**
	 * <h1>initPhase2Actions</h1>
	 * read from the json file all the Phase2 actions and return<br>
	 * an array list of all of those actions
	 * @param JSONArray
	 * @return array list of actions
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<Action> initPhase2Actions(JSONObject obj) {
		JSONArray Phase1ActionsArray = (JSONArray)obj.get("Phase 2");
		ArrayList<Action> actions= new ArrayList<>();
		Phase1ActionsArray.forEach(entry -> {
			String type = ((JSONObject)entry).get("Action").toString();
			//TODO: find out what kind of actions are there in phase1
			Action c=null;
			actions.add(c);
		});
		return actions;
	}
	
	
	public static  void main(String [] args) throws ParseException{
		//TODO: replace method body with real implementation
		ArrayList<Computer> computers;
		ArrayList<Action> Phase1Actions;
		ArrayList<Action> Phase2Actions;
		JSONParser parser = new JSONParser();
		try(FileReader reader = new FileReader(args[0])){
			Object jObj = parser.parse(reader);
			actorThreadPool = initThreadPool((JSONObject)jObj);
			computers = initComputers((JSONObject)jObj);
			
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch(ParseException e) {
        	
        }
		
		
	}
}

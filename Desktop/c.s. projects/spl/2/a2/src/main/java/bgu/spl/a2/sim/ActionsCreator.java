package bgu.spl.a2.sim;

import bgu.spl.a2.Action;
import bgu.spl.a2.ActorThreadPool;
import bgu.spl.a2.PrivateState;
import bgu.spl.a2.sim.Actions.Add_Student;
import bgu.spl.a2.sim.Actions.Check_Administrative_Obligations;
import bgu.spl.a2.sim.Actions.Close__Course;
import bgu.spl.a2.sim.Actions.Open_A_New_Course;
import bgu.spl.a2.sim.Actions.Open_new_spots;
import bgu.spl.a2.sim.Actions.Participate_In_Course;
import bgu.spl.a2.sim.Actions.Register_With_Preferences;
import bgu.spl.a2.sim.Actions.Unregister;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

import java.util.ArrayList;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ActionsCreator {

	public static void createAddStudentAction(JSONObject jsonEntry,ActorThreadPool actorThreadPool){
		String studentId=jsonEntry.get("Student").toString();
		String departmentId=jsonEntry.get("Department").toString();
		Add_Student addStudentAction = new Add_Student(studentId);
		addStudentAction.getResult().subscribe(()->{Simulator.Actioncounter.countDown();});
		DepartmentPrivateState departmentPrivateState = (DepartmentPrivateState)actorThreadPool.getActors().get(departmentId);
		if(departmentPrivateState==null)
		{departmentPrivateState = new DepartmentPrivateState();
			actorThreadPool.getActors().put(departmentId, departmentPrivateState);
		}		
		actorThreadPool.submit(addStudentAction, departmentId, departmentPrivateState);
	}
	
	public static void createOpenCourseAction(JSONObject jsonEntry,ActorThreadPool actorThreadPool) {
		String courseId=jsonEntry.get("Course").toString();
		String departmentId=jsonEntry.get("Department").toString();
		int spaces = Integer.parseInt(jsonEntry.get("Space").toString());
		JSONArray PrerequisitesJson = (JSONArray)(jsonEntry).get("Prerequisites");
		ArrayList<String> prequisites  = new ArrayList<>();
		PrerequisitesJson.forEach(pre ->{prequisites.add((String)pre);});
		DepartmentPrivateState departmentPrivateState = (DepartmentPrivateState)actorThreadPool.getPrivaetState(departmentId);
		if(departmentPrivateState==null)
		{departmentPrivateState = new DepartmentPrivateState();
			actorThreadPool.getActors().put(departmentId, departmentPrivateState);
		}
		Open_A_New_Course openCourseAction = new Open_A_New_Course(courseId, spaces, prequisites);
		openCourseAction.getResult().subscribe(()->{Simulator.Actioncounter.countDown();});
		actorThreadPool.submit(openCourseAction, departmentId,departmentPrivateState);
	}
	
	public static void createParticipateInCourseAction(JSONObject jsonEntry,ActorThreadPool actorThreadPool) {
		Participate_In_Course participateInCourseAction;
		String studentId =jsonEntry.get("Student").toString();
		String courseId = jsonEntry.get("Course").toString();
		CoursePrivateState coursePrivateState =(CoursePrivateState)actorThreadPool.getPrivaetState(courseId);
		int grade;
		if(jsonEntry.get("Grade")!=null) {
			JSONArray gradeJson = (JSONArray)(jsonEntry).get("Grade");
			ArrayList<Integer> gradeArray =new ArrayList<>();
			gradeJson.forEach(entry->{
				if(entry.toString().compareTo("-")==0) 
					gradeArray.add(-1);
				else gradeArray.add(Integer.parseInt(entry.toString()));
				});
			grade=gradeArray.get(0);
			participateInCourseAction = new Participate_In_Course(studentId, grade);
		}
		else{
			participateInCourseAction = new Participate_In_Course(studentId);
		}
		participateInCourseAction.getResult().subscribe(()->{Simulator.Actioncounter.countDown();});
		actorThreadPool.submit(participateInCourseAction, courseId, coursePrivateState);
	}
	
	public static void createRegisterWithPreferncesAction(JSONObject jsonEntry,ActorThreadPool actorThreadPool) {
		String studentId=jsonEntry.get("Student").toString();
		StudentPrivateState studentPrivateState = (StudentPrivateState)actorThreadPool.getPrivaetState(studentId);
		JSONArray preferencesJson = (JSONArray)(jsonEntry).get("Preferences");
		ArrayList<String> preferences  = new ArrayList<>();
		preferencesJson.forEach(pre ->{preferences.add((String)pre);});
		JSONArray gradesJson = (JSONArray)(jsonEntry).get("Grade");
		Register_With_Preferences registerWithPrefernceAction;
		if(gradesJson!=null) {
			ArrayList<Integer> grades = new ArrayList<>();
			gradesJson.forEach(entry->{
				if(entry.toString().compareTo("-")==0)
					grades.add(-1);
				else grades.add(Integer.parseInt(entry.toString()));
			});
			registerWithPrefernceAction = new Register_With_Preferences(preferences,grades);
		}
		else{
			registerWithPrefernceAction = new Register_With_Preferences(preferences);
		}
		registerWithPrefernceAction.getResult().subscribe(()->{Simulator.Actioncounter.countDown();});
		actorThreadPool.submit(registerWithPrefernceAction, studentId, studentPrivateState);
	}
	
	public static void createUnregisterAction(JSONObject jsonEntry,ActorThreadPool actorThreadPool) {
		String studentId =jsonEntry.get("Student").toString();
		String courseId = jsonEntry.get("Course").toString();
		CoursePrivateState coursePrivateState =(CoursePrivateState)actorThreadPool.getPrivaetState(courseId);
		Unregister unRegisterAction= new Unregister(studentId);
		unRegisterAction.getResult().subscribe(()->{Simulator.Actioncounter.countDown();});
		actorThreadPool.submit(unRegisterAction, courseId, coursePrivateState);
	}
	
	public static void createCloseCourseAction(JSONObject jsonEntry, ActorThreadPool actorThreadPool) {
		String courseId= jsonEntry.get("Course").toString();
		String departmentId=jsonEntry.get("Department").toString();
		Close__Course closeCourseAction = new Close__Course(courseId);
		closeCourseAction.getResult().subscribe(()->{Simulator.Actioncounter.countDown();});
		DepartmentPrivateState departmentPrivateState = (DepartmentPrivateState)actorThreadPool.getPrivaetState(departmentId);
		actorThreadPool.submit(closeCourseAction, departmentId, departmentPrivateState);
	}
	
	public static void createAddSpacesAction(JSONObject jsonEntry,ActorThreadPool actorThreadPool) {
		String courseId = jsonEntry.get("Course").toString();
		CoursePrivateState coursePrivateState =(CoursePrivateState)actorThreadPool.getPrivaetState(courseId);
		Integer spaces = Integer.parseInt(jsonEntry.get("Number").toString());
		Open_new_spots openNewSpotsAction = new Open_new_spots(courseId, spaces);
		openNewSpotsAction.getResult().subscribe(()->{Simulator.Actioncounter.countDown();});
		actorThreadPool.submit(openNewSpotsAction, courseId, coursePrivateState);
	}
	
	
	public static void createCheckAdministrativeObligations(JSONObject jsonEntry, ActorThreadPool actorThreadPool) {
		String departmentId=jsonEntry.get("Department").toString();
		JSONArray studentsJson = (JSONArray)(jsonEntry).get("Students");
		ArrayList<String> studentsIds  = new ArrayList<>();
		studentsJson.forEach(course ->{studentsIds.add((String)course);});
		String computerId=jsonEntry.get("Computer").toString();
		JSONArray coursesJson = (JSONArray)(jsonEntry).get("Conditions");
		ArrayList<String> coursesIds  = new ArrayList<>();
		coursesJson.forEach(course ->{coursesIds.add((String)course);});
		Check_Administrative_Obligations checkAdministrativeObligationsAction = new Check_Administrative_Obligations(coursesIds, studentsIds, computerId);
		checkAdministrativeObligationsAction.getResult().subscribe(()->{Simulator.Actioncounter.countDown();});
		DepartmentPrivateState departmentPrivateState = (DepartmentPrivateState)actorThreadPool.getPrivaetState(departmentId);
		actorThreadPool.submit(checkAdministrativeObligationsAction, departmentId, departmentPrivateState);
	}
}

package bgu.spl.a2.sim;

import bgu.spl.a2.Action;
import bgu.spl.a2.ActorThreadPool;
import bgu.spl.a2.PrivateState;
import bgu.spl.a2.sim.Actions.Add_Student;
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
		DepartmentPrivateState departmentPrivateState = (DepartmentPrivateState)actorThreadPool.getActors().get(departmentId);
		actorThreadPool.submit(addStudentAction, departmentId, departmentPrivateState);
	}
	
	public static void createOpenCourseAction(JSONObject jsonEntry,ActorThreadPool actorThreadPool) {
		String courseId=jsonEntry.get("Course").toString();
		String departmentId=jsonEntry.get("Department").toString();
		int spaces = (int)jsonEntry.get("space");
		JSONArray PrerequisitesJson = (JSONArray)(jsonEntry).get("Prerequisites");
		ArrayList<String> prequisites  = new ArrayList<>();
		PrerequisitesJson.forEach(pre ->{prequisites.add((String)pre);});
		DepartmentPrivateState departmentPrivateState = (DepartmentPrivateState)actorThreadPool.getPrivaetState(departmentId);
		Open_A_New_Course openCourseAction = new Open_A_New_Course(courseId, spaces, prequisites);
		actorThreadPool.submit(openCourseAction, departmentId, departmentPrivateState);
	}
	
	public static void createParticipateInCourseAction(JSONObject jsonEntry,ActorThreadPool actorThreadPool) {
		Participate_In_Course participateInCourseAction;
		String studentId =jsonEntry.get("Student").toString();
		String courseId = jsonEntry.get("Course").toString();
		CoursePrivateState coursePrivateState =(CoursePrivateState)actorThreadPool.getPrivaetState(courseId);
		int grade;
		if(jsonEntry.get("grade")!=null) {
			grade=(int)jsonEntry.get("Grade");
			participateInCourseAction = new Participate_In_Course(studentId, grade);
		}
		else{
			participateInCourseAction = new Participate_In_Course(studentId);
		}
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
			gradesJson.forEach(entry->{grades.add((Integer)entry);});
			registerWithPrefernceAction = new Register_With_Preferences(preferences,grades);
		}
		else{
			registerWithPrefernceAction = new Register_With_Preferences(preferences);
		}
		actorThreadPool.submit(registerWithPrefernceAction, studentId, studentPrivateState);
	}
	
	public static void createUnregisterAction(JSONObject jsonEntry,ActorThreadPool actorThreadPool) {
		String studentId =jsonEntry.get("Student").toString();
		String courseId = jsonEntry.get("Course").toString();
		CoursePrivateState coursePrivateState =(CoursePrivateState)actorThreadPool.getPrivaetState(courseId);
		Unregister unRegisterAction= new Unregister(studentId);
		actorThreadPool.submit(unRegisterAction, courseId, coursePrivateState);
	}
	
	public static void createCloseCourseAction(JSONObject jsonEntry, ActorThreadPool actorThreadPool) {
		String courseId= jsonEntry.get("Course").toString();
		String departmentId=jsonEntry.get("Department").toString();
		Close__Course closeCourseAction = new Close__Course(courseId);
		DepartmentPrivateState departmentPrivateState = (DepartmentPrivateState)actorThreadPool.getPrivaetState(departmentId);
		actorThreadPool.submit(closeCourseAction, departmentId, departmentPrivateState);
	}
	
	public static void createAddSpacesAction(JSONObject jsonEntry,ActorThreadPool actorThreadPool) {
		String courseId = jsonEntry.get("Course").toString();
		CoursePrivateState coursePrivateState =(CoursePrivateState)actorThreadPool.getPrivaetState(courseId);
		Integer spaces = (Integer)jsonEntry.get("Number");
		Open_new_spots openNewSpotsAction = new Open_new_spots(courseId, spaces);
		actorThreadPool.submit(openNewSpotsAction, courseId, coursePrivateState);
	}
}

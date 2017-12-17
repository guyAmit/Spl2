package bgu.spl.a2.sim;

import bgu.spl.a2.Action;
import bgu.spl.a2.ActorThreadPool;
import bgu.spl.a2.PrivateState;
import bgu.spl.a2.sim.Actions.Add_Student;
import bgu.spl.a2.sim.Actions.Open_A_New_Course;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;

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
		DepartmentPrivateState departmentPrivateState = (DepartmentPrivateState)actorThreadPool.getActors().get(departmentId);
		Open_A_New_Course openCourseAction = new Open_A_New_Course(courseId, spaces, prequisites);
		actorThreadPool.submit(openCourseAction, departmentId, departmentPrivateState);
	}
}

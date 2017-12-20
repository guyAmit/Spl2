package bgu.spl.a2.sim.Actions;

import java.util.ArrayList;
import java.util.List;
import bgu.spl.a2.Action;

public class Register_With_Preferences<R> extends Action<R> {

	private List<String> preferedCourses = new ArrayList<>();
	private List<Integer> gradesOfPreferedCourses = new ArrayList<>();
	private String studentName ="";
	private List<Action<?>> actions = new ArrayList<Action<?>>();
	
	@SuppressWarnings("unchecked")
	@Override
	protected void start() {
		// TODO Auto-generated method stub
		setActionName("Register With Preferences");

		actions.add(new Action() {
			private String courseName;
			@Override
			protected void start() {
				// TODO Auto-generated method stub
				//no need to check if grade at list 56
				//for(String course : preferedCourses){
				for(int i=0; i<preferedCourses.size(); i++) {
					Participate_In_Course<R> participate = new Participate_In_Course<>();
					participate.NameToBeRegistered(studentName);
					participate.giveGrade(gradesOfPreferedCourses.get(i));
					courseName = preferedCourses.get(i);
					actions.add(participate);
					then(actions,()->{
						//actorId -> course, actorState -> student
						pool.submit(this, courseName , pool.getActors().get(courseName));
					});
					try {
						version.await(version.getVersion());
						if(participate.IsOkToRegister()) 
							break;
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});

		then(actions,()->{
			//actorId -> course, actorState -> student
			pool.submit(this, actorId, actorState);
		});
		complete((R) new Object());
	}
	public void NameToBeRegistered(String studentName) {
		this.studentName = studentName;
	}
}

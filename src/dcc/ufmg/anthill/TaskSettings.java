package dcc.ufmg.anthill;
/**
 * @authors: Rodrigo Caetano O. ROCHA
 * @date: 26 July 2013
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import dcc.ufmg.anthill.*;
import dcc.ufmg.anthill.util.*;
import dcc.ufmg.anthill.net.*;
import dcc.ufmg.anthill.info.*;
import dcc.ufmg.anthill.scheduler.*;
import dcc.ufmg.anthill.stream.*;

public class TaskSettings {
	private static ArrayList<TaskInfo> tasks = new ArrayList<TaskInfo>();
	
	public static void addTaskInfo(TaskInfo taskInfo){
		tasks.add(taskInfo);
	}

	public static void removeTaskInfo(TaskInfo taskInfo){
		for(int i = 0; i<tasks.size(); i++){
			TaskInfo task = tasks.get(i);
			if(task.getHostName().equals(taskInfo.getHostName()) && task.getModuleInfo().getName().equals(taskInfo.getModuleInfo().getName()) && task.getTaskId()==taskInfo.getTaskId()){
				tasks.remove(i);
				break;
			}
		}
	}

	public static ArrayList<TaskInfo> getTaskInfo(){
		return tasks;
	}
}

/**
 * @author Rodrigo Caetano O. ROCHA
 * @date 24 July 2013
 */

import java.io.IOException;

import dcc.ufmg.anthill.*;
import dcc.ufmg.anthill.util.*;
import dcc.ufmg.anthill.net.*;
import dcc.ufmg.anthill.info.*;
import dcc.ufmg.anthill.scheduler.*;
import dcc.ufmg.anthill.stream.*;
import dcc.ufmg.anthill.stream.hdfs.*;

public class TestFilter extends Filter<String, String>{
	int taskId;
	public void start(String hostName, int taskId){this.taskId = taskId;}
	public void process(String data){
		try{
			getOutputStream().write(data);
		}catch(StreamNotWritable e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	public void finish(){}
}

package dcc.ufmg.anthill;
/**
 * @authors: Rodrigo Caetano O. ROCHA
 * @date: 23 July 2013
 */

import java.io.IOException;

import dcc.ufmg.anthill.*;
import dcc.ufmg.anthill.util.*;
import dcc.ufmg.anthill.net.*;
import dcc.ufmg.anthill.info.*;
import dcc.ufmg.anthill.scheduler.*;
import dcc.ufmg.anthill.stream.*;

public abstract class Stream<StreamingType> {// implements Cloneable{
	private ModuleInfo moduleInfo;

	public void setModuleInfo(ModuleInfo moduleInfo){
		this.moduleInfo = moduleInfo;
	}

	public ModuleInfo getModuleInfo(){
		return this.moduleInfo;
	}

	public abstract void start(String hostName, int taskId);
	public abstract void write(StreamingType data) throws StreamNotWritable, IOException; //thow exception StreamNotWritable
	public abstract StreamingType read() throws StreamNotReadable, IOException; //thow exception StreamNotReadable
	public abstract void finish();
}

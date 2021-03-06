package dcc.ufmg.anthill.environment;
/**
 * @author Rodrigo Caetano O. ROCHA
 * @date 26 July 2013
 */

import java.io.IOException;
import java.io.File;

import java.net.InetAddress;
import java.net.UnknownHostException;

import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;

import dcc.ufmg.anthill.*;
import dcc.ufmg.anthill.util.*;
import dcc.ufmg.anthill.net.*;
import dcc.ufmg.anthill.info.*;
import dcc.ufmg.anthill.scheduler.*;
import dcc.ufmg.anthill.stream.*;

class SSHExecutor extends Executor {
	private SSHEnvironment environment;
	public SSHExecutor(SSHEnvironment environment){
		this.environment = environment;
	}

	public void run(TaskInfo taskInfo){
		HostInfo hostInfo = Settings.getHostInfo(taskInfo.getHostName());

		String anthillPath = hostInfo.getWorkspace()+Settings.getFileName();
		String appPath = hostInfo.getWorkspace()+AppSettings.getName()+"/"+AppSettings.getFileName();
		String appXml = hostInfo.getWorkspace()+AppSettings.getName()+"/"+(new File(AppSettings.getXMLFileName())).getName();
		String xml = hostInfo.getWorkspace()+AppSettings.getName()+"/"+(new File(Settings.getXMLFileName())).getName();
		String executable = "java";
		String classPath = anthillPath+":"+appPath;
		String cmd;
		if(environment.getUseHDFS()){
			executable = hostInfo.getHDFSInfo().getPath()+" jar "+anthillPath+" "+Settings.getClassName();
			//classPath = appPath;
			String exportClassPath = "export HADOOP_CLASSPATH="+classPath;
			//Logger.warning(exportClassPath);
			cmd = executable+" -tid "+taskInfo.getTaskId()+" -app-xml "+appXml+" -m "+taskInfo.getModuleInfo().getName()+" -xml "+xml+" -h "+taskInfo.getHostName()+" -sa "+WebServerSettings.getAddress()+" -sp "+WebServerSettings.getPort()+" > "+hostInfo.getWorkspace()+AppSettings.getName()+"/"+(taskInfo.getModuleInfo().getName()+"-tid"+taskInfo.getTaskId())+".log";
			cmd = exportClassPath+" && "+cmd;
		}else {
			cmd = executable+" -cp "+classPath+" "+Settings.getClassName()+" -tid "+taskInfo.getTaskId()+" -app-xml "+appXml+" -m "+taskInfo.getModuleInfo().getName()+" -xml "+xml+" -h "+taskInfo.getHostName()+" -sa "+WebServerSettings.getAddress()+" -sp "+WebServerSettings.getPort()+" > "+hostInfo.getWorkspace()+AppSettings.getName()+"/"+(taskInfo.getModuleInfo().getName()+"-tid"+taskInfo.getTaskId())+".log";
		}

		try{
			environment.getSSHHosts().get(taskInfo.getHostName()).executeInBackground(cmd);
		}catch(SSHConnectionLost e){
			Logger.warning("Connection lost with the host "+taskInfo.getHostName());
			Logger.warning("Host "+taskInfo.getHostName()+" NOT reachable");
			Settings.removeHost(taskInfo.getHostName()); //remove host
			//this.error = -1;
		}
	}
}

public class SSHEnvironment extends Environment {
	private HashMap<String, SSHHost> sshHosts = new HashMap<String, SSHHost>();
	private boolean usingHDFS = false;

	public void setUseHDFS(boolean usingHDFS){
		this.usingHDFS = usingHDFS;
	}

	public boolean getUseHDFS(){
		return this.usingHDFS;
	}

	public HashMap<String, SSHHost> getSSHHosts(){
		return this.sshHosts;
	}

	public void start(){
		validateHosts();

		HostInfo hostInfo;
		for(String hostName : Settings.getHosts()){
			hostInfo = Settings.getHostInfo(hostName);
			this.sshHosts.put(hostName, new SSHHost(hostInfo.getSSHInfo().getUser(), hostInfo.getSSHInfo().getPassword(), hostInfo.getAddress(), hostInfo.getSSHInfo().getPort()) );
		}
		
	}

	public void setup(){
		for(String hostName : Settings.getHosts()){
			SSHHost host = this.sshHosts.get(hostName);
			host.uploadFile(Settings.getFileName(), Settings.getFilePath(), Settings.getHostInfo(hostName).getWorkspace());
			host.uploadFile(AppSettings.getFileName(), AppSettings.getFilePath(), Settings.getHostInfo(hostName).getWorkspace()+AppSettings.getName()+"/");
			for(File file : AppSettings.getFiles()){
				host.uploadFile(file.getName(), file.getParent(), Settings.getHostInfo(hostName).getWorkspace()+AppSettings.getName()+"/");
			}
		}
	}
	
	public TaskMonitor instantiate(TaskInfo taskInfo){
		return new TaskMonitor(taskInfo, new SSHExecutor(this));
	}

	public void finish(){
		//DEBUG LOG
		Logger.info("Closing "+Settings.getHosts().size()+" hosts connections");
		for(String hostName : Settings.getHosts()){
			//DEBUG LOG
			Logger.info("Closing connection with host "+hostName);
			this.sshHosts.get(hostName).close();
		}
		this.sshHosts.clear();
	}

	private void validateHosts(){
		Set<String> hosts = Settings.getHosts();
		Set<String> invalidHosts = new HashSet<String>();
		InetAddress inetAddr = null;
		for(String hostName : hosts){
			HostInfo hostInfo = Settings.getHostInfo(hostName);
			if(usingHDFS && hostInfo.getHDFSInfo()==null){
				invalidHosts.add(hostName);
			}else{
				try{
					inetAddr = InetAddress.getByName(hostInfo.getAddress());
				}catch(UnknownHostException e){
					e.printStackTrace();
				}
				try{
					if(!inetAddr.isReachable(500)){ //the host is not reachable
						invalidHosts.add(hostName);
					}
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		}
		hosts = null;

		for(String hostName : invalidHosts){
			//DEBUG LOG
			Logger.warning("Host "+hostName+" NOT Reachable");
			Settings.removeHost(hostName);
		}
	}
}

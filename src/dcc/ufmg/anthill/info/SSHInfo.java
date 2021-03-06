package dcc.ufmg.anthill.info;
/**
 * @author Rodrigo Caetano O. ROCHA
 * @date 23 July 2013
 */

public class SSHInfo {
	private String user;
	private String password;
	private int port;

	public SSHInfo(String user, String password, int port){
		this.user = user;
		this.password = password;
		this.port = port;
	}
	
	public void setUser(String user){
		this.user = user;
	}

	public String getUser(){
		return this.user;
	}

	public void setPassword(String password){
		this.password = password;
	}

	public String getPassword(){
		return this.password;
	}
		
	public void setPort(int port){
		this.port = port;
	}

	public int getPort(){
		return this.port;
	}
}


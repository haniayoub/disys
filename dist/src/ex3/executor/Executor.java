package ex3.executor;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Executor extends Remote{
	public String execute(char op,int a, int b) throws RemoteException;
}

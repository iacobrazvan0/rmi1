import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ReplicaServerInterface extends Remote {
    void write(String fileName, String data) throws RemoteException;
    String read(String fileName) throws RemoteException;
}

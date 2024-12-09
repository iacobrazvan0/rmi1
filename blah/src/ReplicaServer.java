import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class ReplicaServer extends UnicastRemoteObject implements ReplicaServerInterface {
    private String name;
    private Map<String, String> fileStorage = new HashMap<>();

    public ReplicaServer(String name) throws RemoteException {
        super();
        this.name = name;
    }

    @Override
    public synchronized void write(String fileName, String data) throws RemoteException {
        fileStorage.put(fileName, data);
        System.out.println("[" + name + "] File written: " + fileName);
    }

    @Override
    public synchronized String read(String fileName) throws RemoteException {
        return fileStorage.getOrDefault(fileName, "File not found");
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java ReplicaServer <replica_name>");
            return;
        }
        String replicaName = args[0];
        try {
            LocateRegistry.createRegistry(1100 + Integer.parseInt(replicaName)); // Custom port
            ReplicaServer replica = new ReplicaServer(replicaName);
            Naming.rebind("ReplicaServer" + replicaName, replica);
            // Register with master
            MasterServerInterface master = (MasterServerInterface) Naming.lookup("//localhost/MasterServer");
            ReplicaLoc location = new ReplicaLoc(replicaName, "localhost", true);
            master.registerReplicaServer(replicaName, location);
            System.out.println("Replica Server " + replicaName + " is running...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

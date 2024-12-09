import java.rmi.Naming;

public class Client {
    public static void main(String[] args) {
        try {
            MasterServerInterface master = (MasterServerInterface) Naming.lookup("//localhost/MasterServer");
            System.out.println("Fetching replica locations...");
            var replicas = master.getReplicaLocations("anyFile");
            if (replicas.isEmpty()) {
                System.out.println("No replicas available.");
                return;
            }
            // Use the first replica for this example
            ReplicaLoc replicaLoc = replicas.get(0);
            ReplicaServerInterface replica = (ReplicaServerInterface)
                    Naming.lookup("//" + replicaLoc.getHost() + "/ReplicaServer" + replicaLoc.getId());
            // Write data
            String fileName = "testFile.txt";
            String fileContent = "Hello from Client!";
            replica.write(fileName, fileContent);
            System.out.println("File written: " + fileName);
            // Read data
            String retrievedData = replica.read(fileName);
            System.out.println("File content: " + retrievedData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

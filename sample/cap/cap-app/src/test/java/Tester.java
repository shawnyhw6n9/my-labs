import java.io.BufferedReader;
import java.io.FileReader;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

import tool.JavaUtil;

public class Tester {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        int count = 0;
        String line = "";
        long t1 = System.currentTimeMillis();   
        //get connection
        String mongoUri = "mongodb://sk:sk@localhost:27017";
        String mongoDBname = "stage";
        String collection = "array_test";
        
        MongoClientURI mongoClientURI = new MongoClientURI(mongoUri);
        MongoClient mongoClient = new MongoClient(mongoClientURI);
        MongoDatabase mongoDatabase = mongoClient.getDatabase(mongoDBname);
        
        JavaUtil j = new JavaUtil();
        j.setCollection(collection);
        
        try(FileReader fileReader = new FileReader("D:\\myProject\\MICB\\POCII\\_history\\IDFE0019_2.D10W_Norman.csv")) {
            BufferedReader reader = new BufferedReader(fileReader);
            line = reader.readLine(); //ignore line 1
            while ((line = reader.readLine()) != null){
               String[] token = line.split(","); 
               String deviceId = token[3];
               String id = token[4];
               j.queryByDeviceAndId(mongoDatabase,deviceId,id);
               count++;
               System.out.println("count:"+count+" deviceId:"+deviceId+" id:"+id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(mongoClient!=null)
            mongoClient.close();
        }
        long t2 = System.currentTimeMillis();
        System.out.println("ElapsedTime:"+(t2 - t1));
    }

}

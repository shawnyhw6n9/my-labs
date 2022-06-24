package tool;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.bson.Document;
import org.springframework.context.annotation.Bean;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

/**
 * Use Java util to gen random UUID.
 *
 * <ol>
 * Two types:
 * <li>getJavaUUID(int len)</li>
 * <li>getUUID()</li>
 * </ol>
 * 
 * 全通路場景
 * 
 * <ul>
 * <li>queryByDeviceAndId(MongoDatabase mongoDatabase, String deviceId, String id)</li>
 * </ul>
 * 
 * <pre>
 *
 * Ex:
 * tool.JavaUtil.java usage is
 * JavaUtil.getJavaUUID()	= 2956ec4d-1a63-4dbf-b9e9-b34639d6
 * JavaUtil.getJavaUUID(20)	= 90e7ecf1-f5f3-480c-b
 * JavaUtil.getUUID()		= A881911866B319762837
 * JavaUtil.queryByDeviceAndId() = xxxxx
 * </pre>
 */
public class JavaUtil {

    public static void main(String[] args) throws Exception {
        System.out.println("tool.JavaUtil.java usage:");
        System.out.printf("\tJavaUtil.getJavaUUID() \t\t= %s\n", JavaUtil.getJavaUUID());
        System.out.printf("\tJavaUtil.getJavaUUID(%d) \t= %s\n", 20, JavaUtil.getJavaUUID(20));
        System.out.printf("\tJavaUtil.getUUID() \t\t= %s\n", JavaUtil.getUUID());
        System.out.printf("\tJavaUtil.queryByDeviceAndId() \t= %s\n", JavaUtil.queryByDeviceAndId(new JavaUtil().mongoDatabase() , "D1", ""));
    }
    
    public MongoClient mongoClient() {
        MongoClientURI mongoClientURI = new MongoClientURI(uri);
        MongoClientOptions.Builder builder = new MongoClientOptions.Builder();
        builder.connectionsPerHost(3000);

        MongoClientOptions mongoClientOptions = builder.build();

        MongoClient mongoClient = new MongoClient(mongoClientURI);
        return mongoClient;
    }

    public MongoDatabase mongoDatabase() {
        MongoDatabase mongoDatabase = mongoClient().getDatabase(database);
        return mongoDatabase;
    }

    // TODO 應該由外部帶入
    private static String collection = "myCol";

    private String uri = "mongodb://sk:sk@localhost:27017";
    
    private String database = "test";

    public static final String REQUEST_DEVICE_ID = "DeviceID";
    
    public static final String REQUEST_ID = "ID";

    public static final String OBJECT_ID = "_id";

    /** channel id */
    public static final String DEVICE_ID = "DeviceId";
    
    /** 全通路識別碼 */
    public static final String UID = "UID";
    
    /** 客戶 ID */
    public static final String ID = "ID";

    /**
     * 全通路處理邏輯
     * 
     * @param mongoDatabase
     *            MongoDatabase
     * @param deviceId
     *            String
     * @param id
     *            String
     * @return globalId String
     * @throws NoSuchAlgorithmException
     */
    public static String queryByDeviceAndId(MongoDatabase mongoDatabase, String deviceId, String id) throws Exception {

        // FIXME 取得 mongoDatabase 物件
        if (mongoDatabase == null) {
            return null;
        }
        
        System.out.printf("DEVICE ID => %s, ID => %s\n", deviceId, id);
        
        String result = "";

        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection(collection);

        // CASE A : 依 Device Id 篩選查找
        FindIterable<Document> findIterable = mongoCollection.find(Filters.or(Filters.in(DEVICE_ID, deviceId), Filters.eq(DEVICE_ID, deviceId))).limit(1);

        MongoCursor<Document> mongoCursor = findIterable.iterator();

        // 篩選條件下的筆數 (須參考 find().limit(1) )
        int size = 0;

        // 需上傳更新回 Mongodb 的
        List<String> channelList = new LinkedList<String>();

        Document findOutDoc = customizeDoc(mongoCursor, size, channelList);

        if (findOutDoc == null) {

            // CASE C : DEVICE ID 查不到 改以 ID 查詢
            findIterable = mongoCollection.find(Filters.eq(ID, id)).limit(1);

            mongoCursor = findIterable.iterator();

            findOutDoc = customizeDoc(mongoCursor, size, channelList);

            if (findOutDoc == null) {

                // 需要新增 Doc

                Document document = new Document(UID, getUUID()).append(REQUEST_DEVICE_ID, deviceId);

                document.remove(OBJECT_ID);
                
                result = document.getString(UID);

                channelList.add(deviceId);
                document.remove(REQUEST_DEVICE_ID);
                document.append(DEVICE_ID, channelList);

                mongoCollection.insertOne(document);

            } else {

                // CASE B
                // 依篩選條件更新 Doc

                channelList.add(deviceId);
                Document document = new Document(DEVICE_ID, channelList);
                mongoCollection.updateMany(Filters.eq(ID, id), new Document("$set", document));

            }

        } else {

            // 依篩選條件更新 Doc

            Document document = new Document(DEVICE_ID, channelList);
            if (!id.isEmpty()) {
                document.append(ID, id);
            }
            mongoCollection.updateMany(Filters.or(Filters.in(DEVICE_ID, deviceId), Filters.eq(DEVICE_ID, deviceId)), new Document("$set", document));

        }
        
        // FIXME 待測試
        if (findOutDoc != null) {
            result = findOutDoc.getString(UID);
        }

        return result;
    }

    /**
     * 客製要輸出的 Document
     * 
     * @param mongoCursor
     * @param size
     * @param channelList
     * @return Document
     */
    private static Document customizeDoc(MongoCursor<Document> mongoCursor, int size, List<String> channelList) {

        Document doc = null;

        while (mongoCursor.hasNext()) {

            // 查出來的 Doc
            doc = mongoCursor.next();

            size++;

            doc.put(UID, doc.getOrDefault(UID, ""));

            String mb = "";
            if (doc.containsKey(DEVICE_ID)) {
                try {
                    mb = doc.getString(DEVICE_ID);
                } catch (Exception e) {
                    channelList.addAll(doc.getList(DEVICE_ID, String.class));
                    mb = channelList.isEmpty() ? "" : channelList.get(0);
                }
            }

            doc.put(REQUEST_DEVICE_ID, mb);

            // 這邊整理一下要回應的格式，不需要的就移除
            doc.remove(OBJECT_ID);
            doc.remove(DEVICE_ID);
            doc.remove(ID);

            System.out.printf("%s", doc);

            // 僅取一筆
            break;

        }
        return doc;
    }

    /**
     * Get reandom UUID.
     *
     * @param len
     * @return java.util.UUID
     */
    public static String getJavaUUID(int len) {
        return UUID.randomUUID().toString().substring(0, len);
    }

    public static String getJavaUUID() {
        return getJavaUUID(32);
    }

    /**
     * Get the UUID
     *
     * @return
     */
    public static Object getUUID() throws NoSuchAlgorithmException {
        return getJavaUUID(20);
    }

    /**
     * Random the min to max number
     *
     * @param min
     * @param max
     * @return
     */
    private static int genRandInt(int min, int max) throws NoSuchAlgorithmException {
        SecureRandom rand = getRandom();
        return rand.nextInt((max - min) + 1) + min;
    }

    /**
     * Gen Securn random
     *
     * @return
     */
    private static SecureRandom getRandom() throws NoSuchAlgorithmException {
        SecureRandom rand = SecureRandom.getInstance("SHA1PRNG");
        return rand;
    }
}

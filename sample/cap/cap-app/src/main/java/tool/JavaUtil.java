package tool;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
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
        System.out.printf("\tJavaUtil.queryByDeviceAndId() \t= %s\n", JavaUtil.queryByDeviceAndId(new MongoBean().mongoDatabase().getCollection("myCol"), "D1", ""));
    }

    public static final String REQUEST_DEVICE_ID = MongoBean.DocEnum.REQUEST_DEVICE_ID.getCode();

    public static final String REQUEST_ID = MongoBean.DocEnum.REQUEST_ID.getCode();

    public static final String OBJECT_ID = MongoBean.DocEnum.OBJECT_ID.getCode();

    /** channel id */
    public static final String DEVICE_ID = MongoBean.DocEnum.DEVICE_ID.getCode();

    /** 全通路識別碼 */
    public static final String UID = MongoBean.DocEnum.UID.getCode();

    /** 客戶 ID */
    public static final String ID = MongoBean.DocEnum.ID.getCode();

    /**
     * 全通路處理邏輯
     * 
     * @param mongoCollection
     *            MongoCollection<Document>
     * @param deviceId
     *            String
     * @param id
     *            String
     * @return globalId String
     * @throws NoSuchAlgorithmException
     */
    public static String queryByDeviceAndId(MongoCollection<Document> mongoCollection, String deviceId, String id) throws Exception {

        // FIXMEd 取得 mongoCollection 物件
        if (mongoCollection == null) {
            return null;
        }

        if (isEmpty(deviceId) && isEmpty(id)) {
            return null;
        }

        if (deviceId != null) {
            deviceId = trim(deviceId);
        } else {
            deviceId = "";
        }

        if (id != null) {
            id = trim(id);
        } else {
            id = "";
        }

        System.out.printf("DeviceID => %s, ID => %s\n", deviceId, id);

        String result = "";

        // 用 DeviceID=XXXX or ID=XXXX 查詢 Mongo
        FindIterable<Document> findIterable = mongoCollection.find(Filters.or(Filters.in(DEVICE_ID, deviceId), Filters.eq(DEVICE_ID, deviceId), Filters.eq(ID, id)));

        MongoCursor<Document> mongoCursor = findIterable.iterator();

        // 需上傳更新回 Mongodb 的
        List<String> channelList = new LinkedList<String>();

        List<Document> resultList = customizeDoc(mongoCursor, channelList);

        // A. 查無資料時
        if (isEmpty(resultList)) {

            // 需要新增 Mongo 物件

            Document document = new Document(UID, getUUID()).append(REQUEST_DEVICE_ID, deviceId);

            // 並回傳 UID
            result = trim(document.getString(UID));

            channelList.add(deviceId);

            document.append(DEVICE_ID, channelList);

            mongoCollection.insertOne(document);

        } else if (resultList.size() == 1) {
            // B. 查到一筆
            // 若 DeviceID or UID 有缺，會更新
            // 若 UID 跟輸入資料不一致，新增一個

            Document findDocument = resultList.get(0);
            if (findDocument != null) {
                result = trim(findDocument.getString(UID));
            }

            channelList.add(deviceId);
            Document document = new Document(DEVICE_ID, channelList);
            document.append(ID, id);

            if (!id.equalsIgnoreCase(trim(findDocument.getString(ID)))) {
                mongoCollection.insertOne(document);
            } else {
                mongoCollection.updateOne(Filters.eq(ID, id), new Document("$set", document));
            }

        } else {
            // C. 查到多筆
            // 以有 ID 的那一筆資料為主 DeviceID or UID 有缺，會更新
            // 若 UID 跟輸入資料不一致， 新增一個

            Document document = new Document(DEVICE_ID, channelList);
            if (!(isEmpty(id))) {
                document.append(ID, id);
            }
            mongoCollection.updateMany(Filters.or(Filters.in(DEVICE_ID, deviceId), Filters.eq(DEVICE_ID, deviceId)), new Document("$set", document));

        }

        return result;
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static String trim(String str) {
        return str == null ? null : str.trim();
    }

    public static boolean isEmpty(Collection<?> collection) {
        return (collection == null || collection.isEmpty());
    }

    /**
     * 客製要輸出的 Document
     * 
     * @param mongoCursor
     * @param size
     * @param channelList
     * @return Document
     */
    private static List<Document> customizeDoc(MongoCursor<Document> mongoCursor, List<String> channelList) {

        List<Document> resultList = new ArrayList<Document>();

        Document doc = null;

        while (mongoCursor.hasNext()) {

            // 查出來的 Doc
            doc = mongoCursor.next();

            doc.put(UID, doc.getOrDefault(UID, ""));

            String mb = "";
            if (doc.containsKey(DEVICE_ID)) {
                try {
                    mb = doc.getString(DEVICE_ID);
                } catch (Exception e) {
                    channelList.addAll(doc.getList(DEVICE_ID, String.class));
                    mb = isEmpty(channelList) ? "" : channelList.get(0);
                }
            }

            doc.put(REQUEST_DEVICE_ID, mb);

            // 這邊整理一下要回應的格式，不需要的就移除
            doc.remove(OBJECT_ID);

            System.out.printf("%s", doc);

            resultList.add(doc);
        }

        return resultList;
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

package tool;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.iisigroup.cap.app.config.MongodbConfig.DocEnum;
import com.mongodb.MongoClient;
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
@Component
@Scope("prototype")
public class JavaUtil {

    @Value("${mongodb.doc.collection:}")
    String collection;

    @Autowired(required = false)
    private MongoDatabase mongoDatabase;

    @Autowired(required = false)
    private MongoClient mongoClient;

    /** DeviceId */
    public static final String REQUEST_DEVICE_ID = DocEnum.REQUEST_DEVICE_ID.getCode();

    /** ID */
    public static final String REQUEST_ID = DocEnum.REQUEST_ID.getCode();

    /** _id */
    public static final String OBJECT_ID = DocEnum.OBJECT_ID.getCode();

    /** channel id (DeviceId) */
    public static final String DEVICE_ID = DocEnum.DEVICE_ID.getCode();

    /** 全通路識別碼 (UID) */
    public static final String UID = DocEnum.UID.getCode();

    /** 客戶 ID (ID) */
    public static final String ID = DocEnum.ID.getCode();

    /**
     * 全通路處理邏輯
     * 
     * @param mongoDatabase
     *            MongoDatabase
     * @param deviceId
     *            String
     * @param id
     *            String
     * @return globalId (UID) String
     * @throws Exception
     */
    public String queryByDeviceAndId(MongoDatabase mongoDatabase, String deviceId, String id) throws Exception {
        if (mongoDatabase == null) {
            return null;
        }
        return queryByDeviceAndId(mongoDatabase.getCollection(collection), deviceId, id);
    }

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
     * @throws Exception
     */
    public String queryByDeviceAndId(MongoCollection<Document> mongoCollection, String deviceId, String id) throws Exception {

        // FIXMEd 取得 mongoCollection 物件
        if (mongoCollection == null) {
            return null;
        }

        if (isEmpty(deviceId) && isEmpty(id)) {
            return null;
        }

        deviceId = trimNull(deviceId);
        id = trimNull(id);

        // FIXME
        // System.out.printf("DeviceID => %s, ID => %s\n", deviceId, id);

        // 用 DeviceID=XXXX or ID=XXXX 查詢 Mongo
        FindIterable<Document> findIterable = mongoCollection.find(Filters.or(Filters.eq(DEVICE_ID, deviceId), Filters.eq(ID, id)));

        MongoCursor<Document> mongoCursor = findIterable.iterator();

        List<Document> resultList = customizeDoc(mongoCursor);

        List<String> channelList = new LinkedList<String>();

        Document findDocument = null;

        Document toApplyDocument = null;

        boolean isNewOne = false;

        // A. 查無資料時
        if (isEmpty(resultList)) {

            // 需要新增 Mongo 物件

            toApplyDocument = updateDocContent((String) getUUID(), id, deviceId, channelList);

        } else if (resultList.size() == 1) {
            // B. 查到一筆
            // 若 DeviceID or UID 有缺，會更新
            // 若 UID 跟輸入資料不一致，新增一個

            findDocument = resultList.get(0);
            channelList = findDocument.getList(DEVICE_ID, String.class);

            toApplyDocument = updateDocContent(findDocument.getString(UID), id, deviceId, channelList);

            isNewOne = !isEmpty(findDocument.getString(ID)) && !id.equalsIgnoreCase(trim(findDocument.getString(ID)));

            if (isNewOne) {
                toApplyDocument.put(UID, getUUID());
            }
        } else {
            // C. 查到多筆
            // 以有 ID 的那一筆資料為主 DeviceID or UID 有缺，會更新
            // 若 UID 跟輸入資料不一致， 新增一個

            findDocument = null;
            // FIXME case 5
            // Document case5Document = null;
            Document case12Document = null;

            for (Document doc : resultList) {

                if (id.equalsIgnoreCase(trim(doc.getString(ID)))) {
                    findDocument = doc;
                    case12Document = null;
                    break;
                }

                // FIXME case 5
                // 沒有 device id ex: case 5
                // if (!doc.containsKey(DEVICE_ID) && !isEmpty(doc.getString(ID))) {
                // case5Document = doc;
                // }

                // choose empty id and update it. ex: case 12
                if (isEmpty(doc.getString(ID))) {
                    case12Document = doc;
                }

            }

            if (findDocument == null) {
                findDocument = case12Document;
                // Case 13
                if (case12Document == null) {
                    findDocument = updateDocContent((String) getUUID(), "", "", channelList);
                }
            }

            // 有 id 的都要看，不一致要新增
            isNewOne = !id.equalsIgnoreCase(trim(findDocument.getString(ID)));

            if (case12Document != null) {
                isNewOne = false;
            }

            if (findDocument != null) {
                channelList = findDocument.getList(DEVICE_ID, String.class);
            }
            List<String> toChannelList = new LinkedList<String>();
            toChannelList.addAll(channelList);

            toApplyDocument = updateDocContent(findDocument.getString(UID), id, deviceId, toChannelList);

        }

        if (toApplyDocument != null) {

            if (findDocument == null) {
                mongoCollection.insertOne(toApplyDocument);
            } else {
                if (isNewOne) {
                    mongoCollection.insertOne(toApplyDocument);
                } else {
                    boolean isNeedUpdate = checkNeedUpdate(findDocument, toApplyDocument, id, deviceId);
                    if (isNeedUpdate) {
                        mongoCollection.updateOne(Filters.eq(UID, toApplyDocument.getString(UID)), new Document("$set", toApplyDocument));
                    }
                }
            }

            // 並回傳 UID
            return trim(toApplyDocument.getString(UID));
        }

        return null;
    }

    private boolean checkNeedUpdate(Document findDocument, Document toApplyDocument, String id, String deviceId) {
        if (findDocument == null) {
            return false;
        }
        
        List<String> fChannelList = findDocument.getList(DEVICE_ID, String.class);
        List<String> toChannelList = toApplyDocument.getList(DEVICE_ID, String.class);

        // 當查詢到物件時如果 Device ID & ID 都相同就回傳 UID 然後結束
        return !(id.equalsIgnoreCase(findDocument.getString(ID)) && fChannelList.contains(deviceId) && toChannelList.contains(deviceId));
    }

    /**
     * 代入要更新的內容
     * 
     * @param uid
     * @param id
     * @param deviceId
     * @param channelList
     * @return Doc
     */
    private static Document updateDocContent(String uid, String id, String deviceId, List<String> channelList) {

        if (!(isEmpty(deviceId)) && !channelList.contains(deviceId)) {
            channelList.add(deviceId);
        }

        Document document = new Document(DEVICE_ID, channelList);
        if (!(isEmpty(id))) {
            document.append(ID, id);
        }

        if (!(isEmpty(UID))) {
            document.append(UID, uid);
        }

        return document;
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static String trim(String str) {
        return str == null ? null : str.trim();
    }

    public static String trimNull(String str) {
        return str == null ? "" : trim(str);
    }

    public static boolean isEmpty(Collection<?> collection) {
        return (collection == null || collection.isEmpty());
    }

    /**
     * 客製查詢後要輸出的 Document
     * 
     * @param mongoCursor
     * @param size
     * @param channelList
     * @return Document
     */
    private static List<Document> customizeDoc(MongoCursor<Document> mongoCursor) {

        List<Document> resultList = new ArrayList<Document>();

        Document doc = null;

        while (mongoCursor.hasNext()) {

            // 查出來的 Doc
            doc = mongoCursor.next();

            doc.put(UID, doc.getOrDefault(UID, ""));

            // 需上傳更新回 Mongodb 的
            List<String> channelList = new LinkedList<String>();

            if (doc.containsKey(DEVICE_ID)) {
                try {
                    channelList.add(doc.getString(DEVICE_ID));
                } catch (Exception e) {
                    channelList.addAll(doc.getList(DEVICE_ID, String.class));
                }
                doc.append(DEVICE_ID, channelList);
            }

            // 這邊整理一下要回應的格式，不需要的就移除
            doc.remove(OBJECT_ID);

            // FIXME
            // System.out.printf("%s", doc);

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

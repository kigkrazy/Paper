package io.paperdb;

import com.esotericsoftware.kryo.Serializer;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Fast NoSQL data storage with auto-upgrade support to save any types of Plain Old Java Objects or
 * collections using Kryo serialization.
 * <p/>
 * Every custom class must have no-arg constructor. Common classes supported out of the box.
 * <p/>
 * Auto upgrade works in a way that removed object's fields are ignored on read and new fields
 * have their default values on create class instance.
 * <p/>
 * Each object is saved in separate Paper file with name like object_key.pt.
 * All Paper files are created in the /files/io.paperdb dir in app's private storage.
 */
@SuppressWarnings({"WeakerAccess", "UnusedReturnValue"})
public class Paper {
    static final String TAG = "paperdb";

    static final String DEFAULT_DB_NAME = "io.paperdb";

    private static final ConcurrentHashMap<String, Book> mBookMap = new ConcurrentHashMap<>();
    private static final HashMap<Class, Serializer> mCustomSerializers = new HashMap<>();

    /**
     * Returns book instance with the given name
     *
     * @param name name of new database
     * @return Paper instance
     */
    public static Book book(String name) {
        if (name.equals(DEFAULT_DB_NAME))
            throw new PaperDbException(DEFAULT_DB_NAME + " name is reserved for default library name");
        return getBook(null, name);
    }

    /**
     * Returns default book instance
     *
     * @return Book instance
     */
    public static Book book() {
        return getBook(null, DEFAULT_DB_NAME);
    }

    /**
     * Returns book instance to save data at custom location, e.g. on sdcard.
     *
     * @param location the path to a folder where the book's folder will be placed
     * @param name     the name of the book
     * @return book instance
     */
    public static Book bookOn(String location, String name) {
        location = removeLastFileSeparatorIfExists(location);
        return getBook(location, name);
    }

    /**
     * Returns book instance to save data at custom location, e.g. on sdcard.
     *
     * @param location the path to a folder where the book's folder will be placed
     * @return book instance
     */
    public static Book bookOn(String location) {
        return bookOn(location, DEFAULT_DB_NAME);
    }

    private static Book getBook(String location, String name) {
        String key = (location == null ? "" : location) + name;
        synchronized (mBookMap) {
            Book book = mBookMap.get(key);
            if (book == null) {
                book = new Book(location, name, mCustomSerializers);
                mBookMap.put(key, book);
            }
            return book;
        }
    }

    private static String removeLastFileSeparatorIfExists(String customLocation) {
        if (customLocation.endsWith(File.separator)) {
            customLocation = customLocation.substring(0, customLocation.length() - 1);
        }
        return customLocation;
    }

    /**
     * Sets log level for internal Kryo serializer
     *
     * @param level one of levels from {@link com.esotericsoftware.minlog.Log }
     */
    public static void setLogLevel(int level) {
        for (Map.Entry<String, Book> entry : mBookMap.entrySet()) {
            entry.getValue().setLogLevel(level);
        }
    }

    /**
     * Adds a custom serializer for a specific class
     * When used, must be called right after Paper.init()
     *
     * @param clazz      type of the custom serializer
     * @param serializer the serializer instance
     * @param <T>        type of the serializer
     */
    public static <T> void addSerializer(Class<T> clazz, Serializer<T> serializer) {
        if (!mCustomSerializers.containsKey(clazz))
            mCustomSerializers.put(clazz, serializer);
    }
}

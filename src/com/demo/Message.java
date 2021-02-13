package com.demo;

public class Message {

    /**
     * User-defined message code so that the recipient can identify
     * what this message is about. Each {@link Handler} has its own name-space
     * for message codes, so you do not need to worry about yours conflicting
     * with other handlers.
     */
    public int what;

    public int arg1;

    public int arg2;

    public Object obj;

    /**
     * Optional Messenger where replies to this message can be sent.  The
     * semantics of exactly how this is used are up to the sender and
     * receiver.
     */

    /**
     * Indicates that the uid is not set;
     *
     * @hide Only for use within the system server.
     */
    public static final int UID_NONE = -1;

    public int sendingUid = UID_NONE;

    /**
     * Optional field indicating the uid that caused this message to be enqueued.
     *
     * @hide Only for use within the system server.
     */
    public int workSourceUid = UID_NONE;

    /**
     * If set message is in use.
     * This flag is set when the message is enqueued and remains set while it
     * is delivered and afterwards when it is recycled.  The flag is only cleared
     * when a new message is created or obtained since that is the only time that
     * applications are allowed to modify the contents of the message.
     * <p>
     * It is an error to attempt to enqueue or recycle a message that is already in use.
     */
    /*package*/ static final int FLAG_IN_USE = 1 << 0;

    /**
     * If set message is asynchronous
     */
    /*package*/ static final int FLAG_ASYNCHRONOUS = 1 << 1;

    /**
     * Flags to clear in the copyFrom method
     */
    /*package*/ static final int FLAGS_TO_CLEAR_ON_COPY_FROM = FLAG_IN_USE;

    /*package*/ int flags;
    public Handler target;

    public long when;

    /**
     * @hide
     */
    public static final Object sPoolSync = new Object();
    private static Message sPool;
    private static int sPoolSize = 0;

    private static final int MAX_POOL_SIZE = 50;

    private static boolean gCheckRecycle = true;

    /*package*/ Runnable callback;


    /*package*/ Message next;


    void recycleUnchecked() {
        // Mark the message as in use while it remains in the recycled object pool.
        // Clear out all other details.
        flags = FLAG_IN_USE;
        what = 0;
        arg1 = 0;
        arg2 = 0;
        obj = null;
        sendingUid = UID_NONE;
        workSourceUid = UID_NONE;
        when = 0;
        target = null;
        callback = null;

        synchronized (sPoolSync) {
            if (sPoolSize < MAX_POOL_SIZE) {
                next = sPool;
                sPool = this;
                sPoolSize++;
            }
        }
    }
}

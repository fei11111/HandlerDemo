package com.demo;

public class MessageQueue {

    Message mMessages;

    /**
     * 循环从队列中获取消息返回
     *
     * @return
     */
    public Message next() {
        for (; ; ) {
            synchronized (this) {
                // Try to retrieve the next message.  Return if found.
                final long now = System.currentTimeMillis();
                Message prevMsg = null;
                Message msg = mMessages;
                if (msg != null && msg.target == null) {
                    // Stalled by a barrier.  Find the next asynchronous message in the queue.
                    do {
                        prevMsg = msg;
                        msg = msg.next;
                    } while (msg != null);
                }
                if (msg != null) {
                    if (now < msg.when) {

                    } else {
                        // Got a message.
                        if (prevMsg != null) {
                            prevMsg.next = msg.next;
                        } else {
                            mMessages = msg.next;
                        }
                        msg.next = null;
                        return msg;
                    }
                } else {
                    // No more messages.
                }

            }
        }
    }

    public boolean enqueueMessage(Message msg, long when) {
        msg.when = when;
        Message p = mMessages;
        if (p == null || when == 0 || when < p.when) {
            // New head, wake up the event queue if blocked.
            msg.next = p;
            mMessages = msg;
        } else {
            // Inserted within the middle of the queue.  Usually we don't have to wake
            // up the event queue unless there is a barrier at the head of the queue
            // and the message is the earliest asynchronous message in the queue.
            Message prev;
            for (; ; ) {
                prev = p;
                p = p.next;
                if (p == null || when < p.when) {
                    break;
                }
            }
            msg.next = p; // invariant: p == prev.next
            prev.next = msg;
        }
        return true;
    }
}

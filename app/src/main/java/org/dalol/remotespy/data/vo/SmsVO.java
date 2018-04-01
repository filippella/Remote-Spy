package org.dalol.remotespy.data.vo;

/**
 * @author Filippo Engidashet [filippo.eng@gmail.com]
 * @version 1.0.0
 * @since Sunday, 25/03/2018 at 03:07.
 */

public class SmsVO {

    public String displayName;
    public String address;
    public String msg;
    public String threadId;
    public String date;
    public String type;
    public boolean isRead;
    public String subject;

    @Override
    public String toString() {
        return "SmsVO{" +
                "displayName='" + displayName + '\'' +
                ", address='" + address + '\'' +
                ", msg='" + msg + '\'' +
                ", threadId='" + threadId + '\'' +
                ", date='" + date + '\'' +
                ", type='" + type + '\'' +
                ", isRead=" + isRead +
                ", subject='" + subject + '\'' +
                '}';
    }
}

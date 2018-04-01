package org.dalol.remotespy.data.vo;

/**
 * @author Filippo Engidashet [filippo.eng@gmail.com]
 * @version 1.0.0
 * @since Sunday, 25/03/2018 at 03:40.
 */
public class ContactVO {

    public String id;
    public String name;
    public String number;
    public String photothumb;

    @Override
    public String toString() {
        return "ContactVO {" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", number='" + number + '\'' +
                ", photothumb='" + photothumb + '\'' +
                '}';
    }
}

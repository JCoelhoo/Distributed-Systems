
package pt.upa.transporter.ws;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for jobStateView.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="jobStateView">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="PROPOSED"/>
 *     &lt;enumeration value="REJECTED"/>
 *     &lt;enumeration value="ACCEPTED"/>
 *     &lt;enumeration value="HEADING"/>
 *     &lt;enumeration value="ONGOING"/>
 *     &lt;enumeration value="COMPLETED"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "jobStateView")
@XmlEnum
public enum JobStateView {

    PROPOSED,
    REJECTED,
    ACCEPTED,
    HEADING,
    ONGOING,
    COMPLETED;

    public String value() {
        return name();
    }

    public static JobStateView fromValue(String v) {
        return valueOf(v);
    }

}

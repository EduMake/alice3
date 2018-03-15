
package org.lgna.story.resourceutilities.exporterutils.collada;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for gl_front_face_type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="gl_front_face_type">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="CW"/>
 *     &lt;enumeration value="CCW"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "gl_front_face_type", namespace = "http://www.collada.org/2005/11/COLLADASchema")
@XmlEnum
public enum GlFrontFaceType {

    CW,
    CCW;

    public String value() {
        return name();
    }

    public static GlFrontFaceType fromValue(String v) {
        return valueOf(v);
    }

}

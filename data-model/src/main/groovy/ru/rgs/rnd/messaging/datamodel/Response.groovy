package ru.rgs.rnd.messaging.datamodel

import groovy.transform.ToString
import groovy.transform.builder.Builder

/**
 *
 *
 * @author jihor (jihor@ya.ru)
 * Created on 2016-09-19
 */
@Builder
@ToString
class Response implements Serializable {
    String resultCode
    String errorString
    String response
}

package ru.rgs.rnd.messaging.datamodel

import groovy.transform.builder.Builder

/**
 *
 *
 * @author jihor (jihor@ya.ru)
 * Created on 2016-09-19
 */
@Builder
class Request implements Serializable {
    String firstName
    String lastName
}
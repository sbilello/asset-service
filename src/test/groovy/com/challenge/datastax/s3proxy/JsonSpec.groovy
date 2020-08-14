package com.challenge.datastax.s3proxy

import com.challenge.datastax.s3proxy.model.ErrorMessage
import com.challenge.datastax.s3proxy.util.Json
import org.junit.Test
import spock.lang.Specification


class JsonSpec extends Specification {

    @Test
    def "it should serialize a json error message"() {
        given: "given a generic error message"
        ErrorMessage errorMessage = new ErrorMessage("test message");
        when: "Serializing"
        String errorSerialized = new String(Json.toJsonString(errorMessage));
        then:
        errorSerialized == '''{"errorMessage":"test message"}'''
    }

}

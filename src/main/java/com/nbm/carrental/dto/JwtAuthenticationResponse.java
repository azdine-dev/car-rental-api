package com.nbm.carrental.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.nbm.carrental.entity.User;
import lombok.*;

import java.io.IOException;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Builder
public class JwtAuthenticationResponse {
    @JsonSerialize(using = TokenSerializer.class)
    private String token;
    @JsonSerialize(using = TokenSerializer.class)
    private String refreshToken;
    @JsonIgnore
    private User user;


    public static class TokenSerializer extends StdSerializer<String> {
        public TokenSerializer() {
            this(null);
        }

        public TokenSerializer(Class<String> t) {
            super(t);
        }

        @Override
        public void serialize(String value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            if (value != null) {
                gen.writeString(value);
            }
        }
    }

}

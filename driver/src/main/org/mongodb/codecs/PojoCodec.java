/*
 * Copyright (c) 2008 - 2013 10gen, Inc. <http://10gen.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.mongodb.codecs;

import org.bson.BSONReader;
import org.bson.BSONWriter;
import org.mongodb.CollectibleCodec;
import org.mongodb.MongoClientException;

import java.lang.reflect.Field;

public class PojoCodec implements CollectibleCodec<Object> {
    private final PrimitiveCodecs primitiveCodecs;

    public PojoCodec(final PrimitiveCodecs primitiveCodecs) {
        this.primitiveCodecs = primitiveCodecs;
    }

    @Override
    public Object getId(final Object document) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public void encode(final BSONWriter bsonWriter, final Object value) {
        if (isBSONPrimitive(value)) {
            primitiveCodecs.encode(bsonWriter, value);
        }
        else {
            encodePojo(bsonWriter, value);
        }
    }

    @Override
    public Object decode(final BSONReader reader) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Class<Object> getEncoderClass() {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    private void encodePojo(final BSONWriter bsonWriter, final Object value) {
        System.out.println("encodePojo");
        bsonWriter.writeStartDocument();

        for (Field field : value.getClass().getFields()) {
            bsonWriter.writeName(field.getName());

            try {
                primitiveCodecs.encode(bsonWriter, field.get(value));
            } catch (IllegalAccessException e) {
                //TODO: this is really going to bugger up the writer if it throws an exception halfway through writing
                throw new EncodingException("Could not encode field '" + field.getName() + "' from " + value, e);
            }

            encode(bsonWriter, value);
        }

        bsonWriter.writeEndDocument();
    }

    private boolean isBSONPrimitive(final Object value) {
        return primitiveCodecs.canEncode(value.getClass());
    }


    private class EncodingException extends MongoClientException {
        public EncodingException(final String message, final IllegalAccessException e) {
            super(message, e);
        }
    }
}
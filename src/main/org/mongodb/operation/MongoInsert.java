/*
 * Copyright (c) 2008 - 2012 10gen, Inc. <http://10gen.com>
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
 *
 */

package org.mongodb.operation;

import org.mongodb.WriteConcern;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MongoInsert<T> extends MongoWrite {
    final Iterable<T> documents;

    // TODO: why are calls to this generating compiler warnings?
    public MongoInsert(T document, T...remainingDocuments) {
        List<T> documentList = new ArrayList<T>();
        documentList.add(document);
        Collections.addAll(documentList, remainingDocuments);
        this.documents = documentList;
    }

    public MongoInsert(Iterable<T> documents) {
        this.documents = documents;
    }

    public Iterable<T> getDocuments() {
        return documents;
    }

    @Override
    public MongoInsert<T> writeConcern(final WriteConcern writeConcern) {
        super.writeConcern(writeConcern);
        return this;
    }

    @Override
    public MongoInsert<T> writeConcernIfAbsent(final WriteConcern writeConcern) {
        super.writeConcernIfAbsent(writeConcern);
        return this;
    }
}

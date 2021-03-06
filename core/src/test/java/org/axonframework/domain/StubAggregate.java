/*
 * Copyright (c) 2010-2011. Axon Framework
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.axonframework.domain;

import org.axonframework.eventhandling.annotation.EventHandler;
import org.axonframework.eventsourcing.AbstractEventSourcedAggregateRoot;
import org.axonframework.eventsourcing.AggregateInitializer;

import java.util.UUID;

/**
 * @author Allard Buijze
 */
public class StubAggregate extends AbstractEventSourcedAggregateRoot {

    private int invocationCount;
    private final Object identifier;

    public StubAggregate() {
        this(UUID.randomUUID());
    }

    @AggregateInitializer
    public StubAggregate(Object identifier) {
        this.identifier = identifier;
    }

    public void doSomething() {
        apply(new StubDomainEvent());
    }

    public Object getIdentifier() {
        return identifier;
    }

    @EventHandler
    @Override
    protected void handle(DomainEventMessage event) {
        invocationCount++;
    }

    public int getInvocationCount() {
        return invocationCount;
    }

    public DomainEventMessage createSnapshotEvent() {
        return new GenericDomainEventMessage<StubDomainEvent>(getIdentifier(), (long) 5,
                                                              new StubDomainEvent(), MetaData.emptyInstance());
    }

    public void delete() {
        apply(new StubDomainEvent());
        markDeleted();
    }
}

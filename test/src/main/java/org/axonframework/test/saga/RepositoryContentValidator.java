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

package org.axonframework.test.saga;

import org.axonframework.saga.AssociationValue;
import org.axonframework.saga.annotation.AbstractAnnotatedSaga;
import org.axonframework.saga.repository.inmemory.InMemorySagaRepository;
import org.axonframework.test.AxonAssertionError;

import java.util.Collections;
import java.util.Set;

import static java.lang.String.format;

/**
 * Helper class for the validation of Saga Repository content.
 *
 * @author Allard Buijze
 * @since 1.1
 */
class RepositoryContentValidator {

    private final InMemorySagaRepository sagaRepository;
    private final Class<? extends AbstractAnnotatedSaga> sagaType;

    /**
     * Initialize the validator to validate contents of the given <code>sagaRepository</code>, which contains Sagas of
     * the given <code>sagaType</code>.
     *
     * @param sagaRepository The repository to monitor
     * @param sagaType       The type of saga to validate
     */
    RepositoryContentValidator(InMemorySagaRepository sagaRepository, Class<? extends AbstractAnnotatedSaga> sagaType) {
        this.sagaRepository = sagaRepository;
        this.sagaType = sagaType;
    }

    /**
     * Asserts that an association is present for the given <code>associationKey</code> and
     * <code>associationValue</code>.
     *
     * @param associationKey   The key of the association
     * @param associationValue The value of the association
     */
    public void assertAssociationPresent(String associationKey, String associationValue) {
        Set<? extends AbstractAnnotatedSaga> associatedSagas =
                sagaRepository.find(sagaType, Collections.singleton(new AssociationValue(associationKey,
                                                                                         associationValue)));
        if (associatedSagas.isEmpty()) {
            throw new AxonAssertionError(format(
                    "Expected a saga to be associated with key:<%s> value:<%s>, but found <none>",
                    associationKey,
                    associationValue));
        }
    }

    /**
     * Asserts that <em>no</em> association is present for the given <code>associationKey</code> and
     * <code>associationValue</code>.
     *
     * @param associationKey   The key of the association
     * @param associationValue The value of the association
     */
    public void assertNoAssociationPresent(String associationKey, String associationValue) {
        Set<? extends AbstractAnnotatedSaga> associatedSagas =
                sagaRepository.find(sagaType, Collections.singleton(new AssociationValue(associationKey,
                                                                                         associationValue)));
        if (!associatedSagas.isEmpty()) {
            throw new AxonAssertionError(format(
                    "Expected a saga to be associated with key:<%s> value:<%s>, but found <%s>",
                    associationKey,
                    associationValue,
                    associatedSagas.size()));
        }
    }

    /**
     * Asserts that the repsitory contains the given <code>expected</code> amount of active sagas.
     *
     * @param expected The number of expected sagas.
     */
    public void assertActiveSagas(int expected) {
        if (expected != sagaRepository.size()) {
            throw new AxonAssertionError(format("Wrong number of active sagas. Expected <%s>, got <%s>.",
                                                expected,
                                                sagaRepository.size()));
        }
    }
}

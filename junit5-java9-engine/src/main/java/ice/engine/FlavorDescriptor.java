/*
 * Copyright 2015-2017 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * http://www.eclipse.org/legal/epl-v20.html
 */

package ice.engine;

import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.support.descriptor.AbstractTestDescriptor;

class FlavorDescriptor extends AbstractTestDescriptor {

  FlavorDescriptor(UniqueId uniqueId, Flavor flavor) {
    super(uniqueId.append("Flavor", flavor.name()), flavor.name());
  }

  @Override
  public Type getType() {
    return Type.TEST;
  }
}

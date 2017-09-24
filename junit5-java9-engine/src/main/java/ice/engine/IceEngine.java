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

import org.junit.platform.engine.EngineDiscoveryRequest;
import org.junit.platform.engine.EngineExecutionListener;
import org.junit.platform.engine.ExecutionRequest;
import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.engine.TestEngine;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.support.descriptor.EngineDescriptor;

public class IceEngine implements TestEngine {

  private FlavorDescriptor sample;

  @Override
  public String getId() {
    return "ice";
  }

  @Override
  public TestDescriptor discover(EngineDiscoveryRequest discoveryRequest, UniqueId uniqueId) {
    String caption = "Ice Engine " + getVersion().get() + " (" + getArtifactId().get() + ")";
    TestDescriptor engine = new EngineDescriptor(uniqueId, caption);
    sample = new FlavorDescriptor(engine.getUniqueId(), Flavor.random());
    engine.addChild(sample);
    return engine;
  }

  @Override
  public void execute(ExecutionRequest request) {
    TestDescriptor engine = request.getRootTestDescriptor();
    EngineExecutionListener listener = request.getEngineExecutionListener();
    listener.executionStarted(engine);
    listener.executionStarted(sample);
    listener.executionFinished(sample, TestExecutionResult.successful());
    listener.executionFinished(engine, TestExecutionResult.successful());
  }
}

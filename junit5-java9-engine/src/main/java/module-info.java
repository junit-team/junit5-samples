module ice.engine {
  requires org.junit.platform.engine;

  provides org.junit.platform.engine.TestEngine with
          ice.engine.IceEngine;
}

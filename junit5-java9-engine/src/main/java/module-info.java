module ice.cream {
  requires org.junit.platform.engine;

  provides org.junit.platform.engine.TestEngine with ice.cream.Machine;
}

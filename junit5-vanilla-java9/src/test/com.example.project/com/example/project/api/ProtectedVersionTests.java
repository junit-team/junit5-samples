package com.example.project.api;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ProtectedVersionTests {
  @Test
  void versionEquals4711() {
    assertEquals("47.11", ProtectedVersion.VERSION);
  }
}

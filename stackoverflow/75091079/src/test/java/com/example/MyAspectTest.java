package com.example;

import org.junit.jupiter.api.Test;

class MyAspectTest {

  @ShouldBeWoven
  static class TargetClassInTest {

  }

  @Test
  void instantiateWovenClassInMain() {
    new TargetClassInMain();
  }

  @Test
  void instantiateWovenClassInTest() {
    new TargetClassInTest();
  }

}

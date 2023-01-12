package com.example;

import com.example.MyClass.WovenClassInMain;
import org.junit.jupiter.api.Test;

class MyAspectTest {

  @ShouldBeWoven
  static class WovenClassInTest {

  }

  @Test
  void instantiateWovenClassInMain() {
    new WovenClassInMain();
  }

  @Test
  void instantiateWovenClassInTest() {
    new WovenClassInTest();
  }

}

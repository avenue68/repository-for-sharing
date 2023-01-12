package com.example;

import io.freefair.gradle.plugins.aspectj.AspectJPostCompileWeavingPlugin;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.gradle.api.plugins.JavaPluginExtension;
import org.gradle.testfixtures.ProjectBuilder;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class PostCompileWeavingPluginTest {

  @Test
  void testPluginWithProjectBuilder() {
    var project = ProjectBuilder
        .builder()
        .build();

    project.getPlugins().apply("java");
    project.getPlugins().apply(AspectJPostCompileWeavingPlugin.class);

    var plugins = project.getPlugins();
    var extensions = project.getExtensions();
    var sourceSets = extensions
        .getByType(JavaPluginExtension.class)
        .getSourceSets();

    System.out.println(project);
  }

  @Test
  void testPluginWithGradleRunner(@TempDir File rootDir) throws IOException {
    var buildFile = new File(rootDir, "build.gradle");
    buildFile.getParentFile().mkdirs();
    try (var writer = new FileWriter(buildFile)) {
      writer.write("""
          plugins {
            id 'java'
            id 'io.freefair.aspectj.post-compile-weaving' version '6.6.1'
          }
                    
          repositories {
            mavenCentral()
          }
                    
          dependencies {
            implementation 'org.aspectj:aspectjrt:1.9.9.1'
            testImplementation platform('org.junit:junit-bom:5.9.1')
            testImplementation 'org.junit.jupiter:junit-jupiter'
          }
                    
          compileJava.ajc.options.compilerArgs << "-showWeaveInfo"
          compileJava.ajc.options.compilerArgs << "-verbose"
          compileTestJava.ajc.options.compilerArgs << "-showWeaveInfo"
          compileTestJava.ajc.options.compilerArgs << "-verbose"
          compileTestJava.ajc.options.aspectpath.from sourceSets.main.output
                              
          test {
            useJUnitPlatform()
          }
                    
          """);
    }

    var aspectClassSourceFile = new File(rootDir, "src/main/java/com/example/MyAspect.java");
    aspectClassSourceFile.getParentFile().mkdirs();
    try(var writer = new FileWriter(aspectClassSourceFile)) {
      writer.write("""
          package com.example;
                    
          import org.aspectj.lang.JoinPoint;
          import org.aspectj.lang.annotation.AfterReturning;
          import org.aspectj.lang.annotation.Aspect;
                    
          @Aspect
          public class MyAspect {
                    
            @AfterReturning("@within(com.example.ShouldBeWoven) && execution(* .new(..))")
            public void myAdvice(JoinPoint joinPoint) {
              System.out.println("******* MyAspect wove: " + joinPoint.getSignature().getDeclaringTypeName());
            }
                    
          }
          """);
    }

    var shouldBeWovenClassSourceFile = new File(rootDir, "src/main/java/com/example/ShouldBeWoven.java");
    shouldBeWovenClassSourceFile.getParentFile().mkdirs();
    try(var writer = new FileWriter(shouldBeWovenClassSourceFile)) {
      writer.write("""
          package com.example;
                    
          import static java.lang.annotation.ElementType.TYPE;
          import static java.lang.annotation.RetentionPolicy.RUNTIME;
                    
          import java.lang.annotation.Documented;
          import java.lang.annotation.Retention;
          import java.lang.annotation.Target;
                    
          @Documented
          @Target({TYPE})
          @Retention(RUNTIME)
          public @interface ShouldBeWoven {
                    
          }
          """);
    }

    var targetClassInMainSourceFile = new File(rootDir, "src/main/java/com/example/TargetClassInMain.java");
    targetClassInMainSourceFile.getParentFile().mkdirs();
    try(var writer = new FileWriter(targetClassInMainSourceFile)) {
      writer.write("""
          package com.example;
                    
          @ShouldBeWoven
          public class TargetClassInMain {
                    
          }
          """);
    }

    var MyAspectTestClassSourceFile = new File(rootDir, "src/test/java/com/example/MyAspectTest.java");
    MyAspectTestClassSourceFile.getParentFile().mkdirs();
    try(var writer = new FileWriter(MyAspectTestClassSourceFile)) {
      writer.write("""
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
          """);
    }

    var result = GradleRunner.create()
        .withProjectDir(rootDir)
        .withPluginClasspath()
        .withDebug(true)
        .forwardOutput()
        .withArguments(":test")
        .build();
  }

}

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

public class PluginTest {

  @Test
  void testPluginA() {
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
  void testPlugin(@TempDir File rootDir) throws IOException {
    var buildFile = new File(rootDir, "build.gradle");
    buildFile.getParentFile().mkdirs();
    try (var writer = new FileWriter(buildFile)) {
      writer.write("""
          plugins {
            id 'java'
            id 'io.freefair.aspectj.post-compile-weaving' version '6.6.1'
          }
          dependencies {
            implementation 'org.aspectj:aspectjrt:1.9.9.1'
          }
          """);
    }

    var result = GradleRunner.create()
        .withProjectDir(rootDir)
        .withPluginClasspath()
        .withDebug(true)
        .forwardOutput()
        .withArguments(":build")
        .build();

    System.out.println("");

  }

}

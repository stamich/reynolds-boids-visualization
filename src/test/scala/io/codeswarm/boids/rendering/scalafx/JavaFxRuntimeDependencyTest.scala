package io.codeswarm.boids.rendering.scalafx

import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

/** Regression tests for JavaFX modules required by ScalaFX at runtime. */
final class JavaFxRuntimeDependencyTest {

  /** Verifies that the JavaFX media module required by ScalaFX canvas classes is present on the runtime classpath. */
  @Test
  def mediaExceptionTypeIsAvailable(): Unit =
    assertNotNull(Class.forName("javafx.scene.media.MediaException$Type"))
}

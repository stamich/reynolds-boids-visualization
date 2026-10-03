package io.codeswarm.boids.ui

import scalafx.geometry.Insets
import scalafx.scene.control.{Label, Slider, TextField}
import scalafx.scene.layout.{HBox, VBox}

/** Slider plus editable numeric text field for a floating-point simulation parameter. */
final class DoubleParameterControl(
    labelText: String,
    minValue: Double,
    maxValue: Double,
    initialValue: Double,
    onValueChanged: Double => Unit
) extends VBox(4.0) {
  private var internalChange = false
  private var currentValue = initialValue
  private val slider = new Slider(minValue, maxValue, initialValue)
  private val field = new TextField {
    text = f"$initialValue%.3f"
    prefColumnCount = 7
  }

  padding = Insets(2.0)
  children = Seq(new Label(labelText), new HBox(6.0, slider, field))

  slider.value.onChange { (_, _, value) =>
    if (!internalChange) setValue(value.doubleValue(), notify = true)
  }
  field.onAction = _ => commitText()
  field.focused.onChange { (_, _, focused) => if (!focused) commitText() }

  /** Programmatically sets the control value and optionally invokes the callback. */
  def setValue(value: Double, notify: Boolean = false): Unit = {
    val normalized = math.max(minValue, math.min(maxValue, value))
    internalChange = true
    currentValue = normalized
    slider.value = normalized
    field.text = f"$normalized%.3f"
    internalChange = false
    if (notify) onValueChanged(normalized)
  }

  /** Returns the currently committed value. */
  def value: Double = currentValue

  private def commitText(): Unit =
    scala.util.Try(field.text.value.trim.toDouble).toOption.filter(java.lang.Double.isFinite).fold(setValue(currentValue)) { parsed =>
      setValue(parsed, notify = true)
    }
}

/** Slider plus editable numeric text field for an integer simulation parameter. */
final class IntParameterControl(
    labelText: String,
    minValue: Int,
    maxValue: Int,
    initialValue: Int,
    onValueChanged: Int => Unit
) extends VBox(4.0) {
  private var internalChange = false
  private var currentValue = initialValue
  private val slider = new Slider(minValue.toDouble, maxValue.toDouble, initialValue.toDouble) { blockIncrement = 1.0 }
  private val field = new TextField {
    text = initialValue.toString
    prefColumnCount = 7
  }

  padding = Insets(2.0)
  children = Seq(new Label(labelText), new HBox(6.0, slider, field))

  slider.value.onChange { (_, _, value) => if (!internalChange) setValue(math.round(value.doubleValue()).toInt, notify = true) }
  field.onAction = _ => commitText()
  field.focused.onChange { (_, _, focused) => if (!focused) commitText() }

  /** Programmatically sets the control value and optionally invokes the callback. */
  def setValue(value: Int, notify: Boolean = false): Unit = {
    val normalized = math.max(minValue, math.min(maxValue, value))
    internalChange = true
    currentValue = normalized
    slider.value = normalized.toDouble
    field.text = normalized.toString
    internalChange = false
    if (notify) onValueChanged(normalized)
  }

  /** Returns the currently committed value. */
  def value: Int = currentValue

  private def commitText(): Unit =
    scala.util.Try(field.text.value.trim.toInt).toOption.fold(setValue(currentValue)) { parsed => setValue(parsed, notify = true) }
}

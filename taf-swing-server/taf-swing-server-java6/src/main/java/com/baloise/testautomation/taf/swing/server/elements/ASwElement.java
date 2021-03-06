package com.baloise.testautomation.taf.swing.server.elements;

import static com.baloise.testautomation.taf.swing.server.utils.Encoder.asEscapedString;
import static com.baloise.testautomation.taf.swing.server.utils.Encoder.asEscapedXmlString;
import java.awt.Component;
import java.awt.Rectangle;
import org.assertj.swing.fixture.AbstractComponentFixture;
import com.baloise.testautomation.taf.common.interfaces.ISwApplication;
import com.baloise.testautomation.taf.common.interfaces.ISwElement;
import com.baloise.testautomation.taf.common.utils.TafProperties;
import com.baloise.testautomation.taf.swing.server.utils.SwRobotFactory;

public abstract class ASwElement implements ISwElement<Component> {

  public static String paramCommand = "command";

  public Component component;

  public TafProperties properties;
  // public String tagName;
  public long tid;

  public ASwElement(long tid, Component component) {
    // this.tagName = tagName;
    this.tid = tid;
    this.component = component;
    this.properties = new TafProperties();
    Rectangle r = component.getBounds();
    addProperty("name", component.getName());
    addProperty("tid", tid);
    addProperty("x", r.x);
    addProperty("y", r.y);
    addProperty("width", r.width);
    addProperty("height", r.height);
    addProperty("showing", Boolean.valueOf(component.isShowing()).toString());
    fillProperties();
  }

  protected void addProperty(String key, int value) {
    addProperty(key, new Integer(value).toString());
  }

  protected void addProperty(String key, long value) {
    addProperty(key, new Long(value).toString());
  }

  protected void addProperty(String key, String value) {
    if ((key != null) && (value != null)) {
      properties.putObject(key, value);
    }
  }

  protected String asEscaped(String s) {
    if (s == null) {
      return s;
    }
    return asEscapedString(s);
  }

  protected String asEscapedXml(String s) {
    if (s == null) {
      return s;
    }
    return asEscapedXmlString(s);
  }

  public abstract TafProperties basicExecCommand(TafProperties props);

  // protected void click(Component c, int mask) {
  // if (c != null) {
  // if (c.getBounds().isEmpty()) {
  // System.out.println("Can't click --> has no bounds!");
  // return;
  // }
  // waitUntilReady();
  // Point upperLeft = c.getLocationOnScreen();
  // Rectangle bounds = new Rectangle(upperLeft.x, upperLeft.y, c.getWidth(), c.getHeight());
  // Point p = new Point(new Double(bounds.getCenterX()).intValue(), new Double(bounds.getCenterY()).intValue());
  // click(p, mask);
  // }
  // }
  //
  // protected void click(Point p, int mask) {
  // while (Toolkit.getDefaultToolkit().getSystemEventQueue().peekEvent() != null) {};
  // try {
  // System.out.println("Click auf " + p);
  // Robot robot = new Robot();
  // robot.mouseMove(p.x, p.y);
  // robot.mousePress(mask);
  // robot.delay(100);
  // robot.mouseRelease(mask);
  // }
  // catch (Exception e) {
  // e.printStackTrace();
  // }
  // }

  public static long TIMEOUT_FOR_EXECUTION_IN_SECONDS = 10;
  
  public TafProperties execCommand(TafProperties props) {
    boolean timedOut = false;
    long time = System.currentTimeMillis();
    String errorMessage = "no error";
    while (!timedOut) {
      TafProperties incomingProps = new TafProperties(props);
      System.out.println("ASwElement --> execCommand");
      try {
        if (!getType().equalsIgnoreCase(incomingProps.getString("type"))) {
          return getErrorProperties("wrong type (expected = " + getType() + ", actual = "
              + incomingProps.getString("type"));
        }
        incomingProps = basicExecCommand(incomingProps);
        System.out.println("ASwElement --> execCommand successfully completed");
        return getDoneProperties(incomingProps);
      }
      catch (Exception e) {
        e.printStackTrace();
        System.out.println("ASwElement --> execCommand --> exception caught: " + e);
        errorMessage = e.toString();
        try {
          Thread.sleep(200);
        }
        catch (Exception e2) {
        }
      }
      if (System.currentTimeMillis() > time + (TIMEOUT_FOR_EXECUTION_IN_SECONDS * 1000)) {
        System.out.println("ASwElement --> execCommand --> TIMED OUT");
        timedOut = true;
      }
    }
    return getErrorProperties(errorMessage);
  }

  public abstract void fillProperties();

  // public String getTagName() {
  // if (tagName == null) {
  // return "null-tag";
  // }
  // if (tagName.isEmpty()) {
  // return "empty-tag-" + component.getClass().getSimpleName();
  // }
  // return tagName;
  // }

  public <T extends Enum<T>> T getCommand(Class<T> c, String command) {
    if (c != null && command != null) {
      try {
        return Enum.valueOf(c, command.trim().toLowerCase());
      }
      catch (IllegalArgumentException e) {}
    }
    return null;
  }

  public abstract Component getComponent();

  private TafProperties getDoneProperties(TafProperties props) {
    props.putObject("status", "done");
    return props;
  }

  private TafProperties getErrorProperties(String message) {
    TafProperties result = new TafProperties();
    result.putObject("status", "error");
    result.putObject("message", message);
    return result;
  }

  public abstract AbstractComponentFixture getFixture();

  @Override
  public TafProperties getProperties() {
    return properties;
  }

  public String getPropertiesAsString() {
    String attributes = "";
    for (Object key : getProperties().keySet()) {
      attributes = attributes + " " + key.toString() + "=\""
          + asEscapedXml(properties.getObject(key.toString()).toString()) + "\"";
    }
    return attributes;
  }

  @Override
  public Component getReference() {
    return component;
  }

  public org.assertj.swing.core.Robot getRobot() {
    return SwRobotFactory.getRobot();
  }

  public long getTID() {
    return tid;
  }

  public boolean isEnabled() {
    return getFixture().isEnabled();
  }

  @Override
  public void setApplication(ISwApplication<?> application) {}

  @Override
  public void setProperties(TafProperties props) {
    properties = props;
  }

  @Override
  public void setReference(Component component) {
    this.component = component;
  }

  @Override
  public String toString() {
    return "tagName=" + getType() + " " + component.toString();
  }

  // public void waitUntilReady() {
  // while (Toolkit.getDefaultToolkit().getSystemEventQueue().peekEvent() != null) {}
  // ;
  // long time = System.currentTimeMillis();
  // boolean isReady = false;
  // while (!isReady) {
  // try {
  // isReady = getComponent().isShowing() && getComponent().isEnabled() && getComponent().isFocusable();
  // if (!isReady) {
  // System.out.println("Wait --> element not yet enabled");
  // }
  // }
  // catch (Exception e) {}
  // if (System.currentTimeMillis() > time + 1000 * 10) {
  // // TODO Throw an exception --> Component is NOT ready for user input
  // break;
  // }
  // }
  // }

}

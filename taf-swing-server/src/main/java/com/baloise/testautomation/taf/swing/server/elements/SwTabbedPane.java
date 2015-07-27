/*
 ===========================================================================
 @    $Author$
 @  $Revision$
 @      $Date$
 @
 ===========================================================================
 */
package com.baloise.testautomation.taf.swing.server.elements;

import java.awt.Component;

import javax.swing.JTabbedPane;

import org.assertj.swing.fixture.JTabbedPaneFixture;

import com.baloise.testautomation.taf.common.utils.TafProperties;
import com.baloise.testautomation.taf.swing.base._interfaces.ISwTabbedPane;

/**
 * 
 */
public class SwTabbedPane extends ASwElement implements ISwTabbedPane<Component> {

  public SwTabbedPane(long tid, JTabbedPane component) {
    super(tid, component);
  }

  @Override
  public TafProperties basicExecCommand(TafProperties props) {
    throw new NotSupportedException("no commands implemented");
  }

  @Override
  public void fillProperties() {
    addProperty("selectedIndex", getComponent().getSelectedIndex());
    addProperty("tabCount", getComponent().getTabCount());
    addProperty("selectedTabTitle", getComponent().getTitleAt(getComponent().getSelectedIndex()));
  }

  @Override
  public JTabbedPane getComponent() {
    return (JTabbedPane)component;
  }

  @Override
  public JTabbedPaneFixture getFixture() {
    return new JTabbedPaneFixture(getRobot(), getComponent());
  }

  @Override
  public String getType() {
    return ISwTabbedPane.type;
  }

}
package com.baloise.testautomation.taf.browser.elements;

import com.baloise.testautomation.taf.base._interfaces.IData;
import com.baloise.testautomation.taf.base.types.TafString;

public class BrStringInput extends ABrInput implements IData<TafString> {

  @Override
  public TafString get() {
    String text = find().getAttribute("value");
    return new TafString(text);
  }

  @Override
  public Class<TafString> getDataTypeClass() {
    return TafString.class;
  }

  @Override
  public void setCheck(String s) {
    setCheck(new TafString(s));
  }

  @Override
  public void setCheck(TafString value) {
    checkValue = value;
  }

  @Override
  public void setFill(String s) {
    setFill(new TafString(s));
  }

  @Override
  public void setFill(TafString value) {
    fillValue = value;
  }

}
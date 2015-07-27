package com.baloise.testautomation.taf.common.interfaces;

import java.lang.annotation.Annotation;

public interface IFinder<Element> {

  public Element find(Annotation annotion);

  public Element find(Element root, Annotation annotation);

}
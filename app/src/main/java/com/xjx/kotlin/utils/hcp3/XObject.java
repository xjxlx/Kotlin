package com.xjx.kotlin.utils.hcp3;

import java.net.URI;
import java.util.Comparator;
import java.util.UUID;

public interface XObject extends Comparable<XObject> {
  Comparator<XObject> COMPARATOR = null;
}

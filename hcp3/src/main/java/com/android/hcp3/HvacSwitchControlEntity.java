package com.android.hcp3;

import de.esolutions.fw.rudi.viwi.service.hvac.v3.SwitchControlObject;
import de.esolutions.fw.util.commons.annotation.NonNull;

public class HvacSwitchControlEntity extends BaseRSIValue {
  public HvacSwitchControlEntity(@NonNull SwitchControlObject object) {
    super(object);
  }
}

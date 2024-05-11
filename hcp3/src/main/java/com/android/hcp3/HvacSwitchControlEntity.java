package com.xjx.kotlin.utils.hcp3;

import androidx.annotation.NonNull;
import de.esolutions.fw.rudi.viwi.service.hvac.v3.SwitchControlObject;

public class HvacSwitchControlEntity extends BaseRSIValue {
  public HvacSwitchControlEntity(@NonNull SwitchControlObject object) {
    super(object);
  }
}

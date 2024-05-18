package com.android.hcp3.rsi.hvac.switchcontrols;

import com.android.hcp3.BaseRSIValue;
import com.android.hcp3.rsi.hvac.VcRestrictionReason;
import com.android.hcp3.rsi.hvac.VcSwitchValue;
import de.esolutions.fw.rudi.viwi.service.hvac.v3.SwitchControlObject;
import java.util.List;
import java.util.stream.Collectors;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SwitchControlObjectEntity extends BaseRSIValue {
  private final List<VcSwitchValue> switchValueConfiguration;

  private final VcRestrictionReason restrictionReason;

  private final VcSwitchValue switchValue;

  public SwitchControlObjectEntity(SwitchControlObject object) {
    super(object);
    this.switchValueConfiguration =
        object
            .getSwitchValueConfiguration()
            .map(list -> list.stream().map(VcSwitchValue::fromRSI).collect(Collectors.toList()))
            .orElse(null);
    this.restrictionReason =
        object.getRestrictionReason().map(VcRestrictionReason::fromRSI).orElse(null);
    this.switchValue = object.getSwitchValue().map(VcSwitchValue::fromRSI).orElse(null);
  }
}

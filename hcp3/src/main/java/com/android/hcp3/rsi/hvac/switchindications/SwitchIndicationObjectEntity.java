package com.android.hcp3.rsi.hvac.switchindications;

import com.android.hcp3.BaseRSIValue;
import com.android.hcp3.rsi.hvac.VcRestrictionReason;
import com.android.hcp3.rsi.hvac.VcSwitchIndicationValue;
import de.esolutions.fw.rudi.viwi.service.hvac.v3.SwitchIndicationObject;
import java.util.List;
import java.util.stream.Collectors;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SwitchIndicationObjectEntity extends BaseRSIValue {
  private final List<VcSwitchIndicationValue> switchValueConfiguration;

  private final VcRestrictionReason restrictionReason;

  private final VcSwitchIndicationValue switchValue;

  public SwitchIndicationObjectEntity(SwitchIndicationObject object) {
    super(object);
    this.switchValueConfiguration =
        object
            .getSwitchValueConfiguration()
            .map(
                list ->
                    list.stream()
                        .map(VcSwitchIndicationValue::fromRSI)
                        .collect(Collectors.toList()))
            .orElse(null);
    this.restrictionReason =
        object.getRestrictionReason().map(VcRestrictionReason::fromRSI).orElse(null);
    this.switchValue = object.getSwitchValue().map(VcSwitchIndicationValue::fromRSI).orElse(null);
  }
}

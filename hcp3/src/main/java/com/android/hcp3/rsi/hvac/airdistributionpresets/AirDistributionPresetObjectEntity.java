package com.android.hcp3.rsi.hvac.airdistributionpresets;

import com.android.hcp3.BaseRSIValue;
import de.esolutions.fw.rudi.viwi.service.hvac.v3.AirDistributionPresetObject;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AirDistributionPresetObjectEntity extends BaseRSIValue {
  private final Boolean isActive;

  private final Boolean isBodyAvailable;

  private final Boolean isFootwellAvailable;

  private final Boolean isIndirectAvailable;

  private final Boolean isSideAvailable;

  private final Boolean isUpAvailable;

  private final VcAirDistributionPresetObjectRestrictionReasonEnum restrictionReason;

  public AirDistributionPresetObjectEntity(AirDistributionPresetObject object) {
    super(object);
    this.isActive = object.getIsActive().orElse(null);
    this.isBodyAvailable = object.getIsBodyAvailable().orElse(null);
    this.isFootwellAvailable = object.getIsFootwellAvailable().orElse(null);
    this.isIndirectAvailable = object.getIsIndirectAvailable().orElse(null);
    this.isSideAvailable = object.getIsSideAvailable().orElse(null);
    this.isUpAvailable = object.getIsUpAvailable().orElse(null);
    this.restrictionReason =
        object
            .getRestrictionReason()
            .map(VcAirDistributionPresetObjectRestrictionReasonEnum::fromRSI)
            .orElse(null);
  }
}

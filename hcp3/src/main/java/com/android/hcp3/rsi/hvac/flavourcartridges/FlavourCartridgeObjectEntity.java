package com.android.hcp3.rsi.hvac.flavourcartridges;

import com.android.hcp3.BaseRSIValue;
import com.android.hcp3.rsi.hvac.VcRestrictionReason;
import com.android.hcp3.rsi.hvac.valueindications.ValueIndicationObjectEntity;
import de.esolutions.fw.rudi.viwi.service.hvac.v3.FlavourCartridgeObject;
import java.util.List;
import java.util.stream.Collectors;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class FlavourCartridgeObjectEntity extends BaseRSIValue {
  private final List<VcFlavourCartridgeObjectIntensityValueConfigurationEnum>
      intensityValueConfiguration;

  private final VcRestrictionReason restrictionReason;

  private final ValueIndicationObjectEntity fillingLevel;

  private final VcFlavourCartridgeObjectIntensityValueEnum intensityValue;

  private final VcFlavourCartridgeObjectFlavourEnum flavour;

  public FlavourCartridgeObjectEntity(FlavourCartridgeObject object) {
    super(object);
    this.intensityValueConfiguration =
        object
            .getIntensityValueConfiguration()
            .map(
                list ->
                    list.stream()
                        .map(VcFlavourCartridgeObjectIntensityValueConfigurationEnum::fromRSI)
                        .collect(Collectors.toList()))
            .orElse(null);
    this.restrictionReason =
        object.getRestrictionReason().map(VcRestrictionReason::fromRSI).orElse(null);
    this.fillingLevel = object.getFillingLevel().map(ValueIndicationObjectEntity::new).orElse(null);
    this.intensityValue =
        object
            .getIntensityValue()
            .map(VcFlavourCartridgeObjectIntensityValueEnum::fromRSI)
            .orElse(null);
    this.flavour =
        object.getFlavour().map(VcFlavourCartridgeObjectFlavourEnum::fromRSI).orElse(null);
  }
}

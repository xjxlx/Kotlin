// package com.xjx.kotlin.utils.hcp3;
//
// import androidx.annotation.NonNull;
// import androidx.annotation.Nullable;
//
// public class AirQualityEntity extends BaseRSIValue {
//  @Nullable private final Boolean isAllFine;
//  @Nullable private final Boolean isFogRiskActive;
//  @Nullable private final Boolean isManualModeActive;
//  @Nullable private final Boolean areWindowsOpen;
//  protected AirQualityEntity(@NonNull AirQualityObject object) {
//    super(object);
//    isAllFine = object.getIsAllFine().orElse(null);
//    isFogRiskActive = object.getIsFogRiskActive().orElse(null);
//    isManualModeActive = object.getIsManualModeActive().orElse(null);
//    areWindowsOpen = object.getAreWindowsOpen().orElse(null);
//    amountOfFineParticles =
//        object.getAmountOfFineParticles().map(ValueIndicationEntity::new).orElse(null);
//    aqiValue = object.getAqiValue().map(ValueIndicationEntity::new).orElse(null);
//    countryInformation =
//        object
//            .getCountryInformation()
//            .map(AirQualityEntityCountryInformationEnum::fromObjectEnum)
//            .orElse(null);
//  }
// }

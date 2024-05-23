package technology.cariad.vehiclecontrolmanager.rsi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import de.esolutions.fw.rudi.services.RsiService;

public abstract class BaseRSIResource<E extends BaseRSIValue> {

  protected BaseRSIResource(
      @Nullable RsiService service,
      @NonNull ServiceProvider<? extends RsiService> serviceProvider) {}
}

package technology.cariad.vehiclecontrolmanager.rsi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import de.esolutions.fw.rudi.services.RsiService;

public abstract class BaseRSIResourceUpdate<E extends BaseRSIValue, U> extends BaseRSIResource<E> {

  protected BaseRSIResourceUpdate(
      @Nullable RsiService service,
      @NonNull ServiceProvider<? extends RsiService> serviceProvider) {
    super(service, serviceProvider);
  }
}

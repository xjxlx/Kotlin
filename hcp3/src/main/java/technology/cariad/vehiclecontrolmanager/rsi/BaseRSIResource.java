package technology.cariad.vehiclecontrolmanager.rsi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import de.esolutions.fw.rudi.services.RsiService;
import java.util.List;

public abstract class BaseRSIResource<E extends BaseRSIValue> {

  protected BaseRSIResource(
      @Nullable RsiService service,
      @NonNull ServiceProvider<? extends RsiService> serviceProvider) {}

  public void registerValueCallback(@NonNull ValueCallback<List<E>> callback) {}

  public void unregisterValueCallback(@NonNull ValueCallback<List<E>> callback) {}
}

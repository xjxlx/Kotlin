package technology.cariad.vehiclecontrolmanager.rsi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import de.esolutions.fw.android.rsi.client.rx.IRsiAdmin;
import de.esolutions.fw.rudi.services.RsiService;

public abstract class BaseRSIService<S extends RsiService> implements ServiceProvider<S> {

  protected BaseRSIService(@NonNull IRsiAdmin rsiAdmin) {}

  @Nullable
  @Override
  public S getServiceBlocking() {
    return null;
  }

  @NonNull
  protected <V extends BaseRSIValue, R extends BaseRSIResource<V>> R createResourceInterface(
      @NonNull Class<R> tClass) {
    return null;
  }
}

package technology.cariad.vehiclecontrolmanager.rsi;

import androidx.annotation.NonNull;
import de.esolutions.fw.android.rsi.client.rx.IRsiAdmin;
import de.esolutions.fw.rudi.services.RsiService;

public abstract class BaseRSIService<S extends RsiService> implements ServiceProvider<S> {

  protected BaseRSIService(@NonNull IRsiAdmin rsiAdmin) {}
}

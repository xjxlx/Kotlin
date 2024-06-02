package technology.cariad.vehiclecontrolmanager.rsi;

import androidx.annotation.NonNull;

public interface ValueCallback<T> {
  void onValueChange(@NonNull T t);

  void onError(@NonNull Throwable throwable);
}

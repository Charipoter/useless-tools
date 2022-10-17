package com.useless.core.manager;

import com.useless.core.discard.service.CacheService;
import com.useless.core.discard.service.SqlService;
import com.useless.core.discard.service.WorkerPoolService;
import javafx.stage.Stage;

public class CloseManager {

    private static final CacheService cacheService = SingletonManager.getInstance(CacheService.class);
    private static final SqlService sqlService = SingletonManager.getInstance(SqlService.class);
    private static final WorkerPoolService workerPoolService = SingletonManager.getInstance(WorkerPoolService.class);

    public static void setClose(Stage primaryStage) {
        primaryStage.setOnCloseRequest(event -> {
            sqlService.close();
            cacheService.close();
            workerPoolService.close();
        });
    }

    private CloseManager() {}

}

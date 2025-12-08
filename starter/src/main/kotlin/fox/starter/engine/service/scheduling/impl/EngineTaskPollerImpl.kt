package fox.starter.engine.service.scheduling.impl

import fox.starter.engine.dao.EngineTaskDao
import fox.starter.engine.service.scheduling.EngineTaskPoller
import java.util.concurrent.atomic.AtomicInteger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import org.springframework.scheduling.annotation.Scheduled

internal class EngineTaskPollerImpl(
    private val engineCoroutineScope: CoroutineScope,
    private val engineTaskDao: EngineTaskDao
) : EngineTaskPoller {

    private val mutex = Mutex()

    @Scheduled(cron = "\${engine.scheduler.poller.cron:*/1 * * * * *}")
    override fun startPollingTask() {
        engineCoroutineScope.launch {
            if (mutex.tryLock()) {
                try {
                    pollTask()
                } finally {
                    mutex.unlock()
                }
            }
        }
    }

    override suspend fun pollTask() {
        val countNotResolvedTasksAtomic = AtomicInteger(0)
        do {
            val countNotResolvedTasks = countNotResolvedTasksAtomic.get()
            if (countNotResolvedTasks < 50) {
                val tasks = engineTaskDao.pollTask()
                countNotResolvedTasksAtomic.addAndGet(tasks.size)
                tasks.forEach { task ->
                    engineCoroutineScope.launch {

                    }.invokeOnCompletion {
                        countNotResolvedTasksAtomic.addAndGet(-1)
                    }
                }
            }
            delay(1000)
        } while (countNotResolvedTasksAtomic.get() > 0)
    }
}

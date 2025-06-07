package team.flex.training.corehr.global.transaction

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.stereotype.Component
import org.springframework.transaction.support.TransactionTemplate

@Aspect
@Component
class TransactionAspect(
    private val transactionTemplate: TransactionTemplate,
) {
    @Around("@within(team.flex.training.corehr.global.annotation.Transactional) || @annotation(team.flex.training.corehr.global.annotation.Transactional)")
    fun transactional(joinPoint: ProceedingJoinPoint): Any? {
        return transactionTemplate.execute {
            try {
                joinPoint.proceed()
            } catch (e: RuntimeException) {
                it.setRollbackOnly()
                throw e
            } catch (e: Exception) {
                throw e
            }
        }
    }
}

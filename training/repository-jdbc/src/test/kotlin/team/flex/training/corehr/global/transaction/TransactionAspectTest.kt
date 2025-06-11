package team.flex.training.corehr.global.transaction

import org.aspectj.lang.ProceedingJoinPoint
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.transaction.TransactionStatus
import org.springframework.transaction.support.TransactionCallback
import org.springframework.transaction.support.TransactionTemplate

class TransactionAspectTest {

    @Test
    fun `TransactionTemplate 사용 한다`() {
        // Given
        val transactionTemplate: TransactionTemplate = mock()
        val transactionAspect = TransactionAspect(transactionTemplate)
        val joinPoint: ProceedingJoinPoint = mock()

        whenever(joinPoint.proceed()).thenReturn(Unit)
        whenever(transactionTemplate.execute(any<TransactionCallback<Any>>())).thenAnswer {
            val callback = it.arguments[0] as TransactionCallback<*>
            callback.doInTransaction(mock())
            Unit
        }

        // When
        transactionAspect.transactional(joinPoint)

        // Then
        verify(transactionTemplate).execute(any<TransactionCallback<Any>>())
        verify(joinPoint).proceed()
    }

    @Test
    fun `UnCheckedException 이 발생하면 롤백 한다`() {
        // Given
        val transactionTemplate: TransactionTemplate = mock()
        val transactionStatus: TransactionStatus = mock()
        val transactionAspect = TransactionAspect(transactionTemplate)
        val joinPoint: ProceedingJoinPoint = mock()

        whenever(joinPoint.proceed()).thenThrow(RuntimeException::class.java)
        whenever(transactionTemplate.execute(any<TransactionCallback<Any>>())).thenAnswer {
            val callback = it.arguments[0] as TransactionCallback<*>
            callback.doInTransaction(transactionStatus)
            Unit
        }

        // When
        assertThatThrownBy { transactionAspect.transactional(joinPoint) }

        // Then
        verify(transactionStatus).setRollbackOnly()
    }

    @Test
    fun `CheckedException 이 발생하면 커밋 한다`() {
        // Given
        val transactionTemplate: TransactionTemplate = mock()
        val transactionStatus: TransactionStatus = mock()
        val transactionAspect = TransactionAspect(transactionTemplate)
        val joinPoint: ProceedingJoinPoint = mock()

        whenever(joinPoint.proceed()).thenThrow(Exception::class.java)
        whenever(transactionTemplate.execute(any<TransactionCallback<Any>>())).thenAnswer {
            val callback = it.arguments[0] as TransactionCallback<*>
            callback.doInTransaction(transactionStatus)
            Unit
        }

        // When
        assertThatThrownBy { transactionAspect.transactional(joinPoint) }

        // Then
        verify(transactionStatus, never()).setRollbackOnly()
    }
}

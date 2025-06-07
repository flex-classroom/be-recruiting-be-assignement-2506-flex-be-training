package team.flex.training.corehr.global.transaction

import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.context.annotation.EnableAspectJAutoProxy

@AutoConfiguration
@EnableAspectJAutoProxy(proxyTargetClass = true)
class TransactionConfig

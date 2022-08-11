package ru.jeinmentalist.mail.domain.timestamp.timstampUseCase

data class TimestampsUseCases(
    val addTimestampsList: AddTimestampsListUseCase,
    val loadTimestampList: LoadTimestampListUseCase,
    val updateTimestamp: UpdateTimestampUseCase
)
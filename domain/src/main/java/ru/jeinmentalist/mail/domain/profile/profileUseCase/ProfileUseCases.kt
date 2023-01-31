package ru.jeinmentalist.mail.domain.profile.profileUseCase

data class ProfileUseCases(
    val addProfile: AddProfileUseCase,
    val getProfileById: GetProfileByIdUseCase,
    val getProfileListFlow: GetProfileListFlowUseCase,
    val getProfileList: GetProfileListUseCase,
    val removeProfile: RemoveProfileUseCase,
    val updateProfile: UpdateProfileUseCase
)
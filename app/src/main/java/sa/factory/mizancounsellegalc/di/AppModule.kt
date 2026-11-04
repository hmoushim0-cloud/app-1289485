package sa.factory.mizancounsellegalc.di

import sa.factory.mizancounsellegalc.data.repository.MizanCounselRepositoryImpl
import sa.factory.mizancounsellegalc.domain.repository.MizanCounselRepository
import sa.factory.mizancounsellegalc.domain.usecase.*
import sa.factory.mizancounsellegalc.hardware.HardwareSensorDriver
import sa.factory.mizancounsellegalc.ui.viewmodel.MizanCounselViewModel

class DiContainer {
    val sensorDriver = HardwareSensorDriver()
    val repository: MizanCounselRepository = MizanCounselRepositoryImpl(sensorDriver)

    val getCatalogUseCase = GetCatalogUseCase(repository)
    val manageCartUseCase = ManageCartUseCase(repository)
    val processCheckoutUseCase = ProcessCheckoutUseCase(repository)
    val observeHardwareTelemetryUseCase = ObserveHardwareTelemetryUseCase(repository)

    fun provideViewModel(): MizanCounselViewModel = MizanCounselViewModel(
        getCatalogUseCase,
        manageCartUseCase,
        processCheckoutUseCase,
        observeHardwareTelemetryUseCase
    )
}

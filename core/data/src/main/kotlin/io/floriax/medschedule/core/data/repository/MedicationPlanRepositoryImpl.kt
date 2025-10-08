package io.floriax.medschedule.core.data.repository

import androidx.room.withTransaction
import io.floriax.medschedule.core.data.local.AppDatabase
import io.floriax.medschedule.core.data.local.dao.MedicationDoseDao
import io.floriax.medschedule.core.data.local.dao.MedicationIntakeDao
import io.floriax.medschedule.core.data.local.dao.MedicationPlanDao
import io.floriax.medschedule.core.data.local.dao.MedicationScheduleDao
import io.floriax.medschedule.core.data.local.mapper.toEntity
import io.floriax.medschedule.core.data.local.mapper.toModel
import io.floriax.medschedule.core.domain.model.MedicationPlan
import io.floriax.medschedule.core.domain.model.MedicationSchedule
import io.floriax.medschedule.core.domain.repository.MedicationPlanRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 *
 * @author WangZhiYao
 * @since 2025/10/5
 */
class MedicationPlanRepositoryImpl @Inject constructor(
    private val appDatabase: AppDatabase,
    private val medicationPlanDao: MedicationPlanDao,
    private val medicationScheduleDao: MedicationScheduleDao,
    private val medicationIntakeDao: MedicationIntakeDao,
    private val medicationDoseDao: MedicationDoseDao,
) : MedicationPlanRepository {

    override fun observeAll(): Flow<List<MedicationPlan>> =
        medicationPlanDao.observeAll().map { planWithDetails ->
            planWithDetails.map { planWithDetail -> planWithDetail.toModel() }
        }

    override suspend fun getById(id: Long): MedicationPlan? =
        medicationPlanDao.getById(id)?.toModel()

    override suspend fun create(plan: MedicationPlan): MedicationPlan =
        appDatabase.withTransaction {
            val planId = medicationPlanDao.insert(plan.toEntity())
            val schedule = plan.schedule
            val scheduleId = medicationScheduleDao.insert(schedule.toEntity(planId))

            when (schedule) {
                is MedicationSchedule.OneTime -> {
                    schedule.intakes.forEach { intake ->
                        val intakeId = medicationIntakeDao.insert(intake.toEntity(scheduleId))
                        val doseEntities = intake.medicationDoses.map { it.toEntity(intakeId) }
                        medicationDoseDao.insertBatch(doseEntities)
                    }
                }

                is MedicationSchedule.Repetitive.CustomCycle -> {
                    schedule.cycleDays.forEach { cycleDay ->
                        cycleDay.intakes.forEach { intake ->
                            val intakeId = medicationIntakeDao.insert(
                                intake.toEntity(scheduleId, cycleDay.dayOfCycle)
                            )
                            val doseEntities = intake.medicationDoses.map { it.toEntity(intakeId) }
                            medicationDoseDao.insertBatch(doseEntities)
                        }
                    }
                }

                is MedicationSchedule.Repetitive -> {
                    val intakes = when (schedule) {
                        is MedicationSchedule.Repetitive.Daily -> schedule.intakes
                        is MedicationSchedule.Repetitive.Weekly -> schedule.intakes
                        is MedicationSchedule.Repetitive.Interval -> schedule.intakes
                        else -> emptyList()
                    }
                    intakes.forEach { intake ->
                        val intakeId = medicationIntakeDao.insert(intake.toEntity(scheduleId))
                        val doseEntities = intake.medicationDoses.map { it.toEntity(intakeId) }
                        medicationDoseDao.insertBatch(doseEntities)
                    }
                }
            }

            plan.copy(id = planId)
        }
}

